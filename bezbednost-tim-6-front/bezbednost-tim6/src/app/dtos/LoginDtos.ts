export interface LoginResponse {
    accessToken: string,
    refreshToken: string
}
export interface LoginRequest {
    email: string,
    password: string
}

export interface LoginCreateCode {
    token: string
}

export interface LoginSecondStepRequest {
    token: string,
    code: string
}