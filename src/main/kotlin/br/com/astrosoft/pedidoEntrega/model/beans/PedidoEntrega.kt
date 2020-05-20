package br.com.astrosoft.pedidoEntrega.model.beans

import br.com.astrosoft.pedidoEntrega.model.saci
import java.time.LocalDate
import java.time.LocalTime

data class PedidoEntrega(
  val loja: Int,
  val pedido: Int,
  val marca: String,
  val data: LocalDate?,
  val hora: LocalTime?,
  val nfnoFat: String,
  val nfseFat: String,
  val dataFat: LocalDate?,
  val horaFat: LocalTime?,
  val nfnoEnt: String,
  val nfseEnt: String,
  val dataEnt: LocalDate?,
  val horaEnt: LocalTime?,
  val vendno: Int,
  val custno: Int,
  val frete: Double,
  val valor: Double,
  val status: String,
  val area: String,
  val rota: String,
  val obs: String,
  val codArea: Int,
  val userno: Int,
  val username: String
                        ) {
  val nfFat: String
    get() = numeroNota(nfnoFat, nfseFat)
  val nfEnt: String
    get() = numeroNota(nfnoEnt, nfseEnt)
  
  private fun numeroNota(nfno: String, nfse: String): String {
    return when {
      nfno == "" -> ""
      nfse == "" -> nfnoEnt
      else       -> "$nfno/$nfse"
    }
  }
  
  val paraImprimir: Boolean
    get() = (marca != "S") && (data?.isAfter(LocalDate.of(2020, 5, 18)) ?: true)
  val impressoSemNota: Boolean
    get() = (marca == "S") && (nfnoEnt == "") && (data?.isAfter(LocalDate.of(2020, 5, 17)) ?: true)
  val impressoComNota: Boolean
    get() = (marca == "S") && (nfnoEnt != "")
  
  fun marcaImpresso() {
    saci.ativaMarca(loja, pedido, "S")
  }
  
  fun desmarcaImpresso() {
    saci.ativaMarca(loja, pedido, " ")
  }
  
  companion object {
    fun listaPedido(): List<PedidoEntrega> = saci.listaPedido()
    
    fun listaPedidoImprimir(): List<PedidoEntrega> = saci.listaPedido()
      .filter {it.paraImprimir}
    
    fun listaPedidoImpressoSemNota(): List<PedidoEntrega> = saci.listaPedido()
      .filter {it.impressoSemNota}
    
    fun listaPedidoImpressoComNota(): List<PedidoEntrega> = saci.listaPedido()
      .filter {it.impressoComNota}
  }
}