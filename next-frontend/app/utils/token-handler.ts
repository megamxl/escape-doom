import {useState} from "react";

const tokenString = 'token';

//TODO: Change back to localeStorage when deploying
export const getToken = () => {
    if (typeof window !== "undefined") {
        return sessionStorage.getItem(tokenString)
    }
}

export const useToken = () => {
    const [token, setToken] = useState(getToken || null)

    const _setToken = (newToken: string) => {
        sessionStorage.setItem(tokenString, newToken)
        setToken(newToken)
    }

    return [token, _setToken]
}