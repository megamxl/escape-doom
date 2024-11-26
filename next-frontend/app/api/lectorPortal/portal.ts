import {lectorClient} from "@/app/api/axios";
import {EscapeRoom} from "@/app/lector-portal/dashboard/types";
import {getToken} from "@/app/utils/token-handler";
import {AxiosRequestConfig} from "axios";
import {RoomState} from "@/app/enums/RoomState";

const ENDPOINT = "/portal-escape-room"

const headers: AxiosRequestConfig = {
    headers: { Authorization: `Bearer ${getToken()}`, }
}

const getAllRooms = async (): Promise<EscapeRoom[]> => {
    const {data} = await lectorClient.get(`${ENDPOINT}/getAll`, headers);
    return data;
}

const changeRoomState = async (state: RoomState, id: number, time?: number) => {
    switch (state) {
        case RoomState.PLAYING: return (await startRoom(id, time!));
        case RoomState.JOINABLE: return (await openRoom(id));
        case RoomState.STOPPED: return (await stopRoom(id));
        default: console.log("No valid state given", state); break;
    }
}

const openRoom = async (id: number) => {
    console.log("Headers:", headers)
    const {data} = await lectorClient.post(`${ENDPOINT}/openEscapeRoom/${id}`, null, headers)
    return data;
}

const startRoom = async (id: number, escapeRoomTime: number) => {
    const {data} = await lectorClient.post(`${ENDPOINT}/startEscapeRoom/${id}/${escapeRoomTime}`, null, headers)
    return data;
}

const stopRoom = async (id: number) => {
    const {data} = await lectorClient.post(`${ENDPOINT}/stopEscapeRoom/${id}`, null, headers)
    return data;
}

export {getAllRooms, changeRoomState}