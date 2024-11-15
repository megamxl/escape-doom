import {lectorClient} from "@/app/api/axios";
import {EscapeRoom} from "@/app/lector-portal/dashboard/types";
import {RoomState} from "@/app/lector-portal/dashboard/_components/RoomCard";
import {_getToken} from "@/app/utils/token-handler";
import {AxiosRequestConfig} from "axios";

const ENDPOINT = "/portal-escape-room"

const headers: AxiosRequestConfig = {
    headers: { Authorization: `Bearer ${_getToken()}` }
}

const getAllRooms = async (): Promise<EscapeRoom[]> => {
    const {data} = await lectorClient.get(`${ENDPOINT}/getAll`, headers);
    return data;
}

const changeRoomState = async (state: RoomState, id: number, time?: number) => {
    switch (state) {
        case RoomState.PLAYING: await startRoom(id, time!); break;
        case RoomState.JOINABLE: await openRoom(id); break;
        case RoomState.STOPPED: await stopRoom(id); break;
    }
}

const openRoom = async (id: number) => {
    const {data} = await lectorClient.get(`${ENDPOINT}/openEscapeRoom/${id}`, headers)
    return data;
}

const startRoom = async (id: number, escapeRoomTime: number) => {
    const {data} = await lectorClient.get(`${ENDPOINT}/startEscapeRoom/${id}/${escapeRoomTime}`, headers)
    return data;
}

const stopRoom = async (id: number) => {
    const {data} = await lectorClient.get(`${ENDPOINT}/stopEscapeRoom/${id}`, headers)
    return data;
}

export {getAllRooms, changeRoomState}