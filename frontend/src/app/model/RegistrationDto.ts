export class RegistrationDto {
  constructor(
    public email: string,
    public password: string,
    public firstName: string,
    public lastName: string,
    public currency: string) {
  }
}
