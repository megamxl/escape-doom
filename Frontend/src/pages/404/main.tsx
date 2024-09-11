import { Stack, Typography } from "@mui/material";

const NoRoute = () => {
    return (
        <>
            <Stack justifyContent="center" height="100vh" gap={10}>
                <Typography align="center" fontSize="6rem"> (✖╭╮✖) </Typography>
                <Typography align="center" fontSize="6rem"> Ooopsie no Page </Typography>
                <Typography align="center" fontSize="6rem"> (づ ◕‿◕ )づ </Typography>
            </Stack>
        </>
    );
}
 
export default NoRoute;