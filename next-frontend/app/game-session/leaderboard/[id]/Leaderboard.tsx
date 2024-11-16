'use client'

import React, {useEffect} from 'react';
import {useGetLeaderboard} from "@/app/utils/api/leaderboard/useGetLeaderboard";

const Leaderboard = ({boardID}: { boardID: number }) => {

    const {data, isError} = useGetLeaderboard(boardID)

    return (
        <div className={"flex flex-col gap-2 justify-around content-center"}>
            <div> {boardID} </div>
            {
                data ? data.slice(0, 10).map(player => {
                        return (
                            <>
                                <div className={"flex flex-row gap-2 justify-around"}>
                                    <h3> {player.playerName} </h3>
                                    <h3> {player.score} </h3>
                                    <h3> {player.time || "No time set yet"} </h3>
                                </div>
                            </>
                        )
                    })
                    : <h2> ...Loading </h2>
            }
        </div>
    );
};

export default Leaderboard;