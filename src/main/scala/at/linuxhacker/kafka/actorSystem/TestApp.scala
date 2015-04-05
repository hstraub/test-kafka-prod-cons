package at.linuxhacker.kafka.actorSystem

import akka.actor.ActorSystem
import akka.actor.Props
import at.linuxhacker.kafka.actors._

object TestApp extends App {

  val system = ActorSystem( "TestApp" )
  val props = Props( classOf[Sender], "localhost:9092" )
  val sender = system.actorOf( props, "Sender" )
  
  for ( i <- 1 until 10 )
    sender ! SendMessage( "test", "message nr " + i )
  
  Thread.sleep( 5000 )
  system.shutdown
}