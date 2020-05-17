package br.com.astrosoft.framework.util

fun String?.lpad(size: Int, filler: String): String {
  var str = this ?: ""
  if(str.length > size) return str.substring(0, size)
  val buf = StringBuilder(str)
  while(buf.length < size) buf.insert(0, filler)

  str = buf.toString()
  return str
}

fun String?.rpad(size: Int, filler: String): String {
  val str = this ?: ""
  if(str.length > size) return str.substring(0, size)
  val buf = StringBuilder(str)
  while(buf.length < size) buf.append(filler)

  return buf.toString()
}

fun String?.trimNull(): String {
  return this?.trim {it <= ' '} ?: ""
}

fun String.mid(start: Int, len: Int): String {
  return if(this == "") ""
  else {
    val end = start + len
    val pStart = when {
      start < 0       -> 0
      start >= length -> length - 1
      else            -> start
    }
    val pEnd = when {
      end < 0      -> 0
      end > length -> length
      else         -> end
    }
    if(pStart <= pEnd) substring(pStart, pEnd)
    else ""
  }
}

fun String.mid(start: Int): String {
  return mid(start, start + length)
}

fun parameterNames(sql: String): List<String> {
  val regex = Regex(":([a-zA-Z0-9_]+)")
  val matches = regex.findAll(sql)
  return matches.map {it.groupValues}.toList().flatten().filter {!it.startsWith(":")}
}
/*
@Suppress("UNCHECKED_CAST")
fun readInstanceProperty(instance: Any, propertyName: String): Any? {
  val property = instance::class.memberProperties
    // don't cast here to <Any, R>, it would succeed silently
    .firstOrNull {it.name == propertyName} as? KProperty1<Any, *>
  // force a invalid cast exception if incorrect type here
  return property?.get(instance)
}
*/