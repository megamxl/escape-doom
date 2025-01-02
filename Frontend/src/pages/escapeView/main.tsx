import React, {useEffect, useLayoutEffect, useRef, useState} from 'react';
import Editor from '@monaco-editor/react';
import {Box, Button, FormControl, MenuItem, Select, SelectChangeEvent, Stack, Typography} from "@mui/material";
import Node from './Nodes/Node';
import {getSessionId, removeSessionId} from '../../utils/GameSessionHandler';
import {PlayArrow} from '@mui/icons-material';
import EditorContainer from './EditorContainer';
import {submitCode} from '../../hooks/submitCode';
import {getCode} from '../../hooks/getCode';
import LoadingButton from '@mui/lab/LoadingButton';
import {useNavigate} from 'react-router-dom';
import {green} from '@mui/material/colors';
import {NodeInterface, NodeType} from './Nodes/NodeInterface';
import {getStage} from '../../hooks/getStage';
import axios from "axios";

enum compileStatus {
    ERROR = "ERROR",
    COMPILED = "COMPILED",
    SUCCESS = "SUCCESS",
    WAITING = "WAITING",
    WON = "WON",
    BADREQUEST = "BADREQUEST"
}

const EscapeView = () => {

    const navigate = useNavigate()
    const sleep = (ms: number) => new Promise(r => setTimeout(r, ms));

   const backgroundRef = useRef(null)
   const [backgroundWidth, setBackgroundWidth] = useState(0);
   const [backgroundHeight, setBackgroundHeight] = useState(0);
   const [imgHeight, setImgHeight] = useState(0);

    const [code, setCode] = useState(`// To figure out what to do click on 
// - Purple Icons for the story / context for the riddle
// - Blue Icons to get additional Info for the riddle
// - Yellow Icons to get the code snipped (Dont change that :) )
//   --> Also when you are sure that you can return the solution !! Remove all Prints !!
`)

    const [language, setLangauge] = useState('java')
    const sessionID = getSessionId()
    const [sceneInfo, setSceneInfo] = useState(Object)
    const [codeExecResponse, setCodeExecResponse] = useState({
        "status" : "",
        "output" : ""
    })

    const [submitCodeBody, setSubmitCodeBody] = useState({
        "playerSessionId": sessionID,
        "language": "Java",
        "code": code,
        "codeRiddleID": 1,
        "dateTime": null
    })

    const getStageData = getStage(sessionID)
    const submitCodeCall = submitCode(`${import.meta.env.VITE_GAME_BASE_URL}/join/submitCode`, submitCodeBody)
    //@ts-ignore
    const getCodeCall = getCode(`${import.meta.env.VITE_GAME_BASE_URL}/join/getCode/${sessionID}`)

    const handleCodeSubmission = async () => {
        await submitCodeCall.refetch()
        let respo = await getCodeCall.refetch()
        while(respo.data.status === compileStatus.WAITING) {
            await sleep(500)
            respo = await getCodeCall.refetch()
        }

        switch (respo.data.status) {
            case compileStatus.WON: {
                //@ts-ignore
                removeSessionId()
                navigate(`/leaderboard/${getStageData.data.roomID}`)
                break
            }
        }
        setCodeExecResponse(respo.data)
    }

    const handleChange = (event: SelectChangeEvent) => {
        setLangauge(event.target.value as string)
    }

    const handleEditorChange = (value: any) => {
        setCode(value)
        setSubmitCodeBody({
            "playerSessionId": sessionID,
            "language": language.charAt(0).toUpperCase() + language.slice(1),
            "code": value,
            //@ts-ignore
            "codeRiddleID":  sceneInfo.codeRiddleID,
            "dateTime": null
        })
    }

    useLayoutEffect(() => {
        //@ts-ignore
        setBackgroundHeight(backgroundRef.current.clientHeight)
        //@ts-ignore
        setBackgroundWidth(backgroundRef.current.clientWidth)
        //@ts-ignore
        setImgHeight((backgroundRef.current.clientWidth / 16.0 * 9.0))
    }, [])

    useEffect(() => {
        const handleWindowResize = () => {
            //@ts-ignore
            setBackgroundHeight(backgroundRef.current.clientHeight)
            //@ts-ignore
            setBackgroundWidth(backgroundRef.current.clientWidth)
            //@ts-ignore
            setImgHeight((backgroundRef.current.clientWidth / 16.0 * 9.0))
            //@ts-ignore
            console.log('W: %d; H: %d; AH: %d', backgroundRef.current.clientWidth, backgroundRef.current.clientHeight, (backgroundRef.current.clientWidth / 16.0 * 9.0))
        }

        window.addEventListener('resize', () => {handleWindowResize()});
        return () => {
            window.removeEventListener('resize', () => {handleWindowResize()})
        }
    }, []);

    useEffect(() => {
        axios.get(`${import.meta.env.VITE_GAME_BASE_URL}/join/getStage/${sessionID}`)
            .then((response) => {
                try {
                    //@ts-ignore
                    switch (response.data.state) {
                        case "PLAYING":
                            console.log("in Playing")
                            //@ts-ignore
                            setSceneInfo(JSON.parse(response.data.stage[0])[0]);
                            break;
                        case "STOPPED":
                            console.log("in Stopped")
                            removeSessionId();
                            navigate("/");
                            break;
                        case "JOINABLE":
                            console.log("in Joinable")
                            //@ts-ignore
                            if (response.data.roomID !== null && response.data.roomID !== undefined) {
                                //@ts-ignore
                                navigate(`/lobby/${response.data.roomID}`);
                            } else {
                                navigate("/")
                            }
                            break;
                    }
                } catch (e) {
                    window.location.reload()
                }
            })
    }, [getStageData.data])

    useEffect(() => {
        if (Object.keys(sceneInfo).length !== 0) console.log(sceneInfo)
    }, [sceneInfo])

    const editorRef = useRef(null)

    const handleEditorDidMount = (editor: any, monaco: any) => {
        editorRef.current = editor
    }

    return (
        <Stack direction="row" alignItems="center" height="100vh">
            <Stack direction="column" height="100vh" maxWidth={"31.5vw"}>
                <EditorContainer>
                    <Stack direction="row" alignItems="center">
                        <Typography mx={2}> Code </Typography>
                        <FormControl variant="standard" size='small'>
                            <Select
                                labelId='languageSelect'
                                value={language}
                                label="Language"
                                onChange={handleChange}
                            > 
                                {/* <MenuItem value="javascript" > Javascript </MenuItem> */}
                                <MenuItem value="java"> Java </MenuItem>
                                {/* <MenuItem value="python"> Python </MenuItem> */}
                            </Select>
                        </FormControl>
                    </Stack>
                </EditorContainer>
                <EditorContainer sx={{flexGrow: 1, flexShrink: 1}}>
                    <Editor
                        height="100%"
                        width="30vw"
                        language={language}
                        //@ts-ignore
                        value={code}
                        onMount={handleEditorDidMount}
                        onChange={handleEditorChange}
                        theme={"vs-dark"}
                        options={{
                            wordWrap: 'on',
                            minimap: { enabled: false },
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
                            loading={getCodeCall.data?.status === compileStatus.WAITING}
                            loadingPosition="start"
                            onClick={handleCodeSubmission}
                        >
                            <span> Execute </span> 
                        </LoadingButton>
                    </Stack>
                </EditorContainer>
                <EditorContainer sx={{marginBottom: 1, height: "112px", maxHeight: "112px", overflow: "auto"}}>
                    <Stack direction="column">
                        { codeExecResponse.status === compileStatus.SUCCESS ?
                            <Box sx={{backgroundColor: green[300]}} p={1}>
                                <Typography color={green[900]}> Success! </Typography>
                                <Typography color={green[900]}> You solved this riddle, continue to the next one ? </Typography>
                                <Button sx={{color: green[900], fontWeight: 'bold'}} onClick={() => window.location.reload()}> Take me there </Button>
                            </Box>
                            :
                            <></>
                        }
                        <Box>
                            <Typography> Console Output </Typography>
                        </Box>
                        <Box overflow={"auto"}>
                            <Typography fontSize={"0.9rem"} fontWeight={"bold"} color={codeExecResponse.status === compileStatus.ERROR ? '#f00' : '#fff'}> 
                                {
                                    codeExecResponse.status === compileStatus.ERROR ? 'Error Msg' : 'Output'
                                } 
                            </Typography>
                            <Typography component="pre"> {'> '}{codeExecResponse.output} </Typography>
                        </Box>
                    </Stack>
                </EditorContainer>
            </Stack>
            <Box
                ref={backgroundRef}
                sx={{
                    width: "100%",
                    height: "100%",
                    backgroundImage: `url(${sceneInfo.bgImg})`,
                    backgroundSize: "contain",
                    backgroundRepeat: 'no-repeat',
                    backgroundPosition: 'center',
                }}
            >
                    {
                        sceneInfo.nodes ? (sceneInfo.nodes.map((node: NodeInterface, index: number) => ( 
                            <Node 
                                key={index} 
                                pos={{x: node.pos.x * backgroundWidth, y: node.pos.y * imgHeight + ((backgroundHeight - imgHeight) / 2.0)}} 
                                nodeInfos={node.nodeInfos} 
                                type={node.type as NodeType} 
                                codeSetter={setCode} 
                            />
                        ))) : <></>
                    }
            </Box>
        </Stack>
    )
};

export default EscapeView;