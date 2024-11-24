'use client'

import React, {ChangeEvent, FormEvent, useEffect, useState} from 'react';
import {Alert, Button, Card, CardContent, Grid2, Snackbar, Stack, TextField, Typography} from "@mui/material";
import BackgroundImage from '@/public/images/StudentJoin.jpg'
import { common } from '@mui/material/colors';
import {useLobbyJoin} from "@/app/utils/api/student-join/useLobbyJoin";
import {redirect} from "next/navigation";

const StudentJoin = () => {

    const [roomPin, setRoomPin] = useState('')
    const [snackbar, setSnackbar] = useState(false)
    const {refetch} = useLobbyJoin(roomPin)

    const handleUserInput = (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setRoomPin(e.target.value)
    }

    const sendID = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const {data, isError, error} = await refetch();
        if (!isError && data) {
            const sessionID = data.sessionId
            redirect("")
        }




    }

    return (
        <>
            <Grid2
                container
                direction="column"
                justifyContent="center"
                alignItems="center"
                height="100vh"
                sx={{
                    backgroundImage: `url(${BackgroundImage.src})`,
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
                                type="number"
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
            </Grid2>

            <Snackbar open={snackbar} autoHideDuration={6000} onClose={() => setSnackbar(false)}>
                <Alert onClose={() => setSnackbar(false)} severity="error" sx={{width: '100%'}}>
                    The given lobby is either closed or doesn't exist
                </Alert>
            </Snackbar>
        </>
    );
};

export default StudentJoin;