// TODO :: json decoder 추가
export type Ketch<P, R> = (parameter: P) => Promise<R>;

export class KenetClient {
  readonly baseUrl: string;
  readonly endpoints: string[] = [];

  constructor(baseUrl: string) {
    this.baseUrl = baseUrl;
  }

  protected c<P, R>(endpoint: string): Ketch<P, R> {
    this.endpoints.push(endpoint);

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
