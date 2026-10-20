# Real-time primer (for the chat service)

- **Polling**: client asks every N seconds. Simple, wasteful.
- **Server-Sent Events (SSE)**: one-way stream server -> browser over HTTP. Easy, no client -> server channel.
- **WebSocket**: one persistent two-way connection. Needs connection lifecycle, auth at connect time, and heartbeat handling.
- **STOMP**: a messaging protocol that runs on top of WebSocket (topics/queues); supported in Spring.
- **Presence across several instances** needs shared state (database or a broker like Redis): that would be a technology decision.

Auth question to settle early: a browser WebSocket cannot set an `Authorization` header, so the token usually travels in the first message or the URL (with care).
