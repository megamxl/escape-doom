import {useQuery} from "@tanstack/react-query";
import {AuthCreds, getToken} from "@/app/api/lectorPortal/login";

const useAuthentication = (creds: AuthCreds) => {
    return useQuery({
        queryKey: ["authentication", creds],
        queryFn: () => getToken(creds),
        enabled: false
    })
}

export default useAuthentication