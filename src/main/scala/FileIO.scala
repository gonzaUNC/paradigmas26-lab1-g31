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
      val subscriptions = groupedLines.map(pairLines => {
        // aca agregamos una limpieza, ya que compila pero sin la misma
        // da error por caracteres ilegales 
        val name = pairLines(0).split(":", 2)(1).replace("\"", "").replace(",", "").trim
        val url = pairLines(1).split(":", 2)(1).replace("\"", "").replace(",", "").trim

        // devolvemos la tupla que armamos
        (name, url)
      })

      subscriptions
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