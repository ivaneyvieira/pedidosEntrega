package br.com.astrosoft.pedidoEntrega.model.beans

import br.com.astrosoft.framework.util.toLocalDate
import br.com.astrosoft.pedidoEntrega.model.saci
import java.sql.Time
import java.time.LocalDate
import java.util.*

data class PedidoEntrega(
  val loja: Int,
  val pedido: Int,
  val marca: String,
  val data: LocalDate?,
  val hora: Time?,
  val nfnoFat: String,
  val nfseFat: String,
  val dataFat: LocalDate?,
  val horaFat: Time?,
  val nfnoEnt: String,
  val nfseEnt: String,
  val dataEnt: LocalDate?,
  val horaEnt: Time?,
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
    get() = (marca == "S") && (nfnoEnt != "") && (data?.isAfter(LocalDate.of(2020, 5, 17)) ?: true)
  val pedidoPendente: Boolean
    get() = (nfnoEnt == "") && (data?.isAfter(LocalDate.of(2020, 1, 1)) ?: true)
  
  fun marcaImpresso() {
    saci.ativaMarca(loja, pedido, "S")
  }
  
  fun desmarcaImpresso() {
    saci.ativaMarca(loja, pedido, " ")
  }
  
  companion object {
    val listaPedido = mutableListOf<PedidoEntrega>()
    
    fun update() {
      listaPedido.run{
        this.clear()
        this.addAll(saci.listaPedido())
      }
    }
    
    fun listaPedido(): List<PedidoEntrega> = listaPedido
    
    fun listaPedidoImprimir(): List<PedidoEntrega> = listaPedido
      .filter {it.paraImprimir}
    
    fun listaPedidoImpressoSemNota(): List<PedidoEntrega> = saci.listaPedido()
      .filter {it.impressoSemNota}
  
    fun listaPedidoImpressoComNota(): List<PedidoEntrega> = listaPedido
      .filter {it.impressoComNota}
 
    fun listaPedidoPendente(): List<PedidoEntrega> = listaPedido
      .filter {it.pedidoPendente}
  }
}