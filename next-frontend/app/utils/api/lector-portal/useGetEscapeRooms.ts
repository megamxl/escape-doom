import {useMutation, useQuery} from "@tanstack/react-query";
import {changeRoomState, getAllRooms} from "@/app/api/lectorPortal/portal";
import {RoomState} from "@/app/enums/RoomState";

export const useGetEscapeRooms = () => {
    return useQuery({
            queryKey: ["lectorPortal"],
            queryFn: () => getAllRooms(),
            retry: false
        }
    );
}

export const useChangeRoomState = (state: RoomState, id: number, time?: number) => {
    return useMutation({
            mutationFn: () => changeRoomState(state, id, time)
        }
    );
}