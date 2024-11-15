import {redirect, RedirectType} from "next/navigation";

export default function Home() {

    /**
     * Immediately redirects users to student-join if they call the base url
     */
    const redirectCall = async () => {
        redirect("/game-session/student-join", RedirectType.push)
    }

    return (
        <>
            {redirectCall()}
        </>
    );
}
