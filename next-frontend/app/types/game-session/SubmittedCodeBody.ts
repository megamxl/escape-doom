import {CodeLanguage} from "@/app/enums/CodeLanguage";

export type SubmittedCodeBody = {
    sessionID: string,
    language: CodeLanguage,
    code: string,
    codeRiddleID: number,
    dateTime: Date
}