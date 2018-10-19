# Address Book

Exercise for PWC

## Solution

I decided to use the Java Spring Framework and try out the HAL Rest API format. HAL is a standard format for REST APIs, it has the benefit of being explorable, you can use the website: `localhost:8080` to explore the API. I configured H2 database to use the file system at: `~/db/mydb`.

There is also a postman collection I have added to show example requests to the REST API `postman.json`. But you can also run a script: `demo.sh`.

For implementing the unique friends task, I simply used what can be regarded as an SQL query (JPQL).

There are also unit tests for the union functionality, and some tests on the REST API.
