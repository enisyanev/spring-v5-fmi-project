export class Transaction {
    constructor(
        public id: number,
        public amount: number,
        public currency: string,
        public createdTime: Date,
        public description: string,
        public accountid: number) {
    }
}