'use client'

import React, {useEffect} from 'react';
import {useGetLeaderboard} from "@/app/utils/api/leaderboard/useGetLeaderboard";
import TopThree from "@/app/game-session/leaderboard/[id]/_components/TopThree";
import LeaderboardRankEntry from "@/app/game-session/leaderboard/[id]/_components/LeaderboardRankEntry";

const Leaderboard = ({boardID}: { boardID: number }) => {

    const {data, isError, isFetching} = useGetLeaderboard(boardID)

    useEffect(() => {
    }, []);

    return (
        <div className={"flex flex-col gap-2 justify-around content-center"}>
            { isFetching && <p> Refetching... </p>
            }
            <div> {boardID} </div>
            <TopThree />
            {
                data ? data.slice(0).map((player, idx) => {
                        return (
                            <LeaderboardRankEntry rankingInfo={player} index={idx} key={idx}  />
                        )
                    })
                    : <h2> ...Loading </h2>
            }
        </div>
    );
};

export default Leaderboard;