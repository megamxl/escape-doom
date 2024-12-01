import {useQuery} from "@tanstack/react-query";
import {getCodeResult} from "@/app/api/gameSession/join";

export const useGetCodeResult = (sessionID: string) => {
    return useQuery({
        queryKey: ['stage-info', sessionID],
        queryFn: () => getCodeResult(sessionID)
    })
}