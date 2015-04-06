package at.linuxhacker.kafka.actors

import akka.actor.Actor
import akka.actor.ActorLogging
//import at.linuxhacker.kafka.actors._

class MyConsumer1 extends Actor with ActorLogging {

  val receiverPath = "/user/Receiver"
  
  override def preStart = 
   context.actorSelection( receiverPath ) ! Register( self.path )
  
  def receive = {
    case m: ReceivedMessage =>
      log.info( "Got topic: " + m.topic + " message: " + m.message )
    case r: Registered =>
      log.info( "Got Registered")
    case u: Unregistered =>
      log.info( "Got Unregistered. Sending new Register Message" )
      context.actorSelection( receiverPath ) ! Register( self.path )
  }
  override def postStop = 
    context.actorSelection( receiverPath ) ! Unregister( self.path )
    
}