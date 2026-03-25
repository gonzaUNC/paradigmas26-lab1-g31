import java.time.{Instant, ZoneId}
import java.time.format.DateTimeFormatter

object TextProcessing {

  // convierte un timestamp unix a una fecha legible
  def formatDateFromUTC(utc: Long): String = {
    val instant = Instant.ofEpochSecond(utc)
    val formatter = DateTimeFormatter
      .ofPattern("yyyy-MM-dd HH:mm:ss")
      .withZone(ZoneId.of("UTC"))
    formatter.format(instant)
  }
}
