package main

import (
	_ "encoding/json"
	docker_based "github.com/megamxl/escape-doom/CodeExecutor/internal/engine/docker-based"
	no_op "github.com/megamxl/escape-doom/CodeExecutor/internal/engine/no-op"
	"github.com/stretchr/testify/assert"
	"os"
	"testing"
)

func TestCreateEngineBasedOnConfig(t *testing.T) {
	// Test Docker Engine
	engine, err := createEngineBasedOnConfig("docker")
	assert.NoError(t, err)
	_, ok := engine.(docker_based.DockerEngine)
	assert.True(t, ok)

	// Test No-Op Executor
	engine, err = createEngineBasedOnConfig("no-op")
	assert.NoError(t, err)
	_, ok = engine.(no_op.NoOpExecutor)
	assert.True(t, ok)

	// Test Unknown Engine
	engine, err = createEngineBasedOnConfig("unknown")
	assert.Error(t, err)
	assert.Nil(t, engine)
	assert.Equal(t, "Unknown engine 'unknown'", err.Error())
}

func TestMainWithInvalidArgs(t *testing.T) {
	// Mock os.Args
	originalArgs := os.Args
	defer func() { os.Args = originalArgs }()

	os.Args = []string{"program-name"}

	assert.Panics(t, func() {
		main()
	}, "Usage: program-name <config-file-path>")
}

func TestCheckOutgoingTopicExisting(t *testing.T) {
	tests := []struct {
		name        string
		conf        map[string]string
		expectPanic bool
		expected    string
	}{
		{
			name:        "Valid configuration",
			conf:        map[string]string{"topic.outgoing": "outgoing-topic"},
			expectPanic: false,
			expected:    "outgoing-topic",
		},
		{
			name:        "Missing topic.outgoing key",
			conf:        map[string]string{"topic.incoming": "incoming-topic"},
			expectPanic: true,
		},
		{
			name:        "Empty topic.outgoing value",
			conf:        map[string]string{"topic.outgoing": ""},
			expectPanic: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			defer func() {
				if r := recover(); r != nil {
					if !tt.expectPanic {
						t.Errorf("Test %q panicked unexpectedly: %v", tt.name, r)
					}
				} else if tt.expectPanic {
					t.Errorf("Test %q expected a panic but did not panic", tt.name)
				}
			}()

			result := checkOutgoingTopicExisting(tt.conf)
			if !tt.expectPanic && result != tt.expected {
				t.Errorf("Test %q: expected %q, got %q", tt.name, tt.expected, result)
			}
		})
	}
}

func TestCheckIncomingTopicExisting(t *testing.T) {
	tests := []struct {
		name        string
		conf        map[string]string
		expectPanic bool
		expected    string
	}{
		{
			name:        "Valid configuration",
			conf:        map[string]string{"topic.incoming": "incoming-topic"},
			expectPanic: false,
			expected:    "incoming-topic",
		},
		{
			name:        "Missing topic.incoming key",
			conf:        map[string]string{"topic.outgoing": "outgoing-topic"},
			expectPanic: true,
		},
		{
			name:        "Empty topic.incoming value",
			conf:        map[string]string{"topic.incoming": ""},
			expectPanic: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			defer func() {
				if r := recover(); r != nil {
					if !tt.expectPanic {
						t.Errorf("Test %q panicked unexpectedly: %v", tt.name, r)
					}
				} else if tt.expectPanic {
					t.Errorf("Test %q expected a panic but did not panic", tt.name)
				}
			}()

			result := checkIncomingTopicExisting(tt.conf)
			if !tt.expectPanic && result != tt.expected {
				t.Errorf("Test %q: expected %q, got %q", tt.name, tt.expected, result)
			}
		})
	}
}
