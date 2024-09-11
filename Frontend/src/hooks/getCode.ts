import { useQuery } from "@tanstack/react-query";
import axios from "axios";

export const getCode = (url: string) => {

    return useQuery({
        queryKey: ['getCode'],
        retry: 1,
        queryFn: async () => {
            const { data } = await axios.get(url)
            return data
        },
        enabled: false,
        staleTime: Infinity
    })
};