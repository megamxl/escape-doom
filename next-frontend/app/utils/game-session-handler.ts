const sessionString = 'sessionId'

//TODO: Change back to localeStorage when deploying
export const getSessionId = (): string => {
    return sessionStorage?.getItem(sessionString) || ""
}

export const setSessionId = (sessionId: string) => {
    return sessionStorage.setItem(sessionString, sessionId)
}

export const removeSessionId = () => {
    sessionStorage.removeItem(sessionString)
};