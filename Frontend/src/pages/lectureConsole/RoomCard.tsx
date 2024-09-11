import React, {useState} from 'react';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import {AccessTime, Circle, Close, OpenInBrowser, PlayArrow, Share} from "@mui/icons-material";
import {Alert, FormControl, Link, MenuItem, Select, Snackbar, Stack} from "@mui/material";
import { usePost } from '../../hooks/usePost';

interface Props {
    name: String,
    topic: String,
    imgUrl: String,
    time: number,
    escapeRoomState: string,
    id: number
}

const stateColor = (escapeRoomState: string) => {
    switch (escapeRoomState) {
        case 'STOPPED':
            return '#ff0000'
        case 'JOINABLE':
            return '#ffff00'
        case 'PLAYING':
            return '#00ff00'
    }
}

const RoomCard = ({name, topic, imgUrl, time, escapeRoomState, id}: Props) => {

    const [status, setStatus] = useState(escapeRoomState)
    const [open, setOpen] = useState(false)
    const [lobbyID, setLobbyID] = useState(0)
    const [escapeRoomTime, setEscapeRoomTime] = useState(time)
    
    const handleClose = () => setOpen(false)

    const openEscapeRoomCall = usePost(`${import.meta.env.VITE_LECTOR_BASE_URL}/portal-escape-room/openEscapeRoom/${id}`)
    const startEscapeRoomCall = usePost(`${import.meta.env.VITE_LECTOR_BASE_URL}/portal-escape-room/startEscapeRoom/${id}/${escapeRoomTime}`)
    const stopEscapeRoomCall = usePost(`${import.meta.env.VITE_LECTOR_BASE_URL}/portal-escape-room/stopEscapeRoom/${id}`)

    //TODO: Make room calls into reusable function
    const openRoom = async () => {
        const refetchResponse = (await openEscapeRoomCall.refetch())
        if (!refetchResponse.isError) {
            setLobbyID(refetchResponse.data)
            setStatus('JOINABLE')
        } else {
            setOpen(true)
        }
    }

    const startRoom = async () => {
        const refetchResponse = (await startEscapeRoomCall.refetch())
        if (!refetchResponse.isError) {
            setLobbyID(lobbyID)
            setStatus('PLAYING')
        } else {
            setOpen(true)
        }
    }

    const stopRoom = async () => {
        const refetchResponse = (await stopEscapeRoomCall.refetch())
        if (!refetchResponse.isError) {
            setLobbyID(0)
            setStatus('STOPPED')
        } else {
            setOpen(true)
        }
    }

    return (
        <Card>
            <CardMedia
                sx={{height: {xs: 200, lg: 300}}}
                // @ts-ignore - Needed since I get an error even tho the image gets loaded ... ?_?
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
                {lobbyID !== 0 ?
                    <Typography gutterBottom sx={{fontSize: 14}} component="div">
                        LobbyID: {lobbyID}
                    </Typography>
                    : ''
                }
                {lobbyID !== 0 ?
                    <Link target="_blank" rel="noopener" sx={{fontSize: 14}} href={"/leaderboard/" + lobbyID }>Leaderboard</Link>
                    : ''
                }
                
            </CardContent>
            <CardActions sx={{justifyContent: "space-between"}}>
                <Circle sx={{color: stateColor(status)}}> </Circle>
                <Button onClick={openRoom} startIcon={<OpenInBrowser/>}> Open </Button>
                <Button onClick={startRoom} startIcon={<PlayArrow/>}> Start </Button>
                <Button onClick={stopRoom} startIcon={<Close/>}> Close </Button>
                <Stack direction="row" alignItems={"center"} gap={.5}>
                    <AccessTime/>
                    <FormControl>
                        <Select
                            value={escapeRoomTime}
                            variant="standard"
                            onChange={(event) => {setEscapeRoomTime(event.target.value as number)}}
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

            <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
                <Alert onClose={handleClose} severity="info" sx={{width: '100%'}}>
                    Moving from {status} to the desired state not possible
                </Alert>
            </Snackbar>
        </Card>
    );
};

export default RoomCard;