// TODO :: json decoder 추가
export type Ketch<P, R> = (parameter: P) => Promise<R>;

export class KenetClient {
  readonly baseUrl: string;
  readonly endpoints: string[] = [];
  private parent?: KenetClient;
  private name: string = "";

  constructor(baseUrl: string = "") {
    this.baseUrl = baseUrl;
  }

  protected c<P, R>(endpoint: string): Ketch<P, R> {
    this.endpoints.push(endpoint);

    return async (parameter) => {
      const response = await fetch(this.baseUrl + "/kenet", {
        headers: {"Content-Type": "application/json"},
        method: 'POST',
        mode: 'cors',
        body: JSON.stringify({
          subPath: this.createSubPath(),
          endpointName: endpoint,
          parameterJson: JSON.stringify(parameter),
        }),
      });
      return response.json();
    };
  }

  protected s<T extends KenetClient>(name: string, sub: T): T {
    sub.parent = this;
    sub.name = name;
    this.endpoints.push(...sub.endpoints.map(x => `${name}.${x}`));
    return sub;
  }

  private createSubPath(): string[] {
    if (this.parent == null) {
      return [];
    }
    return [...this.parent.createSubPath(), this.name];
  }
}
