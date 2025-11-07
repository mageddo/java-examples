package producer_record

import (
	"log"

	"github.com/confluentinc/confluent-kafka-go/v2/kafka"
	"github.com/jackc/pglogrepl"
	"github.com/jackc/pgx/v5/pgtype"
)

func MapFromInsertMessage(
	logicalMsg *pglogrepl.InsertMessageV2,
	relations *map[uint32]*pglogrepl.RelationMessageV2,
	typeMap *pgtype.Map,
) *kafka.Message {

	rel, ok := (*relations)[logicalMsg.RelationID]
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
	topic := values["nam_topic"].(string)
	key := values["txt_key"]
	value := values["txt_value"]

	headers := mapHeaders(values["txt_headers"])
	msg := &kafka.Message{
		TopicPartition: kafka.TopicPartition{
			Topic:     &topic,
			Partition: kafka.PartitionAny,
		},
	}
	if key != nil {
		msg.Key = []byte(key.(string))
	}
	if value != nil {
		msg.Value = value.([]byte)
	}
	if headers != nil {
		msg.Headers = mapHeaders(headers)
	}
	return msg
}

func mapHeaders(rawHeaders interface{}) []kafka.Header {
	var headers []kafka.Header
	if rawHeaders == nil {
		return headers
	}
	byteHeaders := DecodeHeadersFromBase64(rawHeaders.(string))
	// Converte headers map -> []kafka.Header
	if len(headers) > 0 {
		headers = make([]kafka.Header, 0, len(headers))
		for k, v := range byteHeaders {
			headers = append(headers, kafka.Header{Key: k, Value: v})
		}
	}
	return headers
}

func decodeTextColumnData(mi *pgtype.Map, data []byte, dataType uint32) (interface{}, error) {
	if dt, ok := mi.TypeForOID(dataType); ok {
		return dt.Codec.DecodeValue(mi, dataType, pgtype.TextFormatCode, data)
	}
	return string(data), nil
}
