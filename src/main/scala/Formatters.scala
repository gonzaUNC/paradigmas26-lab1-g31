import FileIO.Post

object Formatters {

  def formatPost(post: Post): String = {
    val (subreddit, title, selftext, date) = post
    // mostramos un preview del texto para no llenar la pantalla
    val preview = if (selftext.length > 200) selftext.take(200) + "..." else selftext
    s"[$subreddit] $title\nFecha: $date\n$preview"
  }

  def formatSubscription(subreddit: String, posts: List[Post], words: Map[String, Int]): String = {
    val header = s"\n${"=" * 80}\nSubreddit: $subreddit\n${"=" * 80}"
    val body   = posts.map(formatPost).mkString("\n" + "-" * 40 + "\n")

    // palabras mas frecuentes ordenadas de mayor a menor
    val sortedWords = words.toList.sortBy { case (_, count) => -count }
    val wordSection = "\nPalabras mas frecuentes:\n" +
      sortedWords.map { case (word, count) => s"  $word: $count" }.mkString("\n")

    header + "\n" + body + "\n" + wordSection
  }
}
