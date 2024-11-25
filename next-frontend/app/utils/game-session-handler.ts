import {useState} from "react";
import {getSessionStorageItem, setSessionStorageItem} from "@/app/utils/session-storage-handler";

const sessionString = 'sessionId'

export const useSession = () => {
    const [session, setSession] = useState<string>(() => {
        return getSessionStorageItem(sessionString)
    })

    const setGameSession = (newSession: string) => {
        setSessionStorageItem(sessionString, newSession, setSession)
    }

    return [session, setGameSession] as const;
}