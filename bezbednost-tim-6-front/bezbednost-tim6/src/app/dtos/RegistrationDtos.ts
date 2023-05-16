export interface RegistrationDTO{
    email:string;
    password:string;
    name:string;
    surname:string;
    telephoneNumber:string;
    validationType: string;
  }
export interface RegisterResponseDTO{
  id: number,
  name:string,
  surname:string,
  telephoneNumber:string,
  email:string
}