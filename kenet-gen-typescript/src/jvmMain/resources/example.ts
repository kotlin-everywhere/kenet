import {KenetClient} from "./kenet";

class Api extends KenetClient {
  readonly echo = this.c<string, string>("echo");
  readonly sub = this.s("sub", new (class Sub extends KenetClient {
    readonly echo2 = this.c<string, string>("echo2");
  }));
  readonly add = this.f<number>("add");
}
