package at.linuxhacker.kafka.actors

import akka.actor.{ Actor, ActorSystem, ActorRef, Props, ActorLogging, ActorPath }
import kafka.producer.{KeyedMessage, Producer, ProducerConfig}
import java.util.Properties
import kafka.consumer.{ Consumer, ConsumerConfig, ConsumerConnector }
import scala.async.Async.{async, await}
import akka.dispatch.ExecutionContexts

case class Register( actorPath: ActorPath )
case class Unregister( actorPath: ActorPath )
case class ReceivedMessage( topic: String, message: String )

case class Registered( )
case class Unregistered( )

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
  
  var registeredConsumers = scala.collection.mutable.Set[ActorPath]( )

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
    registeredConsumers.foreach( x => context.actorSelection( x ) ! Unregistered( ) )
    log.info( "Consumer shutdown finished." )
  }
  def receive = {
    case r: Register =>
      log.info( "Register: " + r )
      registeredConsumers.add( r.actorPath )
      sender ! Registered( )
      
    case m: String => 
      log.info( "Received: " + m )
      if ( m contains "loop nr 2 message nr 6" )
        throw new Exception( "Testexception" )
      registeredConsumers.foreach { x =>
        context.actorSelection( x ) ! ReceivedMessage( topic, m )
      }
      
    case u: Unregister =>
      log.info( "Unregister: " + u )
      if ( registeredConsumers contains u.actorPath ) 
        registeredConsumers.remove( u.actorPath )
      sender ! Unregistered( )
          

  }
}