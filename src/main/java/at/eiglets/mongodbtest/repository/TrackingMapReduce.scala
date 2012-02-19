package at.eiglets.mongodbtest.repository
import at.eiglets.mongodbtest.domain.TrackingData.Counter
import at.eiglets.mongodbtest.domain.TrackingData.Key

class TrackingMapReduce(keys: Array[Key], counters: Array[Counter]) {
  
  def mapFunction = "function(){"+
    "emit("+keysAsJson()+", "+countersAsJson()+");"+
  "}"
    
  def reduceFunction = "function(key, values){"+
  	"var result = "+resultInit()+";"+
    "values.forEach(function(value){"+
    	inc()+
    "});"+
    "return result;"+
  "}"
 
  private def resultInit() = {
    val a: Array[String] = counters.map((counter: Counter) => counter.name() + ": 0")
    "{" + a.reduceLeft(_ + "," +_) +"}"
//    "{" + a.mkString(",") +"}"
  }
  
  private def inc() = {
    val a: Array[String] = counters.map((counter: Counter) => "result."+counter.name() + "+= value." + counter.name())
    a.mkString(";")
  }
    
  private def keysAsJson() = {
    val a: Array[String] = keys.map((key: Key) => key.name() + ": this." + key.name())
    "{statday: this.statday, " + a.mkString(",") +"}"
  }  
  
  private def countersAsJson() = {
    val a: Array[String] = counters.map((counter: Counter) => counter.name() + ": this." + counter.name())
    "{" + a.mkString(",") +"}"
  }
    
}