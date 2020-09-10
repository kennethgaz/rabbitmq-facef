# RabbitMQ Facef 2020
Trabalho de RabbitMQ (Parking Lot)

### Comandos
```
docker-compose up -d
mvn spring-boot:run
```

### Sobre
Quando a aplicação for iniciada, 8 mensagens serão enfileiradas na fila original do RabbitMq:
* As mensagens de 1 - 3 serão processadas com sucesso e serão removidas da fila original;
* As mensagens de 4 - 5 serão processadas com erro e enviadas para a fila de DLQ para serem processadas novamente;
* As mensagens de 6 - 8 serão processadas com erro e serão removidas da fila original, sem retentativa;

Na fila de DLQ do RabbitMq:
* A mensagem 4 será processada com sucesso e será removida da fila de DLQ;
* A mensagem 5 será processada com erro 3 vezes e enviada para a fila de Parking Lot;

