package at.linuxhacker.kafka.actorSystem

import akka.actor.ActorSystem
import akka.actor.Props
import at.linuxhacker.kafka.actors._
import akka.actor.PoisonPill

object TestApp extends App {

  val system = ActorSystem( "TestApp" )
  val propsSender = Props( classOf[Sender], "localhost:9092" )
  val sender = system.actorOf( propsSender, "Sender" )
  val propsReceiver = Props( classOf[Receiver], "localhost:2181", "test", "akkaReveiver")
  val myConsumer1 = system.actorOf( Props[MyConsumer1], "MyConsumer1" )
  val receiver = system.actorOf( propsReceiver, "Receiver" )

  for ( x <- 1 until 10 ) {
    for ( i <- 1 until 10 )
      sender ! SendMessage( "test", "loop nr " + x + " message nr " + i )
    Thread.sleep( 5000 )
  }
  
  Thread.sleep( 5000 )
  system.shutdown
}