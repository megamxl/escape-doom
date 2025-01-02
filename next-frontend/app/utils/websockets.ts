type websocketParams = {
    url: string,
    method: () => void
}

const createWebSocket = (url: string, method: (event?: MessageEvent) => any) => {
    const ws = new WebSocket(url);

    ws.onopen = () => {
        console.log("WebSocket connection established");
    };

    ws.onmessage = method

    ws.onerror = (error) => {
        console.error("WebSocket error:", error);
    };

    ws.onclose = () => {
        console.log("WebSocket connection closed");
    };

    const close = () => {
        ws.close();
    }

    return { ws, close }
}

// const [yourNameSocket, setYourNameSocket] = useState<WebSocket | null>(null);
// const [yourNamemessages, setYourNameMessages] = useState<string[]>([]);
// useEffect(() => {
//     const ws = new WebSocket("ws://localhost:8090/ws/your-name");
//
//     ws.onopen = () => {
//         console.log("WebSocket connection established");
//     };
//
//     ws.onmessage = (event) => {
//         setYourNameMessages((prevMessages) => [...prevMessages,  event.data]);
//         setName(event.data);
//     };
//
//     ws.onerror = (error) => {
//         console.error("WebSocket error:", error);
//     };
//
//     ws.onclose = () => {
//         console.log("WebSocket connection closed");
//     };
//
//     setYourNameSocket(ws);
//
//     return () => {
//         ws.close();
//     };
// }, []);
//
// const [allNamesSocket, setAllNamesSocket] = useState<WebSocket | null>(null);
// const [allNamesMessages, setAllNamesMessages] = useState<string[]>([]);
//
// useEffect(() => {
//     const ws = new WebSocket("ws://localhost:8090/ws/all-names");
//
//     ws.onopen = () => {
//         console.log("WebSocket connection established");
//     };
//
//     ws.onmessage = (event) => {
//         try {
//             const data = JSON.parse(event.data);
//             setUsers(data.players || []);
//         } catch (error) {
//             console.error("Error parsing WebSocket message:", error);
//         }
//     };
//
//     ws.onerror = (error) => {
//         console.error("WebSocket error:", error);
//     };
//
//     ws.onclose = () => {
//         console.log("WebSocket connection closed");
//     };
//
//     setAllNamesSocket(ws);
//
//     return () => {
//         ws.close();
//     };
// }, []);
//
// const [startedSocket, setStartedSocket] = useState<WebSocket | null>(null);
// const [startedMessages, setStartedMessages] = useState<string[]>([]);
//
// useEffect(() => {
//     const ws = new WebSocket("ws://localhost:8090/ws/started");
//
//     ws.onopen = () => {
//         console.log("WebSocket connection established");
//     };
//
//     ws.onmessage = (event) => {
//         setStartedMessages((prevMessages) => [...prevMessages,  event.data]);
//         //setName(event.data);
//         setIsStarted(true)
//     };
//
//     ws.onerror = (error) => {
//         console.error("WebSocket error:", error);
//     };
//
//     ws.onclose = () => {
//         console.log("WebSocket connection closed");
//     };
//
//     setStartedSocket(ws);
//
//     return () => {
//         ws.close();
//     };
// }, []);

