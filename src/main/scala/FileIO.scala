import scala.util.Using
import scala.io.Source
import org.json4s._
import org.json4s.jackson.JsonMethods._
import java.net.{URL, HttpURLConnection}

object FileIO {

  implicit val formats: Formats = DefaultFormats

  // tipo para representar una suscripcion: nombre y url del subreddit
  type Subscription = (String, String)

  // tipo para un post: subreddit, titulo, texto, fecha formateada
  type Post = (String, String, String, String)

  def readSubscriptions(path: String): Option[List[Subscription]] = {
    val tryRead = Using(Source.fromFile(path)) { source =>
      val lines = source.getLines().toList

      // nos quedamos solo con las lineas de nombre y url
      val filteredLines = lines.filter(line => line.contains("name") || line.contains("url"))
      val groupedLines = filteredLines.grouped(2).toList

      groupedLines.map(par => {
        // limpiamos comillas y comas que vienen del json
        val name = par(0).split(":", 2)(1).replace("\"", "").replace(",", "").trim
        val url  = par(1).split(":", 2)(1).replace("\"", "").replace(",", "").trim
        (name, url)
      })
    }
    tryRead.toOption
  }

  // descarga el contenido de una url, devuelve None si hay algun error
  // reddit pide un User-Agent sino rechaza la peticion
  def downloadFeed(url: String): Option[String] = {
    try {
      val connection = new URL(url).openConnection().asInstanceOf[HttpURLConnection]
      connection.setRequestProperty("User-Agent", "paradigmas26-lab1-g31/0.1")
      Using(Source.fromInputStream(connection.getInputStream))(_.mkString).toOption
    } catch {
      case _: Exception => None
    }
  }

  // parsea el json de reddit y saca los posts que nos interesan
  def parsePosts(subreddit: String, jsonContent: String): Option[List[Post]] = {
    try {
      val json     = parse(jsonContent)
      val children = (json \ "data" \ "children").children
      val posts = children.map { child =>
        val data       = child \ "data"
        val title      = (data \ "title").extract[String]
        val selftext   = (data \ "selftext").extract[String]
        val createdUtc = (data \ "created_utc").extract[Double].toLong
        val date       = TextProcessing.formatDateFromUTC(createdUtc)
        (subreddit, title, selftext, date)
      }
      Some(posts)
    } catch {
      case _: Exception => None
    }
  }

  // combina descarga y parseo en un solo paso
  def fetchPosts(subreddit: String, url: String): Option[List[Post]] =
    downloadFeed(url).flatMap(content => parsePosts(subreddit, content))

  // elimina los posts que no tienen texto ni titulo
  def filterPosts(posts: List[Post]): List[Post] = {
    val filteredPost = posts.filter{ case (_, title, selftext, _) => title.trim.nonEmpty && selftext.trim.nonEmpty}
    filteredPost
  }
}
