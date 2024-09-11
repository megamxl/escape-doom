import { configureStore } from "@reduxjs/toolkit";

import counterReducer from "./counter-slice";

export const store = configureStore({
    // This automatically calls combineReducers, so I can skip doing that myself
    reducer: {
        counter: counterReducer
    }
})

export type AppDispatch = typeof store.dispatch
export type RootState = ReturnType<typeof store.getState>