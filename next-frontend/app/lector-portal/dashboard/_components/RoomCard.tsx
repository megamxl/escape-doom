'use client'

import React, {useState} from 'react';
import {
    Alert,
    Button,
    Card,
    CardActions,
    CardContent,
    CardMedia,
    FormControl,
    Link,
    MenuItem,
    Select,
    Snackbar,
    Stack,
    Typography
} from "@mui/material";
import {AccessTime, Circle, Close, OpenInBrowser, PlayArrow} from "@mui/icons-material";
import axios from "axios";
import {LECTOR_PORTAL_API_PATHS} from "@/app/constants/paths";
import {useChangeRoomState} from "@/app/utils/api/lector-portal/useGetEscapeRooms";

type RoomCardCreationProps = {
    name: string,
    topic: string,
    imgUrl: string,
    time: number,
    escapeRoomState: RoomState,
}

type RoomCardState = {
    Status: RoomState,
    ID: number,
    Time: number
}

export enum RoomState {
    IDLE = '#999',
    STOPPED = '#ff0000',
    JOINABLE = '#ffff00',
    PLAYING = '#00ff00'
}

const RoomCard = ({name, topic, imgUrl, time, escapeRoomState}: RoomCardCreationProps) => {

    const [roomInfo, setRoomInfo] = useState<RoomCardState>({
        Status: RoomState.IDLE,
        ID: 0,
        Time: time
    })

    const [snackbarOpen, setSnackbarOpen] = useState(false)
    const handleClose = () => setSnackbarOpen(false)

    const changeRoomState = async (newState: RoomState) => {
        let callURL: string;

        switch (newState) {
            case RoomState.JOINABLE:
                callURL = `${LECTOR_PORTAL_API_PATHS.OPEN_ROOM()}/${roomInfo.ID}`;
                break;
            case RoomState.PLAYING:
                callURL = `${LECTOR_PORTAL_API_PATHS.START_ROOM()}/${roomInfo.ID}`;
                break;
            case RoomState.STOPPED:
                callURL = `${LECTOR_PORTAL_API_PATHS.STOP_ROOM()}/${roomInfo.ID}`;
                break;
            default:
                callURL = ``
                console.error("The given RoomState does not exist!")
        }
        //TODO: Make this with TanStack Query call
        const response = await axios.get(callURL)
        if (response.status != 200) {
            setSnackbarOpen(true)
        }
    }

    return (
        <Card>
            <CardMedia
                sx={{height: {xs: 200, lg: 300}}}
                image={imgUrl}
                title="Escape Room Picture"
            />
            <CardContent>
                <Typography gutterBottom variant="h5" component="div">
                    {name}
                </Typography>
                <Typography gutterBottom sx={{fontSize: 14}} component="div">
                    {topic}
                </Typography>
                {roomInfo.ID !== 0 ?
                    <Typography gutterBottom sx={{fontSize: 14}} component="div">
                        LobbyID: {roomInfo.ID}
                    </Typography>
                    : ''
                }
                {roomInfo.ID !== 0 ?
                    <Link target="_blank" rel="noopener" sx={{fontSize: 14}}
                          href={"/leaderboard/" + roomInfo.ID}>Leaderboard</Link>
                    : ''
                }

            </CardContent>
            <CardActions sx={{justifyContent: "space-between"}}>
                <Circle sx={{color: roomInfo.Status}}> </Circle>
                <Button onClick={() => changeRoomState(RoomState.JOINABLE)} startIcon={<OpenInBrowser/>}> Open </Button>
                <Button onClick={() => changeRoomState(RoomState.PLAYING)} startIcon={<PlayArrow/>}> Start </Button>
                <Button onClick={() => changeRoomState(RoomState.STOPPED)} startIcon={<Close/>}> Close </Button>
                <Stack direction="row" alignItems={"center"} gap={.5}>
                    <AccessTime/>
                    <FormControl>
                        <Select
                            value={roomInfo.Time}
                            variant="standard"
                            onChange={(event) => {
                                setRoomInfo({...roomInfo, Time: event.target.value as number})
                            }}
                        >
                            <MenuItem value={30}>30 min</MenuItem>
                            <MenuItem value={60}>1h</MenuItem>
                            <MenuItem value={90}>1h 30min</MenuItem>
                            <MenuItem value={120}>2h</MenuItem>
                        </Select>
                    </FormControl>
                </Stack>
            </CardActions>

            {/* Snackbars */}

            <Snackbar open={snackbarOpen} autoHideDuration={6000} onClose={handleClose}>
                <Alert onClose={handleClose} severity="info" sx={{width: '100%'}}>
                    Moving from {roomInfo.Status} to the desired state not possible
                </Alert>
            </Snackbar>
        </Card>
    );
};

export default RoomCard;