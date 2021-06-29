import { Kenet } from "./kenet.ts";

export function createClient(urlPrefix: string): Client {
  return new Client(urlPrefix);
}

class Client extends Kenet {
  echo(parameter: string): Promise<string> {
    return this.call("echo", parameter);
  }
}
