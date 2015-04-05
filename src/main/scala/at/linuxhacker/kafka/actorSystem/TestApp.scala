package at.linuxhacker.kafka.actorSystem

import akka.actor.ActorSystem
import akka.actor.Props
import at.linuxhacker.kafka.actors._

object TestApp extends App {

  val system = ActorSystem( "TestApp" )
  val propsSender = Props( classOf[Sender], "localhost:9092" )
  val sender = system.actorOf( propsSender, "Sender" )
  val propsReceiver = Props( classOf[Receiver], "localhost:2181", "test", "akkaReveiver")
  val receiver = system.actorOf( propsReceiver, "Receiver" )
  
  for ( i <- 1 until 10 )
    sender ! SendMessage( "test", "message nr " + i )
  
  Thread.sleep( 5000 )
  system.shutdown
}