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
import {FormControl, MenuItem, Select, Stack, Typography} from "@mui/material";
import EditorContainer from "@/app/game-session/session/[id]/_components/EditorContainer";
import {PlayArrow} from "@mui/icons-material";
import Editor from '@monaco-editor/react';
import {LoadingButton} from '@mui/lab';
import {useSubmitCode} from "@/app/utils/api/game-session/useSubmitCode";
import {useGetCodeResult} from "@/app/utils/api/game-session/useGetCodeResult";
import {compileStatus} from "@/app/enums/compileStatus";

const Session = ({sessionID}: { sessionID: string }) => {

    // TODO: Thommy - Mit Backend Team absprechen das wir die neue Struktur bekommen, dann hauts auch hin
    // const nodes: NodeV2Props[] = [
    //     { type: NodeType.STORY, position: { top: "20%", left: "30%" }, nodeInfos: { desc: "Story Node", title: "Story" } },
    //     { type: NodeType.CONSOLE, position: { top: "50%", left: "60%" }, nodeInfos: { desc: "Story Node", title: "Story" } },
    //     { type: NodeType.DETAILS, position: { top: "70%", left: "40%" }, nodeInfos: { desc: "Story Node", title: "Story" } },
    //     { type: NodeType.ZOOM, position: { top: "50%", left: "40%" }, nodeInfos: { desc: "Story Node", title: "Story" } },
    // ];

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
        console.log("NewStage:", newStage)
        if (newStage) {
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
        //aber das getCodeResult stimmt iwi noch nicht
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
                                variant={"standard"}>
                                {
                                    Object.keys(CodeLanguage).map(language => {
                                        return (
                                            <MenuItem key={language}
                                                      value={language}> {language[0]}{language.slice(1).toLowerCase()} </MenuItem>
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

            <div className="relative w-full mx-auto">
                <img
                    src={`${stageState.stageScene?.bgImg}`}
                    alt="Background"
                    className="w-full h-auto bg-no-repeat bg-contain"
                />
                {
                    // //TODO: Replace with new Nodes when structure is reworked
                    // nodes.map(({type, position, nodeInfos}, idx) => {
                    //     return (
                    //         <NodeV2 key={idx} type={type} position={position} nodeInfos={nodeInfos} />
                    //     )
                    // })
                    stageState.stageScene?.nodes.map((node, idx) => {
                        return (
                            <Node
                                key={idx}
                                pos={node.pos}
                                nodeInfos={node.nodeInfos}
                                type={node.type}
                                codeSetter={setCode}
                            />
                        )
                    })
                }
            </div>
        </Stack>
    );
};

export default Session;