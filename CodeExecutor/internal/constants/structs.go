package constants

type Request struct {
	PlayerSessionId string `json:playerSessionId`
	Language        string `json:language`
	Code            string `json:code`
	DateTime        string `json:dateTime`
}
