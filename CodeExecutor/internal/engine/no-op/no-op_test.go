package no_op

import (
	"github.com/megamxl/escape-doom/CodeExecutor/internal/constants"
	"testing"
)

func TestNoOpExecutor_ExecuteCode(t *testing.T) {
	// Create an instance of NoOpExecutor
	executor := NoOpExecutor{}

	// Create a mock input request
	input := &constants.Request{
		PlayerSessionId: "test-session",
		Language:        "PYTHON",
		Code:            "print('Hello, World!')",
	}

	// Call ExecuteCode
	result := executor.ExecuteCode(input)

	// Validate the result
	expectedResult := "no-op executor result"
	if result != expectedResult {
		t.Errorf("Expected result: %s, got: %s", expectedResult, result)
	}
}
