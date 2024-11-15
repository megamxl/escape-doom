import {useState} from "react";

const tokenString = 'token';

export const _getToken = () => {
    return sessionStorage.getItem(tokenString)
}

export const useToken = () => {
    const [token, setToken] = useState(_getToken || null)

    const _setToken = (newToken: string) => {
        sessionStorage.setItem(tokenString, newToken)
        setToken(newToken)
    }

    return [token, _setToken]
}