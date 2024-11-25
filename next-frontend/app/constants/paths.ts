export class LECTOR_PORTAL_API {
    public static BASE_API = "http://localhost:8080/api/v1"
}

export class LECTOR_PORTAL_APP_PATHS {
    public static BASE_PATH = "/lector-portal"
    public static DASHBOARD = `${this.BASE_PATH}/dashboard`

}

export class GAME_SESSION_API {
    public static BASE_API = "http://localhost:8090/api"
    public static SESSION = `${this.BASE_API}/session`
}

export class GAME_SESSION_APP_PATHS {
    public static BASE_PATH = "/game-session"
    public static STUDENT_JOIN = `${this.BASE_PATH}/student-join`
    public static SESSION = `${this.BASE_PATH}/session`
    public static LOBBY = `${this.BASE_PATH}/lobby`
    public static LEADERBOARD = `${this.BASE_PATH}/leaderboard`
}

export const BASE_URLS = {
    VITE_LECTOR_BASE_URL: "http://localhost:8080/api/v1",
    VITE_GAME_BASE_URL: "http://localhost:8090/api"
} as const;
