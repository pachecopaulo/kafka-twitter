# kafka-twitter

### Setup the App
1 - The Twitter API key needs to be setup via environment variable. e.g `export TWITTER_API_KEY=MY_API_KEY`

2 - Run Docker Compose `docker-compose up --build -d`

The application will run on port `9080`, so you can just try it out by running `http://localhost:8080/api/v1/stream`


### Kafka Producer and consumer
Kafka and Zookeeper are lunched via `docker-compose`. A default `test` topic will be created by default.


The current configuration supports running Kafka commands using internal or external client

#### Internal Client
```
kafka-console-producer.sh --broker-list 127.0.0.1:9092 --topic test
kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic test --from-beginning
```

#### External Client
```
kafka-console-producer.sh --broker-list localhost:9093 --topic test
kafka-console-consumer.sh --bootstrap-server localhost:9093 --topic test --from-beginning
```
