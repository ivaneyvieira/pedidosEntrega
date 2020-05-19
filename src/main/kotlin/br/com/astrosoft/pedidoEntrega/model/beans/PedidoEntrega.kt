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
    get() = when {
      nfnoFat == "" -> ""
      nfseFat == "" -> nfnoFat
      else          -> "$nfnoFat/$nfseFat"
    }
  val nfEnt: String
    get() = when {
      nfnoEnt == "" -> ""
      nfseEnt == "" -> nfnoEnt
      else          -> "$nfnoEnt/$nfseEnt"
    }
  val paraImprimir: Boolean
    get() = (marca != "S")
  val impressoComNota: Boolean
    get() = (marca == "S") && (nfnoEnt != "")
  val impressoSemNota: Boolean
    get() = (marca == "S") && (nfnoEnt == "")
  
  companion object {
    fun listaPedidoImprimir(): List<PedidoEntrega> = saci.listaPedido()
      .filter {it.paraImprimir}
  
    fun listaPedidoImpressoComNota(): List<PedidoEntrega> = saci.listaPedido()
      .filter {it.impressoComNota}
  
    fun listaPedidoImpressoSemNota(): List<PedidoEntrega> = saci.listaPedido()
      .filter {it.impressoSemNota}
  }
}