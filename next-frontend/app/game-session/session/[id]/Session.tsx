'use client'

import React, {useRef, useState} from 'react';
import {useGetStageInformation} from "@/app/utils/api/game-session/useGetStageInformation";
import {StageState} from "@/app/types/game-session/StageState";
import {CodeExecResponse} from "@/app/types/game-session/CodeExecResponse";
import {SubmittedCodeBody} from "@/app/types/game-session/SubmittedCodeBody";
import {CodeLanguage} from "@/app/enums/CodeLanguage";
import Node from './_components/Node';
import {parseStage} from "@/app/utils/parseJsonString";
import {RoomState} from "@/app/enums/RoomState";
import {Box, FormControl, MenuItem, Select, Stack, Typography} from "@mui/material";
import {StageNode} from "@/app/types/game-session/StageNode";
import EditorContainer from "@/app/game-session/session/[id]/_components/EditorContainer";
import {PlayArrow} from "@mui/icons-material";
import Editor from '@monaco-editor/react';
import {LoadingButton} from '@mui/lab';
import {useSubmitCode} from "@/app/utils/api/game-session/useSubmitCode";
import {useGetCodeResult} from "@/app/utils/api/game-session/useGetCodeResult";
import {compileStatus} from "@/app/enums/compileStatus";

const Session = ({sessionID}: { sessionID: string }) => {

    const [stageState, setStageState] = useState<StageState>({
        language: CodeLanguage.JAVA,
        stageScene: undefined
    })

    const [code, setCode] = useState<string>("Initial code");

    const [codeExecutionResponse, setCodeExecutionResponse] = useState<CodeExecResponse>({
        status: compileStatus.WAITING,
        output: ''
    })
    const [submittedCodeBody, setSubmittedCodeBody] = useState<SubmittedCodeBody>({
        playerSessionId: sessionID,
        language: stageState.language,
        code: code,
        codeRiddleID: 0,
        dateTime: new Date(Date.now())
    })
    const {refetch, data: submitCodeData, error, isFetching} = useSubmitCode(submittedCodeBody);

    const {data, isLoading} = useGetStageInformation(sessionID)
    // const submitCode = useSubmitCode()

    if (data?.state == RoomState.PLAYING && data?.stage && !stageState.stageScene) {
        const newStage = parseStage(data.stage);
        if (newStage) {
            //console.log(newStage[0])
            setStageState((prev) => ({
                ...prev,
                stageScene: newStage,
            }));
        }
    }

    const {
        refetch: refetchCodeResult,
        data: codeResultData,
        isLoading: codeResultisLoading
    } = useGetCodeResult(sessionID);
    const monacoEditorRef = useRef()

    const handleCodeSubmission = async () => {
        await refetch();
        //TODO anas - das refetch oben funktioniert und ich submitte ins backend
        //abder das getCodeResult stimmt iwi noch nicht
        await getCodeResult()
        console.log(codeResultData)
    }

    const getCodeResult = async () => {
        while (codeResultData.status === compileStatus.WAITING) {
            await refetchCodeResult();
        }
        console.log(codeResultData)
    }

    const handleLanguageChange = () => {

    }

    const handleCodeChange = (value: any) => {
        setSubmittedCodeBody({
            "playerSessionId": sessionID,
            "language": stageState.language,
            "code": value,
            "codeRiddleID": 1,
            "dateTime": new Date(Date.now())
        })
    }

    const handleEditorMount = (editor: any) => {
        monacoEditorRef.current = editor
    }

    if (isLoading) return <p>Loading...</p>;
    return (
        <Stack direction="row" alignItems="center" height="100vh">
            <Stack direction="column" height="100vh" maxWidth={"31.5vw"}>
                <EditorContainer>
                    <Stack direction="row" alignItems="center">
                        <Typography mx={2}> Code </Typography>
                        <FormControl variant="standard" size='small'>
                            <Select
                                labelId='languageSelect'
                                value={stageState.language}
                                label="Language"
                                onChange={handleLanguageChange}
                            >
                                {
                                    Object.keys(CodeLanguage).map(language => {
                                        return (
                                            <MenuItem key={language} value={language}> {language[0]}{language.slice(1).toLowerCase()} </MenuItem>
                                        )
                                    })
                                }
                            </Select>
                        </FormControl>
                    </Stack>
                </EditorContainer>
                <EditorContainer sx={{flexGrow: 1, flexShrink: 1}}>
                    <Editor
                        height="100%"
                        width="30vw"
                        language={stageState.language}
                        value={code}
                        onMount={handleEditorMount}
                        onChange={handleCodeChange}
                        theme={"vs-dark"}
                        options={{
                            wordWrap: 'on',
                            minimap: {enabled: false},
                            folding: false,
                            lineNumbersMinChars: 3,
                            scrollBeyondLastLine: false,
                            automaticLayout: true,
                        }}
                    />
                </EditorContainer>
                <EditorContainer>
                    <Stack direction="column">
                        <Typography position={{sx: 'relative', lg: 'absolute'}}> Actions </Typography>
                        <LoadingButton
                            sx={{
                                height: 60,
                                width: 250,
                                m: 1,
                                alignSelf: 'center'
                            }}
                            startIcon={<PlayArrow/>}
                            variant='contained'
                            loading={isLoading}
                            loadingPosition="start"
                            onClick={handleCodeSubmission}
                        >
                            <span> Execute </span>
                        </LoadingButton>
                    </Stack>
                </EditorContainer>
            </Stack>
            <Box
                sx={{
                    width: "100%",
                    height: "100%",
                    backgroundImage: `url(${stageState.stageScene?.bgImg})`,
                    backgroundSize: "contain",
                    backgroundRepeat: 'no-repeat',
                    backgroundPosition: 'center',
                }}
            >
                {
                    stageState.stageScene?.nodes ? (stageState.stageScene?.nodes.map((node: StageNode, index: number) => (
                        <Node
                            key={index}
                            pos={{x: node.pos.x * 1000, y: node.pos.y * 1000}}
                            nodeInfos={node.nodeInfos}
                            type={node.type}
                            codeSetter={setCode}
                        />
                    ))) : <></>
                }
            </Box>
        </Stack>
    );
};

export default Session;