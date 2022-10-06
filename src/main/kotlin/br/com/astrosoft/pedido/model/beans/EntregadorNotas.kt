package br.com.astrosoft.pedido.model.beans

import br.com.astrosoft.framework.util.format
import java.time.LocalDate
import kotlin.collections.Map.Entry

data class EntregadorNotas(val cargano: Int,
                           val funcaoName: String,
                           val nome: String,
                           val empno: Int,
                           val loja: Int,
                           val notaFat: String,
                           val dateFat: LocalDate?,
                           val notaEnt: String,
                           val dateEnt: LocalDate?,
                           val numPedido: Int,
                           val datePedido: LocalDate?,
                           val entrega: LocalDate?,
                           val prdno: String,
                           val grade: String,
                           val descricao: String,
                           val pisoCxs: Int?,
                           val pisoPeso: Double?,
                           val valor: Double?,
                           val valorNota: Double,
                           val valorFrete: Double) : Any() {
  val lojaCol
    get() = if (funcaoName == "") null else loja
  val notaFatCol
    get() = if (funcaoName == "") null else notaFat
  val dateFatCol
    get() = if (funcaoName == "") null else dateFat
  val notaEntCol
    get() = if (funcaoName == "") null else notaEnt
  val dateEntCol
    get() = if (funcaoName == "") null else dateEnt
  val entregaCol
    get() = if (funcaoName == "") null else entrega
  val numPedidoCol
    get() = if (funcaoName == "") null else numPedido
  val datePedidoCol
    get() = if (funcaoName == "") null else datePedido
  val carganoCol
    get() = if (funcaoName == "") null else cargano
  val prdnoCol
    get() = if (funcaoName == "") null else prdno
  val dateFatStr
    get() = dateFat.format()
  val dateEntStr
    get() = dateEnt.format()
  val entregaStr
    get() = entrega.format()
  val datePedidoStr
    get() = datePedido.format()
  var classFormat: Int = 0

  fun groupByNota() = EntregadorNotasGroup(loja, notaEnt)
}

data class EntregadorNotasGroup(val loja: Int, val notaEnt: String)

fun List<EntregadorNotas>.groupByNota(): List<EntregadorNotas> {
  val groupBy = this.groupBy { entregadorNota ->
    entregadorNota.groupByNota()
  }.entries
  val groupValor = totalParcial(groupBy)
  val groupFrete = grupoFrete(groupBy)
  val group = groupFrete + groupValor
  val totalGeral = totalGeral(group)
  val joinList = group + this + totalGeral
  return joinList.sortedWith(compareBy({ it.cargano },
                                       { it.loja },
                                       { it.numPedido },
                                       { it.notaEnt },
                                       { it.datePedido },
                                       { it.prdno }))
}

fun List<EntregadorNotas>.groupByPedido(): List<EntregadorNotas> {
  val groupBy = this.groupBy { entregadorNota ->
    entregadorNota.groupByNota()
  }.entries
  val resumoPedido = resumoPedido(groupBy)
  val totalGeral =
    EntregadorNotas(cargano = 9999999,
                    funcaoName = "",
                    nome = "",
                    dateEnt = null,
                    entrega = null,
                    empno = 0,
                    loja = 999,
                    notaEnt = "",
                    numPedido = 0,
                    datePedido = null,
                    prdno = "",
                    grade = "",
                    descricao = "Total geral",
                    pisoCxs = resumoPedido.sumOf { it.pisoCxs ?: 0 },
                    pisoPeso = resumoPedido.sumOf { it.pisoPeso ?: 0.00 },
                    valor = resumoPedido.sumOf { it.valor ?: 0.00 },
                    valorNota = resumoPedido.sumOf { it.valorNota },
                    valorFrete = resumoPedido.sumOf { it.valorFrete },
                    dateFat = null,
                    notaFat = "")
  return (resumoPedido + totalGeral).sortedWith(compareBy({ it.cargano },
                                                          { it.loja },
                                                          { it.numPedido },
                                                          { it.notaEnt },
                                                          { it.datePedido },
                                                          { it.prdno }))
}

private fun resumoPedido(groupBy: Set<Entry<EntregadorNotasGroup, List<EntregadorNotas>>>): List<EntregadorNotas> {
  val resumo = groupBy.map { entry ->
    val key = entry.key
    val value = entry.value
    EntregadorNotas(cargano = value.firstOrNull()?.cargano ?: 0,
                    funcaoName = value.firstOrNull()?.funcaoName ?: "",
                    nome = value.firstOrNull()?.nome ?: "",
                    dateEnt = value.firstOrNull()?.dateEnt,
                    entrega = value.firstOrNull()?.entrega,
                    notaFat = value.firstOrNull()?.notaFat ?: "",
                    dateFat = value.firstOrNull()?.dateFat,
                    empno = value.firstOrNull()?.empno ?: 0,
                    loja = key.loja,
                    notaEnt = key.notaEnt,
                    numPedido = value.firstOrNull()?.numPedido ?: 0,
                    datePedido = value.firstOrNull()?.datePedido,
                    prdno = "",
                    grade = "",
                    descricao = "",
                    pisoCxs = value.sumOf { it.pisoCxs ?: 0 },
                    pisoPeso = value.sumOf { it.pisoPeso ?: 0.00 },
                    valor = value.firstOrNull()?.valorNota ?: 0.00,
                    valorNota = value.sumOf { it.valorNota },
                    valorFrete = value.firstOrNull()?.valorFrete ?: 0.00)
  }
  return resumo
}

private fun grupoFrete(groupBy: Set<Entry<EntregadorNotasGroup, List<EntregadorNotas>>>): List<EntregadorNotas> {
  val groupFrete = groupBy.map { entry ->
    val key = entry.key
    val first = entry.value.firstOrNull()
    EntregadorNotas(cargano = first?.cargano ?: 0,
                    funcaoName = "",
                    nome = "",
                    dateEnt = null,
                    entrega = null,
                    empno = 0,
                    loja = key.loja,
                    notaFat = "",
                    dateFat = null,
                    notaEnt = key.notaEnt,
                    numPedido = first?.numPedido ?: 0,
                    datePedido = first?.datePedido,
                    prdno = "MMMMMM",
                    grade = "",
                    descricao = "Frete",
                    pisoCxs = null,
                    pisoPeso = null,
                    valor = first?.valorFrete,
                    valorNota = 0.00,
                    valorFrete = 0.00)
  }
  return groupFrete
}

private fun totalGeral(group: List<EntregadorNotas>): EntregadorNotas {
  val totalGeral =
    EntregadorNotas(cargano = 9999999,
                    funcaoName = "",
                    nome = "",
                    dateEnt = null,
                    entrega = null,
                    empno = 0,
                    loja = 999,
                    notaEnt = "",
                    notaFat = "",
                    dateFat = null,
                    numPedido = 0,
                    datePedido = null,
                    prdno = "",
                    grade = "",
                    descricao = "Total geral + Frete: ${
                      group.sumOf { it.valorFrete }.format()
                    }",
                    pisoCxs = group.sumOf { it.pisoCxs ?: 0 },
                    pisoPeso = group.sumOf { it.pisoPeso ?: 0.00 },
                    valor = group.sumOf { it.valorNota },
                    valorNota = group.sumOf { it.valorNota },
                    valorFrete = group.sumOf { it.valorFrete })
  return totalGeral
}

private fun totalParcial(groupBy: Set<Entry<EntregadorNotasGroup, List<EntregadorNotas>>>): List<EntregadorNotas> {
  val groupValor = groupBy.map { entry ->
    val first = entry.value.firstOrNull()
    val valorNota = first?.valorNota ?: 0.00
    val valorFrete = first?.valorFrete ?: 0.00

    EntregadorNotas(cargano = first?.cargano ?: 0,
                    funcaoName = "",
                    nome = "",
                    dateEnt = null,
                    entrega = null,
                    notaFat = "",
                    dateFat = null,
                    empno = 0,
                    loja = entry.key.loja,
                    notaEnt = entry.key.notaEnt,
                    numPedido = first?.numPedido ?: 0,
                    datePedido = first?.datePedido,
                    prdno = "ZZZZZZ",
                    grade = "",
                    descricao = "Total parcial",
                    pisoCxs = entry.value.sumOf { it.pisoCxs ?: 0 },
                    pisoPeso = entry.value.sumOf { it.pisoPeso ?: 0.00 },
                    valor = valorNota,
                    valorNota = valorNota,
                    valorFrete = valorFrete)
  }
  return groupValor
}

fun List<EntregadorNotas>.classificaLinhas(): List<EntregadorNotas> {
  var chave: EntregadorNotasGroup? = null
  var classe = 0
  this.forEach { bean ->
    if (chave != bean.groupByNota()) {
      classe = if (classe == 0) 1 else 0
    }
    bean.classFormat = classe
    chave = bean.groupByNota()
  }
  return this
}
