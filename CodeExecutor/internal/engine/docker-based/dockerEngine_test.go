package docker_based

import (
	"bytes"
	"errors"
	"github.com/megamxl/escape-doom/CodeExecutor/internal/constants"
	"github.com/stretchr/testify/assert"
	"log"
	"os"
	"path/filepath"
	"testing"
)

func TestCopy(t *testing.T) {
	src := "test-src.txt"
	dst := "test-dst.txt"

	// Create mock source file
	file, err := os.Create(src)
	if err != nil {
		t.Fatalf("Failed to create mock source file: %v", err)
	}
	_, err = file.WriteString("mock content")
	if err != nil {
		t.Fatalf("Failed to write to mock source file: %v", err)
	}
	file.Close()

	// Test the copy function
	nBytes, err := copy(src, dst)
	if err != nil {
		t.Fatalf("Expected no error, got: %v", err)
	}
	if nBytes == 0 {
		t.Fatalf("Expected bytes to be copied, got: %d", nBytes)
	}

	// Clean up files
	os.Remove(src)
	os.Remove(dst)
}

func TestGetDockerLanguageParameters(t *testing.T) {
	tests := []struct {
		name           string
		input          *constants.Request
		expectedDocker string
		expectedSample string
	}{
		{
			name:           "JAVA language",
			input:          &constants.Request{Language: "JAVA"},
			expectedDocker: "internal/engine/docker-based/java.Dockerfile",
			expectedSample: "app.java",
		},
		{
			name:           "JAVASCRIPT language",
			input:          &constants.Request{Language: "JAVASCRIPT"},
			expectedDocker: "internal/engine/docker-based/javascript.Dockerfile",
			expectedSample: "app.js",
		},
		{
			name:           "PYTHON language",
			input:          &constants.Request{Language: "PYTHON"},
			expectedDocker: "internal/engine/docker-based/python.Dockerfile",
			expectedSample: "app.py",
		},
		{
			name:           "Unknown language",
			input:          &constants.Request{Language: "RUBY"},
			expectedDocker: "java.Dockerfile",
			expectedSample: "app.java",
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			dockerfile, samplefile := GetDockerLanguageParameters(tt.input)
			assert.Equal(t, tt.expectedDocker, dockerfile, "Dockerfile mismatch for %s", tt.name)
			assert.Equal(t, tt.expectedSample, samplefile, "Samplefile mismatch for %s", tt.name)
		})
	}
}

func TestCreateFileForExecution(t *testing.T) {
	// Create a temporary directory for test files
	tempDir := t.TempDir()

	tests := []struct {
		name           string
		input          *constants.Request
		samplefile     string
		shouldExecute  bool
		expectedExists bool
	}{

		{
			name: "File already exists",
			input: &constants.Request{
				PlayerSessionId: "session2",
				Code:            "print('Hello again!')",
			},
			samplefile:     "existing.py",
			shouldExecute:  true,
			expectedExists: true,
		},
	}

	// Pre-create a file for the second test case
	preExistingFile := filepath.Join(tempDir, "session2", "existing.py")
	err := os.MkdirAll(filepath.Dir(preExistingFile), 0755)
	if err != nil {
		t.Fatalf("Failed to create directory: %v", err)
	}
	err = os.WriteFile(preExistingFile, []byte("Existing content"), 0644)
	if err != nil {
		t.Fatalf("Failed to create pre-existing file: %v", err)
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			// Adjust the PlayerSessionId path to use the temporary directory
			tt.input.PlayerSessionId = filepath.Join(tempDir, tt.input.PlayerSessionId)

			_ = createFileForExecution(tt.input, tt.samplefile, tt.shouldExecute)

			// Construct the expected file path
			expectedFile := filepath.Join(tt.input.PlayerSessionId, tt.samplefile)

			// Check if the file exists
			if tt.expectedExists {
				if _, err := os.Stat(expectedFile); os.IsNotExist(err) {
					t.Errorf("Expected file %s to exist, but it does not", expectedFile)
				}
			} else {
				if _, err := os.Stat(expectedFile); err == nil {
					t.Errorf("Did not expect file %s to exist, but it does", expectedFile)
				}
			}
		})
	}
}

func TestMakeDirectory(t *testing.T) {
	// Create a temporary directory for the tests
	tempDir := t.TempDir()

	tests := []struct {
		name          string
		input         *constants.Request
		shouldExecute bool
		expectExists  bool
	}{
		{
			name: "Create new directory",
			input: &constants.Request{
				PlayerSessionId: filepath.Join(tempDir, "newDir"),
			},
			shouldExecute: true,
			expectExists:  true,
		},
		{
			name: "Directory already exists",
			input: &constants.Request{
				PlayerSessionId: filepath.Join(tempDir, "existingDir"),
			},
			shouldExecute: true,
			expectExists:  true,
		},
	}

	// Pre-create a directory for the second test case
	existingDir := filepath.Join(tempDir, "existingDir")
	err := os.Mkdir(existingDir, os.ModePerm)
	if err != nil {
		t.Fatalf("Failed to create pre-existing directory: %v", err)
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			result := makeDirectory(tt.input, tt.shouldExecute)

			// Check the return value
			if result != tt.shouldExecute {
				t.Errorf("Expected shouldExecute to be %v, got %v", tt.shouldExecute, result)
			}

			// Verify if the directory exists
			if tt.expectExists {
				if _, err := os.Stat(tt.input.PlayerSessionId); os.IsNotExist(err) {
					t.Errorf("Expected directory %s to exist, but it does not", tt.input.PlayerSessionId)
				}
			} else {
				if _, err := os.Stat(tt.input.PlayerSessionId); err == nil {
					t.Errorf("Did not expect directory %s to exist, but it does", tt.input.PlayerSessionId)
				}
			}
		})
	}
}

func TestCheckIfCommandFailed(t *testing.T) {
	tests := []struct {
		name      string
		input     output
		expectLog bool
	}{
		{
			name:      "No error in output",
			input:     output{err: nil},
			expectLog: false,
		},
		{
			name:      "Error in output",
			input:     output{err: errors.New("command failed")},
			expectLog: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			var logBuffer bytes.Buffer
			log.SetOutput(&logBuffer) // Redirect log output to a buffer

			checkIfCommandFailed(tt.input)

			logged := logBuffer.String()
			if tt.expectLog && logged == "" {
				t.Errorf("Expected log output but got none")
			} else if !tt.expectLog && logged != "" {
				t.Errorf("Did not expect log output but got: %s", logged)
			}
		})
	}
}

func TestCheckOutputOverflow(t *testing.T) {
	tests := []struct {
		name           string
		input          output
		expectedOutput string
	}{
		{
			name:           "Output under limit",
			input:          output{out: []byte("short output")},
			expectedOutput: "short output",
		},
		{
			name:           "Output over limit",
			input:          output{out: []byte(string(make([]byte, 1001)))},
			expectedOutput: "wouldOverflow",
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			checkOutputOverflow(&tt.input)

			if string(tt.input.out) != tt.expectedOutput {
				t.Errorf("Expected output %q, got %q", tt.expectedOutput, string(tt.input.out))
			}
		})
	}
}
