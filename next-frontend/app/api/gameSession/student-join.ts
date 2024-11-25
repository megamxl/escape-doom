import {gameSessionClient} from "@/app/api/axios";
import {LobbyJoinResponse} from "@/app/types/student-join/lobby-join-response";
import {LobbyStatusResponse} from "@/app/types/student-join/lobby-status-response";

const ENDPOINT = "/join"

export const postJoinLobby = async (roomPin: string): Promise<LobbyJoinResponse> => {
    const { data } = await gameSessionClient.get(`${ENDPOINT}/${roomPin}`);
    return data;
}

export const getLobbyStatus = async (roomPin: string): Promise<LobbyStatusResponse> => {
    const { data } = await gameSessionClient.get(`${ENDPOINT}/status/${roomPin}`)
    return data;
}