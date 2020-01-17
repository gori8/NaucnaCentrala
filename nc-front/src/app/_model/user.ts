export interface User {
    username: string;
    token?: string;
    expiresIn?:number;
    role?:string;
}