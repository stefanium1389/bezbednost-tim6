export interface CodeAndRenewPasswordsDTO {
    oldPassword:string;
    newPassword:string;
    repeatPassword: string;
    code:string;
}

export interface PasswordRenewRequestDTO {
    email: string;
}