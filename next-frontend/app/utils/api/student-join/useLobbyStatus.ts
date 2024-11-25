import {useQuery} from "@tanstack/react-query";
import {getLobbyStatus, postJoinLobby} from "@/app/api/gameSession/student-join";

export const useLobbyStatus = (roomPin: string) => {
    return useQuery({
        queryKey: ["lobby-status", roomPin],
        queryFn: () => getLobbyStatus(roomPin),
    })
}