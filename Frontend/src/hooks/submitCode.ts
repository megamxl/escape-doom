import React from 'react';
import { useQuery } from "@tanstack/react-query";
import axios, { AxiosRequestConfig } from "axios";

export const submitCode = (url: string, body: object) => {



    return useQuery({
        queryKey: ['submitCode'],
        queryFn: async () => {
            const { data } = await axios.post(url, body)
            return data
        },
        enabled: false,
        retry: false,
        onSuccess(data) {
        },
        onError(err) {
        }
    })
};
