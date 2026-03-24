import scala.util.Using // esta librería vamos a usar para manejar los archivos
import scala.io.Source

object FileIO {

  // aca definimos el tipo de dato para representar la subscripcion
  type Subscription = (String, String) 

  def readSubscriptions(path: String): Option[List[Subscription]] = {

    val tryRead = Using(Source.fromFile(path)) { source =>
      
      // leemos el archivo y convertimos a una lista
      val lines: List[String] = source.getLines().toList

      List.empty[Subscription] 
    }

    // como buena practica pensando en mantener trasparencia referencial
    // convertimos el resultado a option
    tryRead.toOption 
  }

  def downloadFeed(url: String): String = {
    val source = Source.fromURL(url)
    source.mkString
  }
}