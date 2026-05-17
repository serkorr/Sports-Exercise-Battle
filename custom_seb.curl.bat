@echo off
echo CURL Testing for Sports Exercise Battle Achievements

echo.
echo 1) Register users
curl -X POST http://localhost:10001/users --header "Content-Type: application/json" -d "{\"Username\":\"serkan\", \"Password\":\"1234\"}"
echo.
curl -X POST http://localhost:10001/users --header "Content-Type: application/json" -d "{\"Username\":\"markus\", \"Password\":\"1234\"}"
echo.
echo.

echo 2) Login users
curl -X POST http://localhost:10001/sessions --header "Content-Type: application/json" -d "{\"Username\":\"serkan\", \"Password\":\"1234\"}"
echo.
curl -X POST http://localhost:10001/sessions --header "Content-Type: application/json" -d "{\"Username\":\"markus\", \"Password\":\"1234\"}"
echo.
echo.

echo 3) Achievements should be empty before training
curl -X GET http://localhost:10001/achievements --header "Authorization: Basic serkan-sebToken"
echo.
echo.

echo 4) Add first push-up record, should unlock Getting Started
curl -X POST http://localhost:10001/history --header "Content-Type: application/json" --header "Authorization: Basic serkan-sebToken" -d "{\"Name\":\"PushUps\", \"Count\":40, \"DurationInSeconds\":60}"
echo.
curl -X GET http://localhost:10001/achievements --header "Authorization: Basic serkan-sebToken"
echo.
echo.

echo 5) Add more push-ups, should unlock Getting Stronger at 100 total push-ups
curl -X POST http://localhost:10001/history --header "Content-Type: application/json" --header "Authorization: Basic serkan-sebToken" -d "{\"Name\":\"PushUps\", \"Count\":60, \"DurationInSeconds\":70}"
echo.
curl -X GET http://localhost:10001/achievements --header "Authorization: Basic serkan-sebToken"
echo.
echo.

echo 6) Add more push-ups, should unlock Beast Mode at 200 total push-ups
curl -X POST http://localhost:10001/history --header "Content-Type: application/json" --header "Authorization: Basic serkan-sebToken" -d "{\"Name\":\"PushUps\", \"Count\":100, \"DurationInSeconds\":90}"
echo.
curl -X GET http://localhost:10001/achievements --header "Authorization: Basic serkan-sebToken"
echo.
echo.

echo 7) Other user should still have empty achievements
curl -X GET http://localhost:10001/achievements --header "Authorization: Basic markus-sebToken"
echo.
echo.

echo end...
pause