import React, {useState} from 'react';
import BackgroundImage from '../../assets/live-escape-game-1155620.jpg'
import {Alert, Button, Card, CardContent, Grid, Snackbar, Stack, TextField, Typography} from "@mui/material";
import {common} from "@mui/material/colors";
import {useNavigate} from 'react-router-dom';
import {useGet} from '../../hooks/useGet';
import {getSessionId, removeSessionId, setSessionId} from '../../utils/GameSessionHandler';
import axios from "axios";


const StudentJoin = () => {

    const navigate = useNavigate()

    const [roomPin, setRoomPin] = useState('')
    const [snackbar, setSnackbar] = useState(false)

    const {refetch} = useGet(`${import.meta.env.VITE_GAME_BASE_URL}/join/${roomPin}`, false, false)

    const handleUserInput = (e: any) => {
        setRoomPin(e.target.value)
    }

    const sendID = async (e: React.FormEvent<HTMLFormElement>) => {
        document.cookie = "SESSION=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"
        document.cookie = "SESSION=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;domain=194.182.186.44;"
        e.preventDefault()
        if (!getSessionId()) {
            //const response = (await refetch());

            axios.get(`${import.meta.env.VITE_GAME_BASE_URL}/join/${roomPin}`)
                .then((response) => {
                    console.log("axios resp", response)
                    const responseData = response.data;

                    const sessionId = getSessionId();
                    //@ts-ignore
                    switch (responseData.state) {
                        case "PLAYING" :
                            console.log("in playing")
                            //@ts-ignore
                            setSessionId(responseData.sessionId);
                            //@ts-ignore
                            navigate(`/session/${responseData.sessionId}`);
                            break;
                        case "STOPPED":
                            removeSessionId()
                            setSnackbar(true)
                            break
                        case "JOINABLE":
                            navigate(`/lobby/${roomPin}`)
                            //@ts-ignore
                            setSessionId(responseData.sessionId)
                            break
                    }


                })
        } else {
            const sessionId = getSessionId()
            navigate(`/session/${sessionId}`);
        }

    }

    return (
        <>
            <Grid
                container
                direction="column"
                justifyContent="center"
                alignItems="center"
                height="100vh"
                sx={{
                    backgroundImage: `url(${BackgroundImage})`,
                    backgroundColor: '#404040',
                    backgroundBlendMode: 'multiply',
                    backgroundSize: 'cover',
                }}
            >
                <Typography sx={{paddingBottom: 3}} color={common.white} variant="h2"> Escape Room </Typography>
                <Card sx={{minWidth: 550, paddingX: 3}}>
                    <CardContent>
                        <Stack spacing={2} alignItems="center" component="form" onSubmit={sendID} noValidate>
                            <TextField
                                id="outlined-basic"
                                label="Room-PIN"
                                variant="outlined"
                                value={roomPin}
                                onChange={handleUserInput}
                                fullWidth
                            />
                            <Button sx={{height: 56}} variant="contained" type="submit" fullWidth>JOIN</Button>
                        </Stack>
                    </CardContent>
                </Card>
            </Grid>

            <Snackbar open={snackbar} autoHideDuration={6000} onClose={() => setSnackbar(false)}>
                <Alert onClose={() => setSnackbar(false)} severity="error" sx={{width: '100%'}}>
                    The given lobby is either closed or doesn't exist
                </Alert>
            </Snackbar>
        </>
    );
};

export default StudentJoin;