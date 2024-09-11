import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App'
import {store} from './store/store'
import {BrowserRouter} from "react-router-dom";
import {Provider} from "react-redux";
import './index.css'
import {QueryClient, QueryClientProvider} from "@tanstack/react-query";

const queryClient = new QueryClient()

ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
    <React.StrictMode>
        <Provider store={store}>
            <BrowserRouter>
                <QueryClientProvider client={queryClient}>
                    <App/>
                </QueryClientProvider>
            </BrowserRouter>
        </Provider>
    </React.StrictMode>,
)
