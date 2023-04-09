Swagger Doc:
Geh in das Verzeichnis von swagger.json, dann:
docker run --rm -p 9004:8080 --platform linux/amd64 -v $(pwd):/mnt -e SWAGGER_JSON=/mnt/swagger.json swaggerapi/swagger-ui

