object WordCount {

  val stopwords = Set(
    "the", "about", "above", "after", "again", "against", "all", "am", "an",
    "and", "any", "are", "aren't", "as", "at", "be", "because", "been",
    "before", "being", "below", "between", "both", "but", "by", "can't",
    "cannot", "could", "couldn't", "did", "didn't", "do", "does", "doesn't",
    "doing", "don't", "down", "during", "each", "few", "for", "from", "further",
    "had", "hadn't", "has", "hasn't", "have", "haven't", "having", "he", "he'd",
    "he'll", "he's", "her", "here", "here's", "hers", "herself", "him",
    "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if",
    "in", "into", "is", "isn't", "it", "it's", "its", "itself", "let's", "me",
    "more", "most", "mustn't", "my", "myself", "no", "nor", "not", "of", "off",
    "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves",
    "out", "over", "own", "same", "shan't", "she", "she'd", "she'll", "she's",
    "should", "shouldn't", "so", "some", "such", "than", "that", "that's",
    "their", "theirs", "them", "themselves", "then", "there", "there's",
    "these", "they", "they'd", "they'll", "re", "they've", "this", "those",
    "through", "to", "too", "under", "until", "up", "very", "was", "wasn't",
    "we", "we'd", "we'll", "we're", "we've", "were", "weren't", "what",
    "what's", "when", "when's", "where", "where's", "which", "while", "who",
    "who's", "whom", "why", "why's", "with", "won't", "would", "wouldn't",
    "you", "you'd", "you'll", "you're", "you've", "your", "yours",
    "yourself", "yourselves"
  )

  def counting(posts: List[FileIO.Post]): Map[String, Int] = {

    // paso 1: sacamos todas las palabras del texto de cada post
    val words = posts.flatMap(p => p.selftext.split("\\s+"))

    // paso 2: nos quedamos solo con las que empiezan con mayuscula
    val uppercase = words.filter(w => w.nonEmpty && w.head.isUpper)

    // paso 3: sacamos las stopwords (comparamos en minuscula para no errarle)
    // recibimos una w y devolvemos true si no esta en las stopwords
    val filtered = uppercase.filter(w => !stopwords.contains(w.toLowerCase))

    // paso 4: agrupamos palabras iguales y contamos cuantas veces aparece cada una
    filtered.groupBy(w => w).map { case (word, list) => (word, list.length) }
  }
}
