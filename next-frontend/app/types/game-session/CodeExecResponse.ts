import {HttpStatusCode} from "axios";

export type CodeExecResponse = {
    status: HttpStatusCode,
    output: string
}