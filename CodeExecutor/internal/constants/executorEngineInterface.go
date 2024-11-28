package constants

type Engine interface {
	ExecuteCode(input *Request) string
}
