'use client'

import React, {useRef, useState} from 'react';
import {useGetStageInformation} from "@/app/utils/api/game-session/useGetStageInformation";
import {StageState} from "@/app/types/game-session/StageState";
import {CodeExecResponse} from "@/app/types/game-session/CodeExecResponse";
import {SubmittedCodeBody} from "@/app/types/game-session/SubmittedCodeBody";
import {CodeLanguage} from "@/app/enums/CodeLanguage";

const Session = ({sessionID}: {sessionID: string}) => {

    const [stageState, setStageState] = useState<StageState>({
        language: CodeLanguage.JAVA,
        stageScene: undefined
    })
    const [codeExecutionResponse, setCodeExecutionResponse] = useState<CodeExecResponse>()
    const [submittedCodeBody, setSubmittedCodeBody] = useState<SubmittedCodeBody>()

    const stageInformation = useGetStageInformation(sessionID)
    // const submitCode = useSubmitCode()

    const monacoEditorRef = useRef()

    const handleCodeSubmission = async () => {

    }

    const handleLanguageChange = () => {

    }

    const handleCodeChange = () => {

    }

    const handleEditorMount = (editor: any) => {
        monacoEditorRef.current = editor
    }

    return (
        <div>

        </div>
    );
};

export default Session;