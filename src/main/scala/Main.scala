object Main {
  def main(args: Array[String]): Unit = {
    val header = s"Reddit Post Parser\n${"=" * 40}"

    val subscriptions: List[(String, String)] = 
      FileIO.readSubscriptions("subscriptions.json").getOrElse(List.empty)

    // usamos pattern matching para desestructurar la tupla de nombre y url
    val allPosts: List[(String, String)] = subscriptions.map { case (name, url) =>
      println(s"Fetching posts from subreddit: $name")
      val posts = FileIO.downloadFeed(url)
      (url, posts)
    }

    val output = allPosts
      .map { case (url, posts) => Formatters.formatSubscription(url, posts) }
      .mkString("\n")

    println(output)
  }
}