export enum NodeType {
    Console = 'Console',
    Story = 'Story',
    Details = 'Details',
    Zoom = 'Zoom'
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