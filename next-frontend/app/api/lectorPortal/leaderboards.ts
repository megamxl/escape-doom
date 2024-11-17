import {gameSessionClient} from "@/app/api/axios";
import {PlayerProgression} from "@/app/types/leaderboard/playerProgression";

const ENDPOINT = "/leaderboard"

export const getLeaderboard = async (lobbyId: number): Promise<PlayerProgression[]> => {
    const { data } = await gameSessionClient.get(`${ENDPOINT}/${lobbyId}`);
    return sortLeaderboardData(data);
}

const sortLeaderboardData = (data: PlayerProgression[]) => {
    return data?.sort((a,b) => {
        // b - a for descending order
        if (a.score !== b.score) return b.score - a.score
        return b.time ?? 0 - a.time ?? a.time
    });
}