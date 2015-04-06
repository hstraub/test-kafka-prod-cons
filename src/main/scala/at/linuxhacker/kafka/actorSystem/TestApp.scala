package at.linuxhacker.kafka.actorSystem

import akka.actor.{ ActorSystem, Props }
import at.linuxhacker.kafka.actors._


object TestApp extends App {

  val system = ActorSystem( "TestApp" )
  val propsSender = Props( classOf[Sender], "localhost:9092" )
  val sender = system.actorOf( propsSender, "Sender" )
  val propsReceiver = Props( classOf[Receiver], "localhost:2181", "test", "akkaReveiver")
  val myConsumer1 = system.actorOf( Props( classOf[MyConsumer], 1 ), "MyConsumer" + 1 )
  val myConsumer2 = system.actorOf( Props( classOf[MyConsumer], 2 ), "MyConsumer" + 2 )
  val receiver = system.actorOf( propsReceiver, "Receiver" )
  val zmqSubscriber = system.actorOf( Props( classOf[ZeroMqSubscriber], "tcp://localhost:5595" ) )

  for ( x <- 1 until 10 ) {
    for ( i <- 1 until 10 )
      sender ! SendMessage( "test", "loop nr " + x + " message nr " + i )
    Thread.sleep( 5000 )
  }
  
  Thread.sleep( 5000 )
  system.shutdown
}