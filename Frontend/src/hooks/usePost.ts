import React from 'react';
import { useQuery } from "@tanstack/react-query";
import axios, { AxiosRequestConfig } from "axios";
import { getLectureToken } from '../utils/TokenHandler';

export const usePost = (url: string, usrEmail?: string, usrPassword?: string) => {

    console.log(url)

    let body = {}
    let headers = {}

    if (usrEmail && usrPassword) {
        body = {
            "email": usrEmail,
            "password": usrPassword
        }
    } else {
        //@ts-ignore
        headers['Authorization'] = "Bearer " + getLectureToken()
    }

    return useQuery({
        queryKey: ['postData'],
        queryFn: async () => {
            const { data } = await axios.post(url, body, { headers })
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
