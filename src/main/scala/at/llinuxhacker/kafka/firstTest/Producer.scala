package at.llinuxhacker.kafka.firstTest

import kafka.producer.{KeyedMessage, Producer, ProducerConfig}
import java.util.Properties

object TestProducer {
  
  def main( args: Array[String] ): Unit = {
    val props = new Properties( )
    props.put( "metadata.broker.list", "localhost:9092" )
    props.put( "serializer.class", "kafka.serializer.StringEncoder" )
    
    val config = new ProducerConfig( props )
    val producer = new Producer[String,String]( config )
    
    producer.send( new KeyedMessage( "test", "This is the first test." ) )
    producer.close( )
  }

}