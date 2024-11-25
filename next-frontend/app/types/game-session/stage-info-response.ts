import {RoomState} from "@/app/enums/RoomState";

export type StageInfoResponse = {
    stage: string,
    state: RoomState,
    roomID: number
}