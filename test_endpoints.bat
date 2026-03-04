@echo off
echo === 1. POST /api/users ===
curl.exe -s -X POST http://localhost:8080/api/users -H "Content-Type: application/json" -d "{\"name\":\"Juan Perez\",\"email\":\"juan@email.com\",\"role\":\"ADMIN\"}"
echo.

echo === 2. GET /api/users ===
curl.exe -s http://localhost:8080/api/users
echo.

echo === 3. POST /api/projects ===
curl.exe -s -X POST http://localhost:8080/api/projects -H "Content-Type: application/json" -d "{\"name\":\"Sistema SOA\",\"description\":\"Proyecto arquitectura empresarial\"}"
echo.

echo === 4. GET /api/projects ===
curl.exe -s http://localhost:8080/api/projects
echo.

echo === 5. POST /api/tasks ===
curl.exe -s -X POST http://localhost:8080/api/tasks -H "Content-Type: application/json" -d "{\"title\":\"Diseñar arquitectura\",\"status\":\"PENDING\",\"userId\":1,\"projectId\":1}"
echo.

echo === 6. GET /api/tasks ===
curl.exe -s http://localhost:8080/api/tasks
echo.

echo === 7. GET /api/tasks/user/1 ===
curl.exe -s http://localhost:8080/api/tasks/user/1
echo.

echo === 8. PUT /api/tasks/1/status ===
curl.exe -s -X PUT http://localhost:8080/api/tasks/1/status -H "Content-Type: application/json" -d "{\"status\":\"COMPLETED\"}"
echo.

echo === ERROR: POST task with nonexistent user ===
curl.exe -s -X POST http://localhost:8080/api/tasks -H "Content-Type: application/json" -d "{\"title\":\"Test\",\"status\":\"PENDING\",\"userId\":999,\"projectId\":1}"
echo.

echo === ERROR: PUT nonexistent task ===
curl.exe -s -X PUT http://localhost:8080/api/tasks/999/status -H "Content-Type: application/json" -d "{\"status\":\"COMPLETED\"}"
echo.
