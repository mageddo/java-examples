package main

import (
	"context"
	"fmt"
	"log"
	"os"
	"strings"
	"time"

	"github.com/jackc/pglogrepl"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgconn"
	"github.com/jackc/pgx/v5/pgproto3"
	"github.com/jackc/pgx/v5/pgtype"
)

/*
https://github.com/jackc/pglogrepl/blob/a9884f6bd75abf16ec97c50ca0acf4766319f4e8/example/pglogrepl_demo/main.go
*/
func main() {

	const outputPlugin = "pgoutput"
	const slotName = "pglogrepl_demo"
	const tableName = "TTO_RECORD"
	//PGLOGREPL_DEMO_CONN_STRING=postgres://kconnect:kconect@127.0.0.1:5436/db?replication=database
	conn := openReplicationConn().PgConn()
	defer conn.Close(context.Background())

	createPublication(conn, slotName, tableName)
	lsn := createReplicationSlot(conn, slotName, outputPlugin)
	listenEvents(conn, lsn)
}

func listenEvents(conn *pgconn.PgConn, lsn pglogrepl.LSN) {

	typeMap := pgtype.NewMap()
	relations := map[uint32]*pglogrepl.RelationMessageV2{}
	standbyMessageTimeout := time.Second * 10
	nextStandbyMessageDeadline := time.Now().Add(standbyMessageTimeout)

	// whenever we get StreamStartMessage we set inStream to true and then pass it to DecodeV2 function
	// on StreamStopMessage we set it back to false
	inStream := false

	for {
		if time.Now().After(nextStandbyMessageDeadline) {
			err := pglogrepl.SendStandbyStatusUpdate(context.Background(), conn, pglogrepl.StandbyStatusUpdate{
				WALWritePosition: lsn,
				WALFlushPosition: lsn,
				WALApplyPosition: lsn,
			})
			if err != nil {
				log.Fatalln("SendStandbyStatusUpdate failed:", err)
			}
			log.Printf("status=sentStandby, lsn=%s\n", lsn.String())
			nextStandbyMessageDeadline = time.Now().Add(standbyMessageTimeout)
		}

		ctx, cancel := context.WithDeadline(context.Background(), nextStandbyMessageDeadline)
		rawMsg, err := conn.ReceiveMessage(ctx)
		cancel()
		if err != nil {
			if pgconn.Timeout(err) {
				continue
			}
			log.Fatalln("ReceiveMessage failed:", err)
		}

		if errMsg, ok := rawMsg.(*pgproto3.ErrorResponse); ok {
			log.Fatalf("received Postgres WAL error: %+v", errMsg)
		}

		msg, ok := rawMsg.(*pgproto3.CopyData)
		if !ok {
			log.Printf("Received unexpected message: %T\n", rawMsg)
			continue
		}

		switch msg.Data[0] {
		case pglogrepl.PrimaryKeepaliveMessageByteID:
			pkm, err := pglogrepl.ParsePrimaryKeepaliveMessage(msg.Data[1:])
			if err != nil {
				log.Fatalln("ParsePrimaryKeepaliveMessage failed:", err)
			}
			log.Printf(
				"status=PrimaryKeepaliveMessage lsn=%s, ServerWALEnd=%s, ServerTime=%s, ReplyRequested=%t",
				lsn, pkm.ServerWALEnd, pkm.ServerTime, pkm.ReplyRequested,
			)
			if pkm.ReplyRequested {
				nextStandbyMessageDeadline = time.Time{}
			}

		case pglogrepl.XLogDataByteID:
			xld, err := pglogrepl.ParseXLogData(msg.Data[1:])
			if err != nil {
				log.Fatalln("ParseXLogData failed:", err)
			}
			log.Printf(
				"type=XLogData, WALStart=%s ServerWALEnd=%s ServerTime=%s\n",
				xld.WALStart, xld.ServerWALEnd, xld.ServerTime,
			)
			processV2(xld.WALData, relations, typeMap, &inStream, func(commitLSN pglogrepl.LSN) {
				lsn = commitLSN
			})

		}
	}
}

func openRegularConn() *pgx.Conn {
	return connect(os.Getenv("PGLOGREPL_DEMO_CONN_STRING"))
}

func openReplicationConn() *pgx.Conn {
	return connect(buildReplConnUrl())
}

func connect(connUrl string) *pgx.Conn {
	conn, err := pgx.Connect(context.Background(), connUrl)
	if err != nil {
		log.Fatalln("failed to connect to PostgreSQL server:", err)
	}
	return conn
}

func buildReplConnUrl() string {
	return fmt.Sprintf("%s?replication=database", os.Getenv("PGLOGREPL_DEMO_CONN_STRING"))
}

func createReplicationSlot(conn *pgconn.PgConn, slotName string, outputPlugin string) pglogrepl.LSN {
	//sysident, err := pglogrepl.IdentifySystem(context.Background(), conn)
	//if err != nil {
	//	log.Fatalln("IdentifySystem failed:", err)
	//}
	//log.Println("SystemID:", sysident.SystemID, "Timeline:", sysident.Timeline, "XLogPos:", sysident.XLogPos, "DBName:", sysident.DBName)

	_, err := pglogrepl.CreateReplicationSlot(
		context.Background(), conn, slotName, outputPlugin, pglogrepl.CreateReplicationSlotOptions{Temporary: false},
	)
	if err != nil {
		if !strings.Contains(err.Error(), "already exists") {
			log.Fatalln("CreateReplicationSlot failed:", err)
		}
	}
	log.Println("Created replication slot:", slotName)

	pluginArguments := []string{
		"proto_version '2'",
		fmt.Sprintf("publication_names '%s'", slotName),
		"messages 'true'",
		"streaming 'true'",
	}

	pos := findPos(slotName)

	err = pglogrepl.StartReplication(
		context.Background(),
		conn, slotName, pos, pglogrepl.StartReplicationOptions{PluginArgs: pluginArguments},
	)
	if err != nil {
		log.Fatalln("StartReplication failed:", err)
	}
	log.Println("Logical replication started on slot", slotName)
	return pos
}

func findPos(slotName string) pglogrepl.LSN {
	conn := openRegularConn()
	defer conn.Close(context.Background())

	var confirmedFlushLSNStr string
	err := conn.QueryRow(
		context.Background(),
		`SELECT confirmed_flush_lsn FROM pg_replication_slots WHERE slot_name = $1`,
		slotName,
	).Scan(&confirmedFlushLSNStr)
	if err != nil {
		log.Fatalf("could not find pos: %v", err)
	}

	confirmedLSN, err := pglogrepl.ParseLSN(confirmedFlushLSNStr)
	if err != nil {
		log.Fatalf("invalid LSN: %v", err)
	}
	return confirmedLSN
}

func createPublication(conn *pgconn.PgConn, slotName string, tableName string) {
	var result *pgconn.MultiResultReader
	var err error
	//result := conn.Exec(context.Background(), fmt.Sprintf("DROP PUBLICATION IF EXISTS %s;", slotName))
	//_, err := result.ReadAll()
	//if err != nil {
	//  log.Fatalln("drop publication if exists error", err)
	//}
	result = conn.Exec(context.Background(), fmt.Sprintf("CREATE PUBLICATION %s FOR TABLE %s;", slotName, tableName))
	_, err = result.ReadAll()
	if err != nil {
		if strings.Contains(err.Error(), "already exists") {
			return
		}
		log.Fatalln("create publication error", err)
	}
	log.Printf("create publication %s", slotName)
}

func processV2(
	walData []byte, relations map[uint32]*pglogrepl.RelationMessageV2, typeMap *pgtype.Map, inStream *bool,
	onCommit func(lsn pglogrepl.LSN),
) {
	logicalMsg, err := pglogrepl.ParseV2(walData, *inStream)
	if err != nil {
		log.Fatalf("Parse logical replication message: %s", err)
	}

	log.Printf("status=LogicalMsgReceived, type=%s", logicalMsg.Type())

	switch logicalMsg := logicalMsg.(type) {

	case *pglogrepl.RelationMessageV2:
		relations[logicalMsg.RelationID] = logicalMsg

	case *pglogrepl.InsertMessageV2:
		rel, ok := relations[logicalMsg.RelationID]
		if !ok {
			log.Fatalf("unknown relation ID %d", logicalMsg.RelationID)
		}
		values := map[string]interface{}{}
		for idx, col := range logicalMsg.Tuple.Columns {
			colName := rel.Columns[idx].Name
			switch col.DataType {
			case 'n': // null
				values[colName] = nil
			case 'u': // unchanged toast
				// This TOAST value was not changed. TOAST values are not stored in the tuple, and logical replication doesn't want to spend a disk read to fetch its value for you.
			case 't': //text
				val, err := decodeTextColumnData(typeMap, col.Data, rel.Columns[idx].DataType)
				if err != nil {
					log.Fatalln("error decoding column data:", err)
				}
				values[colName] = val
			}
		}
		log.Printf("insert for xid %d\n", logicalMsg.Xid)
		log.Printf("INSERT INTO %s.%s: %v", rel.Namespace, rel.RelationName, values)

	case *pglogrepl.CommitMessage:
		onCommit(logicalMsg.CommitLSN)
	}
}

func decodeTextColumnData(mi *pgtype.Map, data []byte, dataType uint32) (interface{}, error) {
	if dt, ok := mi.TypeForOID(dataType); ok {
		return dt.Codec.DecodeValue(mi, dataType, pgtype.TextFormatCode, data)
	}
	return string(data), nil
}
