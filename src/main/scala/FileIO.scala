import scala.util.Using // esta librería vamos a usar para manejar los archivos
import scala.io.Source

object FileIO {

  // aca definimos el tipo de dato para representar la subscripcion
  type Subscription = (String, String) 

  def readSubscriptions(path: String): Option[List[Subscription]] = {

    val tryRead = Using(Source.fromFile(path)) { source =>
      
      // leemos el archivo y convertimos a una lista
      val lines: List[String] = source.getLines().toList

      // este bloque se encarga de filtrar y agrupar lineas de nombre y url
      val filteredLines = lines.filter(line => line.contains("name") || line.contains("url"))
      val groupedLines = filteredLines.grouped(2).toList

      // este bloque arma la tupla mediante la func map
      val tuple = groupedLines.map(pairLines => {
        val name = pairLines(0)
        val url = pairLines(1)

        // devolvemos la tupla que armamos
        (name, url)
      })

      tuple // lista de tuplas
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