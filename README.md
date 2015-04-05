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

# With Akka Actor System

Run this test with:

```
sbt run

...
Multiple main classes detected, select one to run:

 [1] at.linuxhacker.kafka.firstTest.TestProducer
 [2] at.linuxhacker.kafka.firstTest.TestConsumer
 [3] at.linuxhacker.kafka.actorSystem.TestApp

```

Nr 3. The test Actor System creates a receiver and a sender actor. 

# Failure scenarios and Actor TestApp

Starting the TestApp and then stop the kafka-server:

* The Kafka Producer throws an Exception
* The Sender Actor get stopped ( postStop )
* The Sender Actor will be started again ( preStart )
* And so on
* The messages get lost in this current implemetation.

Log:

```
[INFO] [04/06/2015 00:44:38.258] [TestApp-akka.actor.default-dispatcher-3] [akka://TestApp/user/Sender] received: test message nr 1
[INFO] [04/06/2015 00:44:38.261] [TestApp-akka.actor.default-dispatcher-3] [akka://TestApp/user/Sender] received: test message nr 2
[ERROR] [04/06/2015 00:44:38.705] [TestApp-akka.actor.default-dispatcher-3] [akka://TestApp/user/Sender] Failed to send messages after 3 tries.
kafka.common.FailedToSendMessageException: Failed to send messages after 3 tries.
        at kafka.producer.async.DefaultEventHandler.handle(DefaultEventHandler.scala:90)
        at kafka.producer.Producer.send(Producer.scala:77)
        at at.linuxhacker.kafka.actors.Sender$$anonfun$receive$1.applyOrElse(Sender.scala:38)
        at akka.actor.Actor$class.aroundReceive(Actor.scala:465)
        at at.linuxhacker.kafka.actors.Sender.aroundReceive(Sender.scala:14)
        at akka.actor.ActorCell.receiveMessage(ActorCell.scala:516)
        at akka.actor.ActorCell.invoke(ActorCell.scala:487)
        at akka.dispatch.Mailbox.processMailbox(Mailbox.scala:254)
        at akka.dispatch.Mailbox.run(Mailbox.scala:221)
        at akka.dispatch.Mailbox.exec(Mailbox.scala:231)
        at scala.concurrent.forkjoin.ForkJoinTask.doExec(ForkJoinTask.java:260)
        at scala.concurrent.forkjoin.ForkJoinPool$WorkQueue.runTask(ForkJoinPool.java:1339)
        at scala.concurrent.forkjoin.ForkJoinPool.runWorker(ForkJoinPool.java:1979)
        at scala.concurrent.forkjoin.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:107)

[INFO] [04/06/2015 00:44:38.725] [TestApp-akka.actor.default-dispatcher-2] [akka://TestApp/user/Sender] Kafka Producer stopped.
[INFO] [04/06/2015 00:44:38.727] [TestApp-akka.actor.default-dispatcher-2] [akka://TestApp/user/Sender] Parameter brokerList: localhost:9092
[INFO] [04/06/2015 00:44:38.729] [TestApp-akka.actor.default-dispatcher-2] [akka://TestApp/user/Sender] Kafka Producer started.
[INFO] [04/06/2015 00:44:38.729] [TestApp-akka.actor.default-dispatcher-2] [akka://TestApp/user/Sender] received: test message nr 3
[ERROR] [04/06/2015 00:44:39.157] [TestApp-akka.actor.default-dispatcher-5] [akka://TestApp/user/Sender] Failed to send messages after 3 tries.
kafka.common.FailedToSendMessageException: Failed to send messages after 3 tries.
```

If the Kafka server is reachable again, then the Sender Actor can transmit the messages without manual intervention:

```
[ERROR] [04/06/2015 00:54:04.725] [TestApp-akka.actor.default-dispatcher-5] [akka://TestApp/user/Sender] Failed to send messages after 3 tries.
kafka.common.FailedToSendMessageException: Failed to send messages after 3 tries.
        at kafka.producer.async.DefaultEventHandler.handle(DefaultEventHandler.scala:90)
        at kafka.producer.Producer.send(Producer.scala:77)
        at at.linuxhacker.kafka.actors.Sender$$anonfun$receive$1.applyOrElse(Sender.scala:38)
        at akka.actor.Actor$class.aroundReceive(Actor.scala:465)
        at at.linuxhacker.kafka.actors.Sender.aroundReceive(Sender.scala:14)
        at akka.actor.ActorCell.receiveMessage(ActorCell.scala:516)
        at akka.actor.ActorCell.invoke(ActorCell.scala:487)
        at akka.dispatch.Mailbox.processMailbox(Mailbox.scala:254)
        at akka.dispatch.Mailbox.run(Mailbox.scala:221)
        at akka.dispatch.Mailbox.exec(Mailbox.scala:231)
        at scala.concurrent.forkjoin.ForkJoinTask.doExec(ForkJoinTask.java:260)
        at scala.concurrent.forkjoin.ForkJoinPool$WorkQueue.runTask(ForkJoinPool.java:1339)
        at scala.concurrent.forkjoin.ForkJoinPool.runWorker(ForkJoinPool.java:1979)
        at scala.concurrent.forkjoin.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:107)

[INFO] [04/06/2015 00:54:04.728] [TestApp-akka.actor.default-dispatcher-5] [akka://TestApp/user/Sender] Kafka Producer stopped.
[INFO] [04/06/2015 00:54:04.728] [TestApp-akka.actor.default-dispatcher-5] [akka://TestApp/user/Sender] Parameter brokerList: localhost:9092
[INFO] [04/06/2015 00:54:04.729] [TestApp-akka.actor.default-dispatcher-5] [akka://TestApp/user/Sender] Kafka Producer started.
[INFO] [04/06/2015 00:54:04.729] [TestApp-akka.actor.default-dispatcher-5] [akka://TestApp/user/Sender] received: test message nr 3
[INFO] [04/06/2015 00:54:04.980] [TestApp-akka.actor.default-dispatcher-5] [akka://TestApp/user/Sender] received: test message nr 4
[INFO] [04/06/2015 00:54:04.982] [TestApp-akka.actor.default-dispatcher-5] [akka://TestApp/user/Sender] received: test message nr 5
[INFO] [04/06/2015 00:54:04.983] [TestApp-akka.actor.default-dispatcher-5] [akka://TestApp/user/Sender] received: test message nr 6
[INFO] [04/06/2015 00:54:04.985] [TestApp-akka.actor.default-dispatcher-5] [akka://TestApp/user/Sender] received: test message nr 7
[INFO] [04/06/2015 00:54:04.986] [TestApp-akka.actor.default-dispatcher-6] [akka://TestApp/user/Sender] received: test message nr 8
[INFO] [04/06/2015 00:54:04.988] [TestApp-akka.actor.default-dispatcher-6] [akka://TestApp/user/Sender] received: test message nr 9
Received: message nr 3
Received: message nr 4
Received: message nr 5
Received: message nr 6
Received: message nr 7
Received: message nr 8
```


# TODO:

* Using another Thread Pool für the IO Blocking operations.
