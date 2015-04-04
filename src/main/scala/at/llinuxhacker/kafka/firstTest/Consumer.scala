package at.llinuxhacker.kafka.firstTest



import kafka.consumer.ConsumerConfig
import kafka.consumer.KafkaStream
import kafka.consumer.ConsumerConnector
import java.util.Properties
import kafka.consumer.Consumer


object TestConsumer {

  def main( args: Array[String] ): Unit = {
    
    val props = new Properties( )
    props.put( "zookeeper.connect", "localhost:2181" );
    props.put( "group.id", "myGroupdId" );
    props.put( "zookeeper.session.timeout.ms", "500" );
    props.put( "zookeeper.sync.time.ms", "250" );
    props.put( "auto.commit.interval.ms", "1000" );
    
    val consumer = Consumer.create( new ConsumerConfig( props ) )
    val topicCount = Map[String,Int]( "test" -> 1 )
    val consumerStreams = consumer.createMessageStreams( topicCount )
    
    val stream = consumerStreams( "test" )
    stream.foreach( x => {
      println( "ClientId: " + x.clientId )
      x.foreach( y => {
        println( "Message: " + new String( y.message ) )
      } )
    } )
    
    /*
    consumerStreams.foreach( s => {
      val s1 = s._1
      val s2 = s._2

      println( "s1: " + s1 + " s2: " + s2 )
      s2.foreach( x => {
        println( "x: " + x  + x.toString )
        x.foreach( y =>
          println( "Key: " + y.key + " message: " + y.message.toString ) )
      } )
    } )
    * */
    
    consumer.shutdown
    
  }
}