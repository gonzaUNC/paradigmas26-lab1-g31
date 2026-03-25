import FileIO.Post

object Main {
  def main(args: Array[String]): Unit = {
    println(s"Reddit Post Parser\n${"=" * 40}")

    // leemos las suscripciones del archivo json
    val subscriptions = FileIO.readSubscriptions("subscriptions.json").getOrElse(List.empty)

    // para cada suscripcion descargamos y parseamos los posts
    val allPosts: List[Post] = subscriptions.flatMap { case (name, url) =>
      println(s"Descargando posts de: $name")
      FileIO.fetchPosts(name, url).getOrElse(List.empty)
    }

    // agrupamos por subreddit para mostrar cada uno por separado
    val bySubreddit = allPosts.groupBy { case (subreddit, _, _, _) => subreddit }

    val output = subscriptions
      .map { case (name, _) => Formatters.formatSubscription(name, bySubreddit.getOrElse(name, List.empty)) }
      .mkString("\n")

    println(output)
    println(s"\nTotal de posts: ${allPosts.length}")
  }
}
