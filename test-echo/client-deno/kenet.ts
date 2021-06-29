interface KenetRequest {
  endpointName: string;
  parameterJson: string;
}

interface KenetResponse {
  responseJson: string;
}

export class Kenet {
  readonly urlPrefix: string;

  constructor(urlPrefix: string) {
    this.urlPrefix = urlPrefix;
  }

  protected async call<Parameter, Response>(
    endpointName: string,
    parameter: Parameter
  ): Promise<Response> {
    const kenetRequest: KenetRequest = {
      endpointName: endpointName,
      parameterJson: JSON.stringify(parameter),
    };

    const response = await fetch(this.urlPrefix, {
      method: "POST",
      body: JSON.stringify(kenetRequest),
      headers: { "Content-Type": "application/json" },
    });

    // TODO :: correct error handler
    console.assert(
      response.status === 200,
      `call failed : response.status = ${response.status}`
    );

    const kenetResponse: KenetResponse = await response.json();
    return JSON.parse(kenetResponse.responseJson);
  }
}
