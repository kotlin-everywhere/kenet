import { createClient } from "./def.ts";

async function main() {
  const client = createClient("http://localhost:5000/kenet");
  const message = " test.echo.client.typescript";
  const echoedMessage = await client.echo(message);
  console.assert(
    message == echoedMessage,
    `server dose not return same message : messages = ${message}, echoedMessage = ${echoedMessage}`
  );
}

// 그냥 await 를 사용하기 위한 main wrapper
// noinspection JSIgnoredPromiseFromCall
main();
