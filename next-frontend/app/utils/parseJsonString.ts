import {StageScene} from "@/app/types/game-session/StageScene";

export function parseStage(data: string[] | undefined): StageScene | undefined {
    try {

        if (data && data.length > 0) {
            if (data) {
                const parsedStage: StageScene = JSON.parse(data[0]);
                //console.log(parsedStage)
                return parsedStage;
            }
        }
        return undefined;
    } catch (error) {
        console.error('Error parsing stage data:', error);
        return undefined;
    }
}