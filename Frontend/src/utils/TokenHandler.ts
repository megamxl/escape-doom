import { SetStateAction, useState } from "react";

const tokenString = 'token'

export const getLectureToken = () => {
    return sessionStorage.getItem(tokenString)
}

export default function useToken() {
    const getToken = () => {
        return sessionStorage.getItem(tokenString)
    }

    const [token, setToken] = useState(getToken())

    // @ts-ignore
    function setLectureToken(lectureToken: string): void {
        sessionStorage.setItem(tokenString, lectureToken)
        setToken(lectureToken)
    }

    return [token, setLectureToken]
}