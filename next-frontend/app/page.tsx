import {redirect, RedirectType} from "next/navigation";
import {PATHS} from "@/app/constants/paths";

export default function Home() {

    /**
     * Immediately redirects users to student-join if they call the base url
     */
    const redirectCall = async () => {
        redirect(PATHS.STUDENT_JOIN, RedirectType.push)
    }

    return (
        <>
            {redirectCall()}
        </>
    );
}
