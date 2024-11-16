import {gameSessionClient} from "@/app/api/axios";
import {PlayerProgression} from "@/app/types/leaderboard/playerProgression";

const ENDPOINT = "/leaderboard"

export const getLeaderboard = async (lobbyId: number): Promise<PlayerProgression[]> => {
    const { data } = await gameSessionClient.get(`${ENDPOINT}/${lobbyId}`);
    return data;
}