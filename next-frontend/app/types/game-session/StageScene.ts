import {StageNode} from "@/app/types/game-session/StageNode";

export type StageScene = {
    id: number,
    name: string,
    bgImage: string,
    nodes: StageNode[]
}