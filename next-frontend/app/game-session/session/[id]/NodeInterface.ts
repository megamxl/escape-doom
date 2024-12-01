import {NodeType} from "@/app/types/node-type";

export interface NodeInterface {
    type: NodeType
    pos: { x: number, y: number }
    nodeInfos: object
    codeSetter: React.Dispatch<React.SetStateAction<string>>
}

export interface NodeInstance {
    pos: { x: number, y: number }
    nodeInfos: any
}