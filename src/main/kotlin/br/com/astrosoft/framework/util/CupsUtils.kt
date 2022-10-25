package br.com.astrosoft.framework.util

import org.cups4j.CupsClient
import org.cups4j.CupsPrinter
import org.cups4j.PrintJob

object CupsUtils {
  private val cupsClient = CupsClient("172.20.47.1", 631, "root")
  private val printers
    get() = cupsClient.printers.toList()
  val printersInfo
    get() = printers.filter { it.location != "" }.map { PrinterInfo(it) }

  fun printerExists(printerName: String): Boolean {
    val impressoras = printers
    return impressoras.any { it.name == printerName }
  }

  private fun findPrinter(printerName: String): CupsPrinter? {
    val printers = cupsClient.printers.toList()
    return printers.firstOrNull { it.name == printerName }
  }

  @Throws(ECupsPrinter::class)
  fun CupsPrinter.printText(text: String, resultMsg: (String) -> Unit = {}) {
    val job = PrintJob.Builder(text.toByteArray()).build()
    try {
      val result = print(job)
      resultMsg("Job ${result.jobId}: ${result.resultDescription} : ${result.resultMessage}")
    } catch (e: Exception) {
      throw ECupsPrinter("Erro de impressão")
    }
  }

  fun CupsPrinter.printerTeste() {
    printText(etiqueta)
  }

  @Throws(ECupsPrinter::class)
  fun printCups(impressora: String, text: String, resultMsg: (String) -> Unit = {}) {
    val printer =
      findPrinter(impressora)
      ?: throw ECupsPrinter("Impressora $impressora não está configurada no sistema operacional")
    printer.printText(text, resultMsg)
  }

  private val etiqueta = """
    |^XA
    |^FT20,070^A0N,70,50^FH^FDNF ENTRADA:1212^FS
    |^FT600,070^A0N,70,50^FH^FD30/06/18^FS
    |^FT20,140^A0N,70,50^FH^FDPRODUTO:000019^FS
    |^FT400,140^A0N,70,50^FH^FD - ^FS
    |^FT20,210^A0N,70,50^FH^FDTGR  SD ADA CT  20X012^FS
    |^FT20,280^A0N,70,50^FH^FDPALLET COM: 5CXS^FS
    |^FT20,350^A0N,70,50^FH^FDENTRADA: 1/5 PALLET^FS
    |^FT20,420^A0N,70,50^FH^FDESTOQUE: 1/5PALLET^FS
    |^FT220,650^A0N,250,300^FH^FD1^FS
    |^FO220,700^BY1^BCN,50,Y,N,N^FD000019  5 1/5^FS
    |^XZ""".trimMargin()
}

class ECupsPrinter(msg: String) : Exception(msg)

class PrinterInfo(private val printer: CupsPrinter) {
  val name: String get() = printer.name
  val location: String get() = printer.location
  val description: String get() = printer.description
}