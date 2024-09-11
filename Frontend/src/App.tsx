import React, {useState} from 'react'

import LectureConsole from "./pages/lectureConsole/main"
import {Route, Routes, RedirectFunction, Navigate, useParams} from "react-router-dom";
import StudentJoin from "./pages/studenJoin/main";
import {createTheme, CssBaseline, ThemeProvider} from "@mui/material";
import Login from "./pages/login/main";
import EscapeView from "./pages/escapeView/main";
import GameLobby from './pages/gameLobby/main';
import NoRoute from './pages/404/main';
import EscapeLeaderboard from './pages/leaderboard/main';

function App() {

    const darkTheme = createTheme({
        palette: {
            mode: "dark"
        }
    })

    return (
        <div className="App">
            <ThemeProvider theme={darkTheme}>
                <CssBaseline/>
                <Routes>
                    <Route path="/" element={<StudentJoin/>}/>
                    <Route path="/login" element={<Login/>}/>
                    <Route path="/LectureConsole" element={<LectureConsole/>}/>
                    <Route path="/lobby/:lobbyID" element={<GameLobby/>}/>
                    <Route path="/session/:lobbyID" element={<EscapeView />}/>
                    <Route path="/leaderboard/:lobbyID" element={<EscapeLeaderboard />}/>
                    <Route path="*" element={<NoRoute />}/>
                </Routes>
            </ThemeProvider>
        </div>
    )
}

export default App
