# First Kafka Tests

Following the [QuickStart Guide](http://kafka.apache.org/documentation.html#quickstart) and start a Kafka broker. Currently there a two test classes available: TestProducer and TestConsumer.

# Run

```
sbt run

 [1] at.linuxhacker.kafka.firstTest.TestConsumer
 [2] at.linuxhacker.kafka.firstTest.TestProducer
```

and start in one Console a Consumer and in the other a Producer.

# Eclipse

```
sbt eclipse
```

# Standalone Jar

```
sbt assembly
```

and run a Consumer:

```
$ java -cp /home/stb/projects/test-kafka-prod-cons/target/scala-2.11/KafkaTest-assembly-0.1.jar at.linuxhacker.kafka.firstTest.TestConsumer
```

and a Producer

```
$ java -cp /home/stb/projects/test-kafka-prod-cons/target/scala-2.11/KafkaTest-assembly-0.1.jar at.linuxhacker.kafka.firstTest.TestConsumer
```

# With Akka Actor

Run this test with:

```
sbt run

...
Multiple main classes detected, select one to run:

 [1] at.linuxhacker.kafka.firstTest.TestProducer
 [2] at.linuxhacker.kafka.firstTest.TestConsumer
 [3] at.linuxhacker.kafka.actorSystem.TestApp

```

Nr 3. The test Actor Systems create a receiver and a sender actor. Open Points:

* Using another Thread Pool für the IO Blocking operations.
