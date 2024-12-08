import {SetStateAction} from "react";

export const getSessionStorageItem = (key: string) => {
    try {
        const item = sessionStorage.getItem(key);
        return item ?? "";
    } catch (error) {
        console.error("Error reading sessionStorage key:", error);
        return ""
    }
}

export const setSessionStorageItem = (key: string, value: string, fun: (action: SetStateAction<string>) => void) => {
    try {
        fun(value);
        sessionStorage.setItem(key, value)
    } catch (error) {
        console.error("Error setting sessionStorage key:", error);
    }
}

export const removeSessionStorageItem = (key: string) => {
    try {
        sessionStorage.removeItem(key)
    } catch (error) {
        console.error("Error removing sessionStorage key:", error);
    }
}