'use client'

import React, {useEffect, useState} from 'react';
import {Backdrop, CircularProgress, Divider, Grid2, Grow, Paper, Stack, Typography} from "@mui/material";
import {common} from "@mui/material/colors";
import {redirect} from 'next/navigation'
import UserCard from "./_components/UserCard";
import {useSession} from "@/app/utils/game-session-handler";
import {useLobbyStatus} from "@/app/hooks/student-join/useLobbyStatus";
import {LobbyState} from "@/app/types/lobby/LobbyState";
import {GAME_SESSION_APP_PATHS, LECTOR_PORTAL_API} from "@/app/constants/paths";
import {useJoinLobby} from "@/app/hooks/game-session/useJoinLobby";

const Lobby = ({lobbyID}: { lobbyID: number }) => {

    const [sessionId, setSessionId] = useSession()
    const {data: joinData, error: joinError, isError: isJoinError} = useJoinLobby(sessionId);

    const [lobbyState, setLobbyState] = useState<LobbyState>({
        name: '',
        users: [],
        countdown: 5,
        isStarted: false
    })

    const [sessionID, setSessionID] = useSession();

    const {data, isError, error} = useLobbyStatus(sessionID)

    useEffect(() => {

        console.log(joinData)

    }, [joinData]);

    useEffect(() => {
        const ws = new WebSocket("ws://localhost:8090/ws/your-name");

        ws.onopen = () => {
            console.log("WebSocket connection established");
        };

        ws.onmessage = (event) => {
            setLobbyState({...lobbyState, name: event.data});
        };

        ws.onerror = (error) => {
            console.error("WebSocket error:", error);
        };

        ws.onclose = () => {
            console.log("WebSocket connection closed");
        };

        return () => {
            ws.close();
        };
    }, []);

    useEffect(() => {
        const ws = new WebSocket("ws://localhost:8090/ws/all-names");

        ws.onopen = () => {
            console.log("WebSocket connection established");
        };

        ws.onmessage = (event) => {
            try {
                const data = JSON.parse(event.data);
                setLobbyState({...lobbyState, users: data.players || []});
            } catch (error) {
                console.error("Error parsing WebSocket message:", error);
            }
        };

        ws.onerror = (error) => {
            console.error("WebSocket error:", error);
        };

        ws.onclose = () => {
            console.log("WebSocket connection closed");
        };

        return () => {
            ws.close();
        };
    }, []);

    useEffect(() => {
        const ws = new WebSocket("ws://localhost:8090/ws/started");

        ws.onopen = () => {
            console.log("WebSocket connection established");
        };

        ws.onmessage = (event) => {
            setLobbyState({...lobbyState, isStarted: true})
        };

        ws.onerror = (error) => {
            console.error("WebSocket error:", error);
        };

        ws.onclose = () => {
            console.log("WebSocket connection closed");
        };

        return () => {
            ws.close();
        };
    }, []);


    useEffect(() => {

        if (isError) {

        } else if (data) {

            if (data.state === "JOINABLE") {
                const url = `${LECTOR_PORTAL_API.BASE_API}/join/lobby/${sessionID}`
                // source.onerror = (event) => {
                //     console.log("errors")
                //     //navigate("/")
                // };
                // source.addEventListener("yourName", (e) => {
                //     const parsedData = e.data
                //     setLobbyState(prev => ({...prev, name: parsedData}))
                // })
                //
                // source.addEventListener("allNames", (e) => {
                //     const parsedData = JSON.parse(e.data)
                //     setLobbyState(prev => ({...prev, users: parsedData.players}))
                // })
                //
                // source.addEventListener("started", (e) => {
                //     setLobbyState(prev => ({...prev, isStarted: true}))
                //     source.close()
                // })
                // return () => {
                //     source.close()
                // }
            } else {
                redirect(`${GAME_SESSION_APP_PATHS.SESSION}/${sessionID}`);
            }
        }

        return () => {

        }
    }, [])

    useEffect(() => {
        //@ts-ignore
        let interval;
        const handleVisibilityChange = () => {
            if (document.visibilityState === "visible") {
                interval = setInterval(() => {
                    setLobbyState({...lobbyState, countdown: lobbyState.countdown - 1});
                }, 1000);
            } else {
                //@ts-ignore
                clearInterval(interval);
            }
        };

        if (lobbyState.isStarted) {
            document.addEventListener("visibilitychange", handleVisibilityChange);
            handleVisibilityChange();
        }

        if (lobbyState.countdown === 0) {
            setLobbyState({...lobbyState, isStarted: false, countdown: 5})
            redirect(`${GAME_SESSION_APP_PATHS.SESSION}/${sessionID}`);
        }

        return () => {
            //@ts-ignore
            clearInterval(interval);
            document.removeEventListener("visibilitychange", handleVisibilityChange);
        };
    }, [lobbyState.countdown, lobbyState.isStarted]);

    return (
        <>
            <Paper sx={{width: "50%", margin: "auto", padding: 2, marginY: 2}}>
                <Typography align="center" color={common.white} variant="h4"> Join at {window.location.host} with
                    GamePin: </Typography>
                <Typography align="center" color={common.white} variant="h2"> {lobbyID} </Typography>
            </Paper>
            <Divider/>
            <Stack direction="row" justifyContent="space-between">
                <Stack marginLeft={10} direction="column">
                    <Typography fontSize={"2.5rem"} fontWeight="bold"
                                align="center"> {lobbyState.users.length} </Typography>
                    <Typography fontSize={"1.5rem"} fontWeight="bold"> Players </Typography>
                </Stack>
                <Stack direction="row" alignItems="center">
                    <Typography fontSize={"1rem"} fontWeight="bold"> Waiting for players </Typography>
                    <CircularProgress size={30} thickness={5} sx={{margin: 2, marginRight: 10}}/>
                </Stack>
            </Stack>
            <Grid2
                container
                direction="row"
                alignItems="center"
                marginX="auto"
                width="70vw"
                columnSpacing={3}
                rowSpacing={3}
            >
                {lobbyState.users.map((playerName, index) => (

                    <Grid2 key={index} size={{xs: 4}} p={1}>
                        <UserCard playerName={playerName} isMainUsr={lobbyState.name === playerName}/>
                    </Grid2>

                ))
                }
            </Grid2>
            <Backdrop TransitionComponent={Grow} open={lobbyState.isStarted}>
                <Stack>
                    <Typography fontSize="8rem"> Starting in </Typography>
                    <Typography align="center" fontSize="10rem"> {lobbyState.countdown} </Typography>
                </Stack>
            </Backdrop>
        </>
    );
};

export default Lobby;