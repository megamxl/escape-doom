export const LECTOR_PORTAL_API_PATHS = {
    BASE_URL: 'http://localhost:8080/api/v1',
    LECTOR_PORTAL_BASE: function() { return `${this.BASE_URL}/portal-escape-room` },
    OPEN_ROOM: function() { return `${this.LECTOR_PORTAL_BASE()}/openEscapeRoom` },
    START_ROOM: function() { return `${this.LECTOR_PORTAL_BASE()}/startEscapeRoom` },
    STOP_ROOM: function() { return `${this.LECTOR_PORTAL_BASE()}/stopEscapeRoom` }
} as const;

/**
 *
 */
export const APP_PATHS = {
    STUDENT_JOIN: "/game-session/student-join",
    LECTOR_DASHBOARD: "/lector-portal/dashboard"
} as const;

export const BASE_URLS = {
    VITE_LECTOR_BASE_URL: "http://localhost:8080/api/v1",
    VITE_GAME_BASE_URL: "http://localhost:8090/api"
} as const;
