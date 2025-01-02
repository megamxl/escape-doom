export enum NodeType {

    CONSOLE = 'Console',
    DETAILS = 'Details',
    STORY = 'Story',
    ZOOM = 'Zoom'
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