import {gameSessionClient} from "@/app/api/axios";
import {LobbyJoinResponse} from "@/app/types/student-join/lobby-join-response";

const ENDPOINT = "/join"

export const postJoinLobby = async (roomPin: string): Promise<LobbyJoinResponse> => {
    let { data } = await gameSessionClient.get(`${ENDPOINT}/${roomPin}`);
    return data;
}