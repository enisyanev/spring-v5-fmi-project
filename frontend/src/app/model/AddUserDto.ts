export class AddUserDto {
    constructor(
        public username: string,
        public password: string,
        public firstName: string,
        public lastName: string) {
    }
}
