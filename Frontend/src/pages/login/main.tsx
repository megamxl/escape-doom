import React, {useState} from 'react';
import {Alert, Avatar, Button, Container, Snackbar, TextField, Typography} from "@mui/material";
import {LockOutlined} from "@mui/icons-material";
import Box from "@mui/material/Box";
import {useNavigate} from "react-router-dom";
import useToken from "../../utils/TokenHandler";
import { usePost } from '../../hooks/usePost';


const Login = () => {
    const navigate = useNavigate()
    const [open, setOpen] = useState(false)
    const [token, setToken] = useToken()
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")

    const handleClose = () => setOpen(false)

    const onChangePassword = (event: any) => {
        setPassword(event.target.value)
    }

    const onChangeEmail = (event: any) => {
        setEmail(event.target.value)
    }

    const {refetch} = usePost(`${import.meta.env.VITE_LECTOR_BASE_URL}/auth/authenticate`, email, password)

    async function login (e: React.FormEvent<HTMLFormElement>) {
        e.preventDefault()
        const refetchResponse = (await refetch())
        if (!refetchResponse.isError) {
            //@ts-ignore
            setToken(refetchResponse.data?.token)
            navigate("/LectureConsole")
        } else {
            setOpen(true)
        }
    }

    return (
        <div>
            <Container maxWidth="xs">
                <Box
                    sx={{
                        mt: 8,
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center'
                    }}
                >
                    <Avatar sx={{m: 1}}>
                        <LockOutlined/>
                    </Avatar>
                    <Typography component="h1" variant="h5">
                        Log In!
                    </Typography>
                    <Box component="form" onSubmit={login} noValidate mt={1}>
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            id="email"
                            label="Email Address"
                            name="email"
                            onChange={onChangeEmail}
                            autoComplete="email"
                            autoFocus
                        />
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            name="password"
                            label="Password"
                            type="password"
                            id="password"
                            onChange={onChangePassword}
                            autoComplete="current-password"
                        />
                        <Button
                            type="submit"
                            fullWidth
                            variant={"contained"}
                            sx={{mt: 3, mb: 2}}
                        >
                            Log In
                        </Button>
                    </Box>
                </Box>
            </Container>
            <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
                <Alert onClose={handleClose} severity="error" sx={{width: '100%'}}>
                    Logging in didn't work! Make sure you use the correct credentials
                </Alert>
            </Snackbar>

        </div>
    );
};

export default Login;