'use client'

import React from 'react';
import {Box, Divider, Grid2, Stack, Typography} from "@mui/material";
import BackgroundImage from '@/public/images/StudentJoin.jpg'
import RoomCard, {RoomState} from "@/app/lector-portal/dashboard/_components/RoomCard";

const LectorPortalDashboard = () => {
    return (
        <>
            <Box width="70vw" margin="auto" mt={6}>
                <Stack gap={3}>
                    <Stack direction="row" alignItems="center">
                        <Typography fontSize="16" fontWeight="bold" mr={2}> Your Escape Rooms </Typography>
                        <Divider sx={{flexGrow: 1, borderBottomWidth: 3}} orientation="horizontal"/>
                    </Stack>
                    <Grid2 container spacing={{md: 6, lg: 12}}>
                        {
                            [...Array(4)].map((_, index) => (
                                <Grid2 key={index} size={{lg: 6, sm: 12}}>
                                    <RoomCard name={"Test"} topic={"Test"} imgUrl={BackgroundImage.src}
                                              time={90} escapeRoomState={RoomState.STOPPED}/>
                                </Grid2>
                            ))
                        }
                        {/*TODO: Put this back after loading data from backend*/}
                        {/*{data ? data.map(({name, topic, time, escapeRoomState, escaproom_id}, index) => (*/}
                        {/*    <Grid2 key={index} size={{lg: 6, sm: 12}}>*/}
                        {/*        <RoomCard name={name} topic={topic} imgUrl={BackgroundImage.src}*/}
                        {/*                  time={time} escapeRoomState={escapeRoomState}*/}
                        {/*        />*/}
                        {/*    </Grid2>*/}
                        {/*)) : */}
                        {/*    [...Array(4)].map((_, index) => (*/}
                        {/*    <Grid2 key={index} size={{lg: 6, sm: 12}}>*/}
                        {/*        <RoomCardSkeleton key={index}/>*/}
                        {/*    </Grid2>*/}
                        {/*))*/}
                        {/*}*/}
                    </Grid2>
                </Stack>
            </Box>
        </>
    );
};

export default LectorPortalDashboard;