import { Api } from "./dist/api.ts";

const api = new Api("https://example.com");
console.log(`verified endpoint-counts: ${api.endpoints.length}`);
