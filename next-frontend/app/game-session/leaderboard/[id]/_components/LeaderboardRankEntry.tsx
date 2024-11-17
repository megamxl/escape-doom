import React, {HTMLAttributes} from 'react';
import {PlayerProgression} from "@/app/types/leaderboard/playerProgression";

type RankEntryProps = {
    rankingInfo: PlayerProgression,
    index: number
}

const LeaderboardRankEntry = ({rankingInfo, index}: RankEntryProps) => {
    const {playerName, score, time} = rankingInfo

    console.log("Trying to create new player card")

    return (
        <div className={"flex flex-row gap-2 justify-between px-8"}>
            <div className={"flex flex-row gap-3"}>
                <p className={"text-2xl"}> #{index + 1} </p>
                <p> {playerName} </p>
            </div>
            <div>
                <h2> {score} </h2>
                <h2> {time || "No time set yet"} </h2>
            </div>
        </div>
    )
}

export default LeaderboardRankEntry;