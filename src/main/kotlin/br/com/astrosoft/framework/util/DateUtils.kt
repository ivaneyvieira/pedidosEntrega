package br.com.astrosoft.framework.util

import java.sql.Time
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

private const val DATE_PATTERN = "dd/MM/yy"
private const val DATETIME_PATTERN = "dd/MM/yy HH:mm"
private const val TIME_PATTERN = "HH:mm"
private val DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN)
private val DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_PATTERN)
private val TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN)

fun LocalDateTime?.toDate(): Date? {
  if(this == null) return null
  val instant = this.atZone(ZoneId.systemDefault())?.toInstant()
  return Date.from(instant)
}

fun LocalDateTime?.toTimeStamp(): Timestamp? {
  if(this == null) return null
  val instant = this.atZone(ZoneId.systemDefault())?.toInstant()
  return Timestamp.from(instant)
}

fun LocalDate?.toDate(): Date? {
  if(this == null) return null
  val instant = this.atStartOfDay()?.atZone(ZoneId.systemDefault())?.toInstant()
  return Date.from(instant)
}

fun LocalTime?.toDate(): Date? {
  if(this == null) return null
  val date = LocalDate.now()
  val year = date.year
  val month = date.month
  val dayOfMonth = date.dayOfMonth
  val instant = this.atDate(LocalDate.of(year, month, dayOfMonth))?.atZone(ZoneId.systemDefault())?.toInstant()
  return Date.from(instant)
}

fun Date?.toLocalDateTime(): LocalDateTime? {
  if(this == null) return null
  val instant = Instant.ofEpochMilli(this.time)
  return LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
}

fun Date?.toLocalDate(): LocalDate? {
  if(this == null) return null
  val instant = Instant.ofEpochMilli(this.time)
  val zone = ZoneId.systemDefault()
  val zdt = instant.atZone(zone)
  return zdt.toLocalDate()
}

fun LocalDateTime?.format(): String {
  if(this == null) return ""
  return DATETIME_FORMATTER.format(this)
}

fun Date?.format(): String {
  if(this == null) return ""
  val sdf = SimpleDateFormat(DATE_PATTERN)
  return sdf.format(this)
}

fun Time?.format(): String {
  if(this == null) return ""
  val sdf = SimpleDateFormat(TIME_PATTERN)
  return sdf.format(this)
}

fun LocalDate?.format(): String {
  return this?.format(DATE_FORMATTER) ?: ""
}

fun LocalTime?.format(): String {
  return this?.format(TIME_FORMATTER) ?: ""
}

fun Int.localDate(): LocalDate? {
  val strDate = this.toString()
  if(strDate.length != 8) return null
  val year = strDate.substring(0, 4).toIntOrNull() ?: return null
  val month = strDate.substring(4, 6).toIntOrNull() ?: return null
  val day = strDate.substring(6, 8).toIntOrNull() ?: return null
  return LocalDate.of(year, month, day)
}

fun LocalDate.toSaciDate(): Int {
  val ano = this.year
  val mes = this.monthValue
  val dia = this.dayOfMonth
  return ano * 10000 + mes * 100 + dia
}

fun String?.parserDate(): LocalDate? {
  return try {
    LocalDate.parse(this, DATE_FORMATTER)
  } catch(e: Exception) {
    null
  }
}