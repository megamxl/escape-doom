package no_op

import "github.com/megamxl/escape-doom/CodeExecutor/internal/constants"

type NoOpExecutor struct{}

func (n NoOpExecutor) ExecuteCode(input *constants.Request) string {

	return "no-op executor result"
}
