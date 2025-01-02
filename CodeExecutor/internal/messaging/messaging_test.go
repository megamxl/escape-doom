package messaging

import (
	"github.com/confluentinc/confluent-kafka-go/kafka"
	"github.com/megamxl/escape-doom/CodeExecutor/internal/constants"
	"github.com/stretchr/testify/assert"
	"os"
	"testing"
)

func TestReadConfig(t *testing.T) {
	// Create a temporary config file
	configFile := "test_config.properties"
	content := `
	bootstrap.servers=localhost:9092
	# This is a comment
	key=ignored
	`
	err := os.WriteFile(configFile, []byte(content), 0644)
	assert.NoError(t, err, "Failed to create config file")
	defer os.Remove(configFile) // Cleanup

	// Test ReadConfig function
	config := ReadConfig(configFile)
	assert.Equal(t, "localhost:9092", config["bootstrap.servers"], "Bootstrap servers mismatch")
}

func TestReadKafkaConfig(t *testing.T) {
	// Create a temporary config file
	configFile := "test_kafka_config.properties"
	content := `
	bootstrap.servers=localhost:9092
	`
	err := os.WriteFile(configFile, []byte(content), 0644)
	assert.NoError(t, err, "Failed to create config file")
	defer os.Remove(configFile) // Cleanup

	// Test ReadKafkaConfig function
	configMap := ReadKafkaConfig(configFile)
	assert.Equal(t, "localhost:9092", configMap["bootstrap.servers"], "Bootstrap servers mismatch")
}

func TestSendMessage(t *testing.T) {
	topic := "test_topic"

	// Kafka configuration for testing
	conf := kafka.ConfigMap{
		"bootstrap.servers": "localhost:9092",
	}

	// Input and output
	input := &constants.Request{
		PlayerSessionId: "test-session-id",
	}
	output := "test-output-message"

	// Create a Kafka producer
	producer, err := kafka.NewProducer(&conf)
	assert.NoError(t, err, "Failed to create Kafka producer")
	defer producer.Close()

	// Channel to signal message delivery
	done := make(chan bool)

	// Start a Go routine to handle events
	go func() {
		for e := range producer.Events() {
			switch ev := e.(type) {
			case *kafka.Message:
				if ev.TopicPartition.Error != nil {
					t.Errorf("Message delivery failed: %v", ev.TopicPartition.Error)
				} else {
					assert.Equal(t, topic, *ev.TopicPartition.Topic, "Topic mismatch")
					assert.Equal(t, []byte(input.PlayerSessionId), ev.Key, "Key mismatch")
					assert.Equal(t, []byte(output), ev.Value, "Value mismatch")
					done <- true
				}
			}
		}
	}()

	// Send the message
	SendMessage(topic, conf, input, output)
}
