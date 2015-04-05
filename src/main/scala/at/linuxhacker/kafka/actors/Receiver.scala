package at.linuxhacker.kafka.actors

import akka.actor.{ Actor, ActorSystem, ActorRef, Props }
import akka.actor.ActorLogging
import kafka.producer.{KeyedMessage, Producer, ProducerConfig}
import java.util.Properties
import kafka.consumer.{ Consumer, ConsumerConfig, ConsumerConnector }
import scala.async.Async.{async, await}
import akka.dispatch.ExecutionContexts
//import sun.net.httpserver.ServerImpl.DefaultExecutor

class Receiver( zookeeper: String, topic: String, groupId: String) 
      extends Actor with ActorLogging {
  
  log.info( "Receiver with Zookeeper: " + zookeeper + " groupId: " + groupId )
  val props = new Properties()
  props.put( "zookeeper.connect", zookeeper );
  props.put( "group.id", groupId );
  props.put( "zookeeper.session.timeout.ms", "500" );
  props.put( "zookeeper.sync.time.ms", "250" );
  props.put( "auto.commit.interval.ms", "1000" );
 
  var consumer: ConsumerConnector = null
  val topicCount = Map[String,Int]( "test" -> 1 )

  def kafkaReceiverLoop( ): Unit = {
    val consumerStreams = consumer.createMessageStreams(topicCount)
    val streams = consumerStreams( topic )
        streams.foreach( x =>  x.foreach( y =>
          self ! new String( y.message ) ) )
  }

  override def preStart = {
   implicit val ec = context.dispatcher    
   consumer = Consumer.create( new ConsumerConfig( props ) )
   var future = async { kafkaReceiverLoop() }
  }

  override def postStop = {
    consumer.shutdown
    log.info( "Consumer shutdown finished." )
  }
  def receive = {
    case m: String => println( "Received: " + m )
  }
}