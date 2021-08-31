// TODO :: json decoder 추가
export type Ketch<P, R> = (parameter: P) => Promise<R>;

export class KenetClient {
  readonly baseUrl: string;

  constructor(baseUrl: string) {
    this.baseUrl = baseUrl;
  }

  protected c<P, R>(endpoint: string): Ketch<P, R> {
    return async (parameter) => {
      const response = await fetch(this.baseUrl + "/kenet", {
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          endpointName: endpoint,
          parameterJson: JSON.stringify(parameter),
        }),
      });
      return response.json();
    };
  }
}
