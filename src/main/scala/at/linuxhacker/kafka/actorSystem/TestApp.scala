package at.linuxhacker.kafka.actorSystem

import akka.actor.{ ActorSystem, Props }
import at.linuxhacker.kafka.actors._
import com.typesafe.config.ConfigFactory


object TestApp extends App {

  val system = ActorSystem( "TestApp" )
  val config = system.settings.config.getConfig( "TestKafka" )
  val propsSender = Props( classOf[Sender], config.getString( "brokerList" ) )
  val sender = system.actorOf( propsSender, "Sender" )
  val propsReceiver = Props( classOf[Receiver], config.getString( "zookeeperConnect" ), "test", "akkaReveiver")
  val myConsumer1 = system.actorOf( Props( classOf[MyConsumer], 1 ), "MyConsumer" + 1 )
  val myConsumer2 = system.actorOf( Props( classOf[MyConsumer], 2 ), "MyConsumer" + 2 )
  val receiver = system.actorOf( propsReceiver, "Receiver" )
  val zmqSubscriber = system.actorOf( Props( classOf[ZeroMqSubscriber], config.getString( "zeroMqSubscriber"), sender.path ) )

  for ( x <- 1 until 10 ) {
    for ( i <- 1 until 10 )
      sender ! SendMessage( "test", "loop nr " + x + " message nr " + i )
    Thread.sleep( 5000 )
  }

}