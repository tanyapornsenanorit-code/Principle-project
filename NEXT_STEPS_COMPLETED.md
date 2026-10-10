# API, Security, Testing and Deployment hardening

## API endpoints
- `GET /api/v1/costumes`
- `GET /api/v1/costumes/{id}`
- `GET /api/v1/costumes/search?keyword=...`
- `GET /api/v1/costumes/status/{status}`
- `POST /api/v1/shipments/rentals/{rentalId}`
- Swagger UI: `/swagger-ui/index.html`; OpenAPI JSON: `/v3/api-docs`

Costume API responses now use `CostumeResponse` rather than exposing JPA entities.

## Security notes
- The current web login is custom `HttpSession`-based, not Spring Security form authentication. `USER` is the persisted role; the product requirement calls this `CUSTOMER`.
- This patch adds Spring Security infrastructure but leaves route authorization permissive to avoid breaking the existing custom session login. Therefore, **role-based access control is not yet production-complete** and admin/service operations must be guarded consistently before deployment.
- JWT is not enabled and must not be represented as implemented.
- CSRF is disabled to preserve existing legacy form behavior; enable it and add CSRF tokens to all forms before production.

## Tests
Run from `code/`: `./mvnw test` (macOS/Linux) or `mvnw.cmd test` (Windows). The Maven wrapper must have execute permission on Unix: `chmod +x mvnw`.

## Docker
1. Copy `.env.example` to `.env` and set strong, unique `DB_PASSWORD` and `ADMIN_PASSWORD`.
2. Run from repository root: `docker compose up --build`.
3. Open `http://localhost:8080/swagger-ui/index.html`.
4. Stop with `docker compose down`; persistent database data remains in the named volume. Use `docker compose down -v` only if you intentionally want to delete database data.

Do not commit `.env`. Deployment to a hosted server/Render requires creating the service and configuring its environment variables and managed PostgreSQL database in that account; this repository change cannot deploy without account authorization and secrets.
