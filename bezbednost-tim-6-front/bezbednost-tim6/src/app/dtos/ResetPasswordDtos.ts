export interface PasswordResetRequestDTO {
    email: string;
}
export interface CodeAndPasswordDTO {
    newPassword:string;
    repeatPassword: string;
    code:string;
}