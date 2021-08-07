// TODO :: json decoder 추가
export type Ketch<P, R> = (baseUrl: string, parameter: P) => Promise<R>;

export function fetched<P, R>(endpoint: string): Ketch<P, R> {
  return async (baseUrl, parameter) => {
    const response = await fetch(baseUrl + "/kenet", {
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        endpointName: endpoint,
        parameterJson: JSON.stringify(parameter),
      }),
    });
    return response.json();
  };
}

class KenetClient {
  public static echo = fetched<number, number>("echo");
}
