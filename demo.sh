

curl -X POST \
  http://localhost:8080/people \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"name" : "Bob",
	"number" : "1111111111"
}'

curl -X POST \
  http://localhost:8080/people \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"name" : "Mary",
	"number" : "2222222222"
}'

curl -X POST \
  http://localhost:8080/people \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"name" : "Jane",
	"number" : "3333333333"
}'

curl -X POST \
  http://localhost:8080/people \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"name" : "John",
	"number" : "4444444444"
}'



curl -X POST \
  http://localhost:8080/books \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"name" : "Book1",
    "people": [
		"http://localhost:8080/people/1",
		"http://localhost:8080/people/2",
		"http://localhost:8080/people/3"
    ]
}'


curl -X POST \
  http://localhost:8080/books \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"name" : "Book2",
    "people": [
		"http://localhost:8080/people/2",
		"http://localhost:8080/people/4",
		"http://localhost:8080/people/3"
    ]
}'

curl -X GET \
  'http://localhost:8080/people/search/union?book1=5&book2=6' \
  -H 'cache-control: no-cache' \
