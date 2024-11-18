export enum NodeType {

    CONSOLE = 'CONSOLE',
    DETAILS = 'DETAILS',
    STORY = 'STORY',
    ZOOM = 'ZOOM'
}

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