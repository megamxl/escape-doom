import { useQuery } from "@tanstack/react-query";
import { getLectureToken } from "../utils/TokenHandler";
import axios from "axios";

export const getStage = (sessionID: string) => {

    return useQuery({
        retry: 1,
        queryKey: ['getStageHook'],
        queryFn: async () => {
            const url = `${import.meta.env.VITE_GAME_BASE_URL}/join/getStage/${sessionID}`
            console.log("THE URL IS", url)
            console.log("THE URL IS", url)
            const { data } = await axios.get(url)
            return data
        },
        onSuccess(data) {
            console.log(data)
        },
        onError(err) {
            console.log(err)
        },
        enabled: true,
        staleTime: Infinity
    })
};