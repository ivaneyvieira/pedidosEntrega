package br.com.astrosoft.pedido.model.beans

import java.time.LocalDate

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
  val pisoCxs: Int,
  val pisoPeso: Double,
  val valor: Double,
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
  
  fun groupByNota() = EntregadorNotasGroup(loja, nota, numPedido, datePedido, valorNota, valorFrete)
}

data class EntregadorNotasGroup(val loja: Int,
                                val nota: String,
                                val numPedido: Int,
                                val datePedido: LocalDate?,
                                val valorNota: Double,
                                val valorFrete: Double)