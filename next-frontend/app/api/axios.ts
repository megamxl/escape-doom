import Axios from "axios"
import assert from "assert";

// Asserts to not run if .env Variables are not prominent
assert(process.env.NEXT_PUBLIC_LECTOR_PORTAL_BASE_URL, "env variable not set: NEXT_PUBLIC_LECTOR_PORTAL_BASE_URL")
assert(process.env.NEXT_PUBLIC_GAME_SESSION_BASE_URL, "env variable not set: NEXT_PUBLIC_GAME_SESSION_BASE_URL")

// Exports two different Axios clients due to two different base URLs and use-cases
export const lectorClient = Axios.create({
    baseURL: process.env.NEXT_PUBLIC_LECTOR_PORTAL_BASE_URL
})

export const gameSessionClient = Axios.create({
    baseURL: process.env.NEXT_PUBLIC_GAME_SESSION_BASE_URL
})