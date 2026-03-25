import FileIO.Post

object Formatters {

  def formatPost(post: Post): String = {
    val (subreddit, title, selftext, date) = post
    // mostramos un preview del texto para no llenar la pantalla
    val preview = if (selftext.length > 200) selftext.take(200) + "..." else selftext
    s"[$subreddit] $title\nFecha: $date\n$preview"
  }

  def formatSubscription(subreddit: String, posts: List[Post]): String = {
    val header = s"\n${"=" * 80}\nSubreddit: $subreddit\n${"=" * 80}"
    val body   = posts.map(formatPost).mkString("\n" + "-" * 40 + "\n")
    header + "\n" + body
  }
}
