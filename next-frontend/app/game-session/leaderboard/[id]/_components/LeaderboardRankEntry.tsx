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
                <p className={"text-2xl"}> {playerName} </p>
            </div>
            <div className={"flex flex-row gap-3"}>
                <p className={"text-1xl"}> {score} </p>
                <p className={"text-1xl"}> {time || "No time set yet"} </p>
            </div>
        </div>
    )
}

export default LeaderboardRankEntry;