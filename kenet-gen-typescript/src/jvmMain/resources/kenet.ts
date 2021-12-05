// TODO :: json decoder 추가
export type Ketch<P, R> = (parameter: P) => Promise<R>;
export type Fire<P> = (parameter: P) => void;

export class KenetClient {
  readonly baseUrl: string;
  readonly endpoints: string[] = [];
  private parent?: KenetClient;
  private name: string = "";
  private readonly blocker?: HTMLDivElement;
  private lock = 0;

  constructor(baseUrl, autoBlock: boolean = false) {
    this.baseUrl = baseUrl;
    if (autoBlock) {
      const blocker = document.createElement("div");
      document.body.appendChild(blocker);
      blocker.style.display = "none";
      blocker.style.top = "0";
      blocker.style.left = "0";
      blocker.style.width = "100%";
      blocker.style.height = "100%";
      blocker.style.position = "fixed";
      blocker.style.backgroundColor = "rgba(0, 0, 0, 0.2)";
      blocker.style.cursor = "wait";
      this.blocker = blocker;
    }
  }

  protected c<P, R>(endpoint: string): Ketch<P, R> {
    this.endpoints.push(endpoint);

    return async (parameter) => {
      // block
      this.lock++;
      if (this.blocker) {
        this.blocker.style.display = "block";
      }

      try {
        const response = await fetch(this.baseUrl + "/kenet", {
          headers: {"Content-Type": "application/json"},
          method: "POST",
          mode: "cors",
          body: JSON.stringify({
            subPath: this.createSubPath(),
            endpointName: endpoint,
            parameterJson: JSON.stringify(parameter),
          }),
        });
        return JSON.parse((await response.json()).responseJson);
      } finally {
        // unblock
        this.lock--;
        if (!this.lock) {
          if (this.blocker) {
            this.blocker.style.display = "none";
          }
        }
      }
    };
  }

  protected f<P>(endpoint: string): Fire<P> {
    this.endpoints.push(endpoint);

    return (parameter) => {
      fetch(this.baseUrl + "/kenet", {
        headers: {"Content-Type": "application/json"},
        method: "POST",
        mode: "cors",
        body: JSON.stringify({
          subPath: this.createSubPath(),
          endpointName: endpoint,
          parameterJson: JSON.stringify(parameter),
        }),
      }).then((response: Response) => {
        if (response.ok) {
          return;
        }
        throw new FetchError('request failed', response);
      })
    };
  }


  protected s<T extends KenetClient>(name: string, sub: T): T {
    sub.parent = this;
    sub.name = name;
    this.endpoints.push(...sub.endpoints.map((x) => `${name}.${x}`));
    return sub;
  }

  private createSubPath(): string[] {
    if (this.parent == null) {
      return [];
    }
    return [...this.parent.createSubPath(), this.name];
  }
}

class FetchError extends Error {
  readonly response: Response;

  constructor(message: string, response: Response) {
    super(message);
    this.response = response;
  }
}
