export interface PasswordResetRequestDTO {
    email: string;
    mode:string;
}
export interface CodeAndPasswordDTO {
    newPassword:string;
    repeatPassword: string;
    code:string;
}