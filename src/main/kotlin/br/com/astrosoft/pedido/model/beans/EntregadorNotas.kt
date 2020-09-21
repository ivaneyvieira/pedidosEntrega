package br.com.astrosoft.pedido.model.beans

import br.com.astrosoft.framework.util.format
import java.time.LocalDate
import kotlin.collections.Map.Entry

data class EntregadorNotas(
  val cargano: Int,
  val funcaoName: String,
  val nome: String,
  val date: LocalDate?,
  val empno: Int,
  val loja: Int,
  val nota: String,
  val numPedido: Int,
  val datePedido: LocalDate?,
  val prdno: String,
  val grade: String,
  val descricao: String,
  val pisoCxs: Int?,
  val pisoPeso: Double?,
  val valor: Double?,
  val valorNota: Double,
  val valorFrete: Double
                          ): Any() {
  val lojaCol
    get() = if(funcaoName == "") null else loja
  val notaCol
    get() = if(funcaoName == "") null else nota
  val numPedidoCol
    get() = if(funcaoName == "") null else numPedido
  val datePedidoCol
    get() = if(funcaoName == "") null else datePedido
  val dateCol
    get() = if(funcaoName == "") null else date
  val carganoCol
    get() = if(funcaoName == "") null else cargano
  val prdnoCol
    get() = if(funcaoName == "") null else prdno
  val dateStr
    get() = date.format()
  val datePedidoStr
    get() = datePedido.format()
  var classFormat: Int = 0
  
  fun groupByNota() = EntregadorNotasGroup(loja, nota, numPedido, datePedido, valorNota, valorFrete)
}

class EntregadorNotasGroup(val loja: Int,
                                val nota: String,
                                val numPedido: Int,
                                val datePedido: LocalDate?,
                                val valorNota: Double,
                                val valorFrete: Double){
  override fun equals(other: Any?): Boolean {
    if(this === other) return true
    if(javaClass != other?.javaClass) return false
    
    other as EntregadorNotasGroup
    
    if(loja != other.loja) return false
    if(nota != other.nota) return false
    if(numPedido != other.numPedido) return false
    
    return true
  }
  
  override fun hashCode(): Int {
    var result = loja
    result = 31 * result + nota.hashCode()
    result = 31 * result + numPedido
    return result
  }
}

fun List<EntregadorNotas>.groupByNota(): List<EntregadorNotas> {
  val groupBy = this.groupBy {entregadorNota ->
    entregadorNota.groupByNota()
  }.entries
  val groupValor = totalParcial(groupBy)
  val groupFrete = grupoFrete(groupBy)
  val group = groupFrete + groupValor
  val totalGeral = totalGeral(group)
  val joinList = group + this + totalGeral
  return joinList.sortedWith(compareBy({it.loja},
                                       {it.numPedido},
                                       {it.nota},
                                       {it.datePedido},
                                       {it.prdno}))
}

fun List<EntregadorNotas>.groupByPedido(): List<EntregadorNotas> {
  val groupBy = this.groupBy {entregadorNota ->
    entregadorNota.groupByNota()
  }.entries
  val resumoPedido = resumoPedido(groupBy)
  val totalGeral = EntregadorNotas(cargano = 0,
                                   funcaoName = "",
                                   nome = "",
                                   date = null,
                                   empno = 0,
                                   loja = 999,
                                   nota = "",
                                   numPedido = 0,
                                   datePedido = null,
                                   prdno = "",
                                   grade = "",
                                   descricao = "Total geral",
                                   pisoCxs = resumoPedido.sumBy {it.pisoCxs ?: 0},
                                   pisoPeso = resumoPedido.sumByDouble {it.pisoPeso ?: 0.00},
                                   valor = resumoPedido.sumByDouble {it.valor ?: 0.00},
                                   valorNota = resumoPedido.sumByDouble {it.valorNota},
                                   valorFrete = resumoPedido.sumByDouble {it.valorFrete}
                                  )
  return (resumoPedido + totalGeral).sortedWith(compareBy({it.loja},
                                       {it.numPedido},
                                       {it.nota},
                                       {it.datePedido},
                                       {it.prdno}))
}

private fun resumoPedido(groupBy: Set<Entry<EntregadorNotasGroup, List<EntregadorNotas>>>): List<EntregadorNotas> {
  val resumo = groupBy.map{entry ->
    val key = entry.key
    val value = entry.value
    EntregadorNotas(cargano = value.firstOrNull()?.cargano ?: 0,
                    funcaoName = value.firstOrNull()?.funcaoName ?: "",
                    nome = value.firstOrNull()?.nome ?: "",
                    date = value.firstOrNull()?.date,
                    empno = value.firstOrNull()?.empno ?: 0,
                    loja = key.loja,
                    nota = key.nota,
                    numPedido = key.numPedido,
                    datePedido = key.datePedido,
                    prdno = "",
                    grade = "",
                    descricao = "",
                    pisoCxs = value.sumBy {it.pisoCxs ?: 0},
                    pisoPeso = value.sumByDouble {it.pisoPeso ?: 0.00},
                    valor = key.valorNota,
                    valorNota = value.sumByDouble {it.valorNota},
                    valorFrete = key.valorFrete
                   )
  }
  return resumo
}

private fun grupoFrete(groupBy: Set<Entry<EntregadorNotasGroup, List<EntregadorNotas>>>): List<EntregadorNotas> {
  val groupFrete = groupBy.map {entry ->
    val key = entry.key
    EntregadorNotas(cargano = 0,
                    funcaoName = "",
                    nome = "",
                    date = null,
                    empno = 0,
                    loja = key.loja,
                    nota = key.nota,
                    numPedido = key.numPedido,
                    datePedido = key.datePedido,
                    prdno = "MMMMMM",
                    grade = "",
                    descricao = "Frete",
                    pisoCxs = null,
                    pisoPeso = null,
                    valor = key.valorFrete,
                    valorNota = 0.00,
                    valorFrete = 0.00
                   )
  }
  return groupFrete
}

private fun totalGeral(group: List<EntregadorNotas>): EntregadorNotas {
  val totalGeral = EntregadorNotas(cargano = 0,
                                   funcaoName = "",
                                   nome = "",
                                   date = null,
                                   empno = 0,
                                   loja = 999,
                                   nota = "",
                                   numPedido = 0,
                                   datePedido = null,
                                   prdno = "",
                                   grade = "",
                                   descricao = "Total geral + Frete: ${
                                     group.sumByDouble {it.valorFrete}
                                       .format()
                                   }",
                                   pisoCxs = group.sumBy {it.pisoCxs ?: 0},
                                   pisoPeso = group.sumByDouble {it.pisoPeso ?: 0.00},
                                   valor = group.sumByDouble {it.valorNota},
                                   valorNota = group.sumByDouble {it.valorNota},
                                   valorFrete = group.sumByDouble {it.valorFrete}
                                  )
  return totalGeral
}

private fun totalParcial(groupBy: Set<Entry<EntregadorNotasGroup, List<EntregadorNotas>>>): List<EntregadorNotas> {
  val groupValor = groupBy.map {entry ->
    val valorNota = entry.key.valorNota
    val valorFrete = entry.key.valorFrete
    
    EntregadorNotas(cargano = 0,
                    funcaoName = "",
                    nome = "",
                    date = null,
                    empno = 0,
                    loja = entry.key.loja,
                    nota = entry.key.nota,
                    numPedido = entry.key.numPedido,
                    datePedido = entry.key.datePedido,
                    prdno = "ZZZZZZ",
                    grade = "",
                    descricao = "Total parcial",
                    pisoCxs = entry.value.sumBy {it.pisoCxs ?: 0},
                    pisoPeso = entry.value.sumByDouble {it.pisoPeso ?: 0.00},
                    valor = valorNota,
                    valorNota = valorNota,
                    valorFrete = valorFrete
                   )
  }
  return groupValor
}

fun List<EntregadorNotas>.classificaLinhas(): List<EntregadorNotas> {
  var chave: EntregadorNotasGroup? = null
  var classe = 0
  this.forEach {bean ->
    if(chave != bean.groupByNota()) {
      classe = if(classe == 0)        1      else 0
    }
    bean.classFormat = classe
    chave = bean.groupByNota()
  }
  return this
}
