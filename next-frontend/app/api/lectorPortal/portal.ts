import {lectorClient} from "@/app/api/axios";
import {EscapeRoom} from "@/app/lector-portal/dashboard/types";
import {RoomState} from "@/app/lector-portal/dashboard/_components/RoomCard";

const ENDPOINT = "/portal-escape-room"

const getAllRooms = async (): Promise<EscapeRoom[]> => {
    const { data } = await lectorClient.get(`${ENDPOINT}/getAll`,
        {headers: { Authorization: "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsZW9uQGRvb20uYXQiLCJpYXQiOjE3MzE2ODM0NjUsImV4cCI6MTczMTY4NDkwNX0.R4awhYoB6lxYTTwM3o-u18u4Jlgw57TgVVkVz6Xwm1s" }});
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
    const { data } = await lectorClient.get(`${ENDPOINT}/openEscapeRoom/${id}`)
    return data;
}

const startRoom = async (id: number, escapeRoomTime: number) => {
    const { data } = await lectorClient.get(`${ENDPOINT}/startEscapeRoom/${id}/${escapeRoomTime}`)
    return data;
}

const stopRoom = async (id: number) => {
    const { data } = await lectorClient.get(`${ENDPOINT}/stopEscapeRoom/${id}`)
    return data;
}

export {getAllRooms, changeRoomState}