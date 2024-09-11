import { useQuery } from "@tanstack/react-query";
import { getLectureToken } from "../utils/TokenHandler";
import axios from "axios";

interface escapeRoomData {
    escaproom_id: number
    name: string
    topic: string
    time: number
    escapeRoomState: string
    userId: number
}

interface gameJoin {
    playerName: string
}

const sleep = (ms: number) => new Promise(r => setTimeout(r, ms));

export const useGet = (url: string, needToken = false, isEnabled = true) => {

    console.log(url)

    let headers = {}

    if (needToken) {
        //@ts-ignore
        headers['Authorization'] = "Bearer " + getLectureToken()
    }

    return useQuery({
        retry: 1,
        queryKey: ['basicUseGetHook'],
        queryFn: async () => {
            const { data } = await axios.get(url, { headers })
            return data as [escapeRoomData]
        },
        enabled: isEnabled,
        staleTime: Infinity
    })
};