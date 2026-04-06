import FileIO.Post
import javax.swing.text.Position

object Main {
  def main(args: Array[String]): Unit = {
    println(s"Reddit Post Parser\n${"=" * 40}") //deco

    // leemos las suscripciones del archivo json
    val subscriptions = FileIO.readSubscriptions("subscriptions.json").getOrElse(List.empty)

    // para cada suscripcion descargamos y parseamos los posts
    val allPosts: List[Post] = subscriptions.flatMap { case (name, url) =>
      println(s"Descargando posts de: $name")
      FileIO.fetchPosts(name, url).getOrElse(List.empty)
    }

    // realizamos el filtro de post no validos
    val validPosts: List[Post] = FileIO.filterPosts(allPosts)

    // agrupamos por subreddit para mostrar cada uno por separado
    val bySubreddit = validPosts.groupBy { case (subreddit, _, _, _) => subreddit }

    // contamos frecuencia de palabras por subreddit
    val wordsBySubreddit = bySubreddit.map { case (name, posts) =>
      (name, WordCount.counting(posts))
    }

    val output = subscriptions
      .map { case (name, _) =>
        val posts = bySubreddit.getOrElse(name, List.empty)
        val words = wordsBySubreddit.getOrElse(name, Map.empty)
        Formatters.formatSubscription(name, posts, words)
      }
      .mkString("\n")

    println(output)
    println(s"\nTotal de posts: ${allPosts.length}")
  }
}
