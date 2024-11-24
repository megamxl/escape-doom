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
import {useChangeRoomState} from "@/app/utils/api/lector-portal/useGetEscapeRooms";
import {RoomState} from "@/app/enums/RoomState";

type RoomCardCreationProps = {
    name: string,
    topic: string,
    imgUrl: string,
    time: number,
    id: number
    escapeRoomState: RoomState,
}

type RoomCardState = {
    Status: RoomState,
    ID: number,
    Time: number
}

const RoomCard = ({name, topic, imgUrl, time, id, escapeRoomState}: RoomCardCreationProps) => {

    const [roomInfo, setRoomInfo] = useState<RoomCardState>({
        Status: escapeRoomState,
        ID: id,
        Time: time
    })

    const [snackbarOpen, setSnackbarOpen] = useState(false)
    const handleClose = () => setSnackbarOpen(false)
    const changeRoomState = useChangeRoomState(roomInfo.Status, roomInfo.ID, roomInfo.Time)

    const statusLedColor = (() => {
        switch (roomInfo.Status) {
            case RoomState.STOPPED: return '#f00';
            case RoomState.PLAYING: return '#0f0';
            case RoomState.JOINABLE: return '#ff0'
            default: console.log("Something went wrong - Can't read RoomState:", roomInfo.Status)
        }
    })();

    const handleStateChange = async (newState: RoomState) => {
        const stateBeforeUpdate = roomInfo

        setRoomInfo({...roomInfo, Status: newState});
        changeRoomState.mutate()

        if (changeRoomState.isError) {
            setRoomInfo(stateBeforeUpdate);
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
                <Typography variant="h5" component="div">
                    {name}
                </Typography>
                <Typography sx={{fontSize: 14}} component="div">
                    {topic}
                </Typography>
                <div className={"flex flex-row gap-2"}>
                    {roomInfo.ID !== 0 ?
                        <Typography alignSelf={"baseline"} sx={{fontSize: 14}} component="div">
                            LobbyID: {roomInfo.ID}
                        </Typography>
                        : ''
                    }
                    |
                    {roomInfo.ID !== 0 ?
                        <Link target="_blank" rel="noopener" sx={{fontSize: 14}}
                              href={"/leaderboard/" + roomInfo.ID}>Leaderboard</Link>
                        : ''
                    }
                </div>

            </CardContent>
            <CardActions sx={{justifyContent: "space-between"}}>
                <Circle sx={{color: statusLedColor}}> </Circle>
                <Button onClick={() => handleStateChange(RoomState.JOINABLE)} startIcon={<OpenInBrowser/>}> Open </Button>
                <Button onClick={() => handleStateChange(RoomState.PLAYING)} startIcon={<PlayArrow/>}> Start </Button>
                <Button onClick={() => handleStateChange(RoomState.STOPPED)} startIcon={<Close/>}> Close </Button>
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
                <Alert onClose={handleClose} severity="error" sx={{width: '100%'}}>
                    There was a problem changing the Room state!
                </Alert>
            </Snackbar>
        </Card>
    );
};

export default RoomCard;