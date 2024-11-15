import {useQuery} from "@tanstack/react-query";
import {changeRoomState, getAllRooms} from "@/app/api/lectorPortal/portal";
import {RoomState} from "@/app/lector-portal/dashboard/_components/RoomCard";

const useGetEscapeRooms = () => {
    return useQuery({
            queryKey: ["lectorPortal"],
            queryFn: () => getAllRooms()
        }
    );
}

const useChangeRoomState = (state: RoomState, id: number, time?: number) => {
    return useQuery({
            queryKey: ["lectorPortal", id],
            queryFn: () => changeRoomState(state, id, time)
        }
    );
}

export {useGetEscapeRooms, useChangeRoomState}