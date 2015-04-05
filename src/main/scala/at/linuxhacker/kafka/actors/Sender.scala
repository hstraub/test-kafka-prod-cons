package at.linuxhacker.kafka.actors

import akka.actor.{ Actor, ActorSystem, Props }
import akka.kernel.Bootable
import akka.actor.{ Actor, ActorRef, Props }
import akka.actor.ActorLogging
import akka.actor.TypedActor.PreStart
import kafka.producer.{KeyedMessage, Producer, ProducerConfig}
import java.util.Properties
import kafka.producer.KeyedMessage

case class SendMessage( topic: String, message: String )

class Sender( brokerList: String ) extends Actor with ActorLogging {
  
  log.info( "Parameter brokerList: " + brokerList )
  val props: Properties = new Properties()
  props.put( "metadata.broker.list", brokerList )
  props.put( "serializer.class", "kafka.serializer.StringEncoder" )

  var config: ProducerConfig = null
  var producer: Producer[String,String] = null

  override def preStart = {
    config = new ProducerConfig( props )
    producer = new Producer[String, String]( config )
    log.info( "Kafka Producer started.")
  }
  
  override def postStop = {
    producer.close
    log.info( "Kafka Producer stopped.")
  }
  
  def receive = {
    case m: SendMessage => 
      log.info( "received: " + m.topic + " " + m.message )
      producer.send( new KeyedMessage( m.topic, m.message ) )
  }

}