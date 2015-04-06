package at.linuxhacker.kafka.actors

import akka.actor.{ Actor, ActorSystem, ActorRef, Props, ActorLogging, ActorPath }
import scala.async.Async.{async, await}
import org.zeromq.ZMQ
import akka.dispatch.ExecutionContexts

case class ZMQMessage( content: String )

class ZeroMqSubscriber( subscriberAddress: String, sendTo: ActorPath ) extends Actor with ActorLogging {
  
  log.info( "ZeroMqSubscriber created")

  var zmqContext: ZMQ.Context = null
  var subscriber: ZMQ.Socket = null
  var runState = true
  
  def readLoop( ):Unit =  {
    log.info ( "Enter readLoop")
    while ( runState ) 
      self ! ZMQMessage( new String( subscriber.recv( 0 ) ) )
  }
  
  override def preStart = {
    zmqContext = ZMQ.context( 1 )
    subscriber = zmqContext.socket( ZMQ.SUB )
    subscriber.connect( subscriberAddress )
    subscriber.subscribe( "".getBytes )
    
    implicit val ec = context.dispatcher
    async { readLoop }
  }
  
  def receive = {
    case m: ZMQMessage =>
      log.info( "Receiving ZMQMessage: " + m.content )
      context.actorSelection( sendTo ) ! SendMessage( "test", m.content )
  }
  
  override def postStop = {
    log.info( "Stopping ZMQ subscriber")
    runState = false
    subscriber.close
    zmqContext.close
    log.info( "ZMQ subscriber stopped")
  }
  
}