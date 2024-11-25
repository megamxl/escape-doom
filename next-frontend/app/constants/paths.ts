import assert from "assert";

assert(process.env.NEXT_PUBLIC_LECTOR_PORTAL_BASE_URL, "env variable not set: NEXT_PUBLIC_LECTOR_PORTAL_BASE_URL")
assert(process.env.NEXT_PUBLIC_GAME_SESSION_BASE_URL, "env variable not set: NEXT_PUBLIC_GAME_SESSION_BASE_URL")

export class LECTOR_PORTAL_API {
    public static BASE_API = process.env.NEXT_PUBLIC_LECTOR_PORTAL_BASE_URL
}

export class LECTOR_PORTAL_APP_PATHS {
    public static BASE_PATH = "/lector-portal"
    public static DASHBOARD = `${this.BASE_PATH}/dashboard`

}

export class GAME_SESSION_API {
    public static BASE_API = process.env.NEXT_PUBLIC_GAME_SESSION_BASE_URL
    public static SESSION = `${this.BASE_API}/session`
}

export class GAME_SESSION_APP_PATHS {
    public static BASE_PATH = "/game-session"
    public static STUDENT_JOIN = `${this.BASE_PATH}/student-join`
    public static SESSION = `${this.BASE_PATH}/session`
    public static LOBBY = `${this.BASE_PATH}/lobby`
    public static LEADERBOARD = `${this.BASE_PATH}/leaderboard`
}
