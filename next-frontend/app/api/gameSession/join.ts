import {gameSessionClient} from "@/app/api/axios";
import {LobbyJoinResponse} from "@/app/types/student-join/lobby-join-response";
import {LobbyStatusResponse} from "@/app/types/student-join/lobby-status-response";
import {StageInfoResponse} from "@/app/types/game-session/stage-info-response";
import {SubmittedCodeBody} from "@/app/types/game-session/SubmittedCodeBody";

const ENDPOINT = "/join"

export const postJoinLobby = async (roomPin: string): Promise<LobbyJoinResponse> => {
    const { data } = await gameSessionClient.get(`${ENDPOINT}/${roomPin}`);
    return data;
}

export const getLobbyStatus = async (roomPin: string): Promise<LobbyStatusResponse> => {
    const { data } = await gameSessionClient.get(`${ENDPOINT}/status/${roomPin}`)
    return data;
}

export const getStageInformation = async (sessionID: string): Promise<StageInfoResponse> => {
    const { data } = await gameSessionClient.get(`${ENDPOINT}/getStage/${sessionID}`)
    return data;
}

export const postSubmitCode = async (codeBody: SubmittedCodeBody) => {

}