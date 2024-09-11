import React, {useEffect, useState} from 'react';
import TopAppBar from "./TopAppBar";
import RoomCard from "./RoomCard";
import {Alert, Button, Divider, Grid, Skeleton, Snackbar, Stack, Typography} from "@mui/material";
import Box from "@mui/material/Box"
import BackgroundImage1 from '../../assets/live-escape-game-1155620.jpg'
import BackgroundImage2 from '../../assets/ankhesenamun-KitGM-GDgOI-unsplash.jpg'
import {useNavigate} from "react-router-dom";
import {useGet} from "../../hooks/useGet";
import RoomCardSkeleton from "./RoomCardSkeleton";
//rsc

const backgroundImages = [BackgroundImage1, BackgroundImage2]
const LectureConsole = () => {

    const navigate = useNavigate()
    const {data, isLoading, isError, error} = useGet(`${import.meta.env.VITE_LECTOR_BASE_URL}/portal-escape-room/getAll`, true)

    useEffect(() => {
        //@ts-ignore
        if (error?.response.status === 403 && !isLoading) {
            navigate("/login")
        }
    }, [isError])

    return (
        <div>
            <TopAppBar/>
            <Box width="70vw" margin="auto" mt={6}>
                <Stack gap={3}>
                    <Stack direction="row" alignItems="center">
                        <Typography fontSize="16" fontWeight="bold" mr={2}> Your Escape Rooms </Typography>
                        <Divider sx={{flexGrow: 1, borderBottomWidth: 3}} orientation="horizontal"/>
                    </Stack>
                    <Grid container spacing={{md: 6, lg: 12}}>
                        {data ? data.map(({name, topic, time, escapeRoomState, escaproom_id}, index) => (
                            <Grid key={index} item lg={6} sm={12}>
                                <RoomCard name={name} topic={topic} imgUrl={backgroundImages[index % 2]}
                                          time={time} escapeRoomState={escapeRoomState} id={escaproom_id}
                                />
                            </Grid>
                        )) : [...Array(4)].map((_, index) => (
                            <Grid key={index} item lg={6} sm={12}>
                                <RoomCardSkeleton key={index} />
                            </Grid>
                        ))
                        }
                    </Grid>
                </Stack>
            </Box>
        </div>
    );
};

export default LectureConsole;