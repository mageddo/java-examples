package kafka

import (
"context"
"errors"
"fmt"
"time"

"github.com/confluentinc/confluent-kafka-go/v2/kafka"
)

type Producer struct {
	kp *kafka.Producer
}

func New(brokers string, extra map[string]any) (*Producer, error) {
	cfg := &kafka.ConfigMap{
		"bootstrap.servers":        brokers,
		"acks":                     "all", // confirma em todos os ISRs
		"retries":                  10,
		"retry.backoff.ms":         100,
		"max.in.flight.requests.per.connection": 5, // seguro com idempotence
		"linger.ms":                5,   // batching leve
		"batch.num.messages":       100, // batching leve
		"compression.type":         "zstd",
		"socket.timeout.ms":        30000,
		"request.timeout.ms":       30000,
	}
	for k, v := range extra {
		_ = cfg.SetKey(k, v)
	}
	p, err := kafka.NewProducer(cfg)
	if err != nil {
		return nil, err
	}
	return &Producer{kp: p}, nil
}

func (p *Producer) Close() {
	if p != nil && p.kp != nil {
		p.kp.Flush(5000) // tenta drenar por até 5s
		p.kp.Close()
	}
}

// Send publica 1 mensagem com key string, value []byte e headers.
// headers é opcional (pode ser nil). Usa delivery report com timeout.
func (p *Producer) Send(msg *kafka.Message) error {
	if p == nil || p.kp == nil {
		return errors.New("producer not initialized")
	}

	// Canal de delivery para essa mensagem
	dlv := make(chan kafka.Event, 1)

	if err := p.kp.Produce(msg, dlv); err != nil {
		close(dlv)
		return fmt.Errorf("produce: %w", err)
	}

	// Aguarda delivery (ou timeout)
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	select {
	case e := <-dlv:
		m := e.(*kafka.Message)
		close(dlv)
		if m.TopicPartition.Error != nil {
			return fmt.Errorf("delivery failed: %w", m.TopicPartition.Error)
		}
		return nil

	case <-ctx.Done():
		close(dlv)
		return fmt.Errorf("delivery timeout: %w", ctx.Err())
	}
}
