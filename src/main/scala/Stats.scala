import FileIO.Post

object Stats {

  def calculateScore(posts: List[Post]): Int = {
    posts.foldLeft(0) { (acc, post) =>
      val (_, _, _, _, score, _) = post 
      acc + score
    }
  }

  def getTopPosts(posts: List[Post], n: Int): List[Post] = {
    posts.take(n)
  }
}
