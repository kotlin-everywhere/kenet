import { KenetClient } from "./kenet.ts";

class Api extends KenetClient {
  readonly echo = this.c<string, string>("echo");
}
