package br.com.astrosoft.framework.viewmodel

import br.com.astrosoft.pedidoEntrega.model.QuerySaci
import br.com.astrosoft.framework.util.CupsUtils
import br.com.astrosoft.framework.util.lpad
import br.com.astrosoft.framework.util.rpad
import java.io.File
import java.text.DecimalFormat

abstract class PrintText<T> {
  private val columns = mutableListOf<Column<T, *>>()
  
  fun columText(header: String, size: Int, lineBreak: Boolean = false,
                process: T.() -> String): PrintText<T> {
    val column = Column(header, size, lineBreak, process) {str ->
      str.rpad(size, " ")
    }
    columns.add(column)
    return this
  }
  
  open fun sumaryLine(): List<String> {
    return emptyList()
  }
  
  fun columNumber(header: String, size: Int, format: String = "0", lineBreak: Boolean = false,
                  process: T.() -> Double): PrintText<T> {
    val decimalFormat = DecimalFormat(format)
    val column = Column(header, size, lineBreak, process) {number ->
      decimalFormat.format(number.toInt())
        .lpad(size, " ")
    }
    columns.add(column)
    return this
  }
  
  private fun header() = montaLinha {col ->
    col.columnText
  }
  
  private fun detail(value: T) = montaLinha {col ->
    col.dataText(value)
  }
  
  fun print(impressora: String, dados: List<T>) {
    dados.firstOrNull()
      ?.let {bean ->
        val text = StringBuilder()
        inicialize(text)
        printTitle(text, bean)
        
        printHeader(text)
        dados.forEach {beanDetail ->
          printDetail(text, beanDetail)
        }
        sumary(text)
        finalize(text)
        if(!QuerySaci.test)
          CupsUtils.printCups(impressora, text.toString())
        else {
          println(text.toString())
          File("/tmp/relatorio.txt").writeText(text.toString())
        }
      }
  }
  
  private fun sumary(text: StringBuilder) {
    sumaryLine().forEach {linha ->
      text.line(linha)
    }
  }
  
  private fun inicialize(text: StringBuilder) {
    text.append(0x1b.toChar())
      .append(0x0f.toChar())
  }
  
  protected fun String.barras(): String {
    val stringBuffer = StringBuilder()
    stringBuffer
      .append(0x1d.toChar())
      .append(0x68.toChar())
      .append(0x50.toChar())
      .append(0x1d.toChar())
      .append(0x77.toChar())
      .append(0x04.toChar())
      .append(0x1d.toChar())
      .append(0x6b.toChar())
      .append(0x49.toChar())
      .append(this.length.toChar())
      .append(this)
    return stringBuffer.toString()
  }
  
  protected fun String.negrito(): String {
    val stringBuffer = StringBuilder()
    stringBuffer.append(0x1b.toChar())
      .append(0x45.toChar())
      .append(this)
      .append(0x1b.toChar())
      .append(0x46.toChar())
    return this
  }
  
  private fun finalize(text: StringBuilder) {
    text.append(0x0a.toChar())
      .append(0x0a.toChar())
      .append(0x0a.toChar())
      .append(0x1b.toChar())
      .append(0x69.toChar())
      .append(0x12.toChar())
  }
  
  private fun printDetail(text: StringBuilder, bean: T) {
    text.line(detail(bean))
  }
  
  private fun printHeader(text: StringBuilder) {
    text.line(header().negrito())
  }
  
  private fun printTitle(text: StringBuilder, bean: T) {
    titleLines(bean).forEach {line ->
      text.line(line)
    }
  }
  
  protected abstract fun titleLines(bean: T): List<String>
  
  private fun String.expandLine(): String {
    val stringBuffer = StringBuilder()
    stringBuffer.append(0x1b.toChar())
      .append(0x45.toChar())
      .append(0x01.toChar())
      .append(this)
      .append(0x1b.toChar())
      .append(0x45.toChar())
      .append(0x00.toChar())
      .appendln()
    return stringBuffer.toString()
  }
  
  private fun StringBuilder.line(line: String): StringBuilder {
    this.append(line)
      .appendln()
    return this
  }
  
  private fun montaLinha(process: (Column<T, *>) -> String): String {
    return columns.joinToString(separator = "") {col ->
      val lineBreak = if(col.lineBreak) "\n" else " "
      val linha = process(col)
      "$linha$lineBreak"
    }
  }
}

data class Column<T, V>(val header: String, val size: Int, val lineBreak: Boolean,
                        val process: T.() -> V, val posProcess: (V) -> String) {
  val columnText
    get() = header.rpad(size, "_")
  
  fun dataText(value: T) = posProcess(process(value))
}
