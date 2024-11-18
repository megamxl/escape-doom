export const formatTime = (time: number) => {
    return new Date(time).toISOString().substring(11, 19);
}