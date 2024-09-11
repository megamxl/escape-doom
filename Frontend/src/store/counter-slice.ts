import {createSlice, PayloadAction} from "@reduxjs/toolkit";

interface CounterState {
    value: number;
}

const initialState: CounterState = {
    value: 0
}

const counterSlice = createSlice({
    name: 'counter',
    initialState,
    reducers: {
        incremented(state) {
            state.value++
        },
        amountAdded(state, action: PayloadAction<number>) {
            state.value += action.payload
        },
        decrement(state) {
            state.value--
        },
        reset(state) {
            state.value = 0
        }
    }
})

export const { incremented, amountAdded } = counterSlice.actions
export default counterSlice.reducer