import {useQuery} from "@tanstack/react-query";
import {postJoinLobby} from "@/app/api/gameSession/student-join";

export const useLobbyJoin = (roomPin: string) => {
    return useQuery({
        queryKey: ["join-lobby"],
        queryFn: () => postJoinLobby(roomPin),
        enabled: false
    })
}