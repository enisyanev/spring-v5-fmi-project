export class UploadRequest {
  constructor(
    public accountId: number,
    public instrumentId: number,
    public amount: number,
    public currency : string,
    public type: string) {
  }
}
