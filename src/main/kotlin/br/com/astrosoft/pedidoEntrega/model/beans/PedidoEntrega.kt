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
  val data: Date?,
  val hora: Time?,
  val nfnoFat: String,
  val nfseFat: String,
  val dataFat: Date?,
  val horaFat: Time?,
  val nfnoEnt: String,
  val nfseEnt: String,
  val dataEnt: Date?,
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
  
  val dataLD get() = data.toLocalDate()
  
  val paraImprimir: Boolean
    get() = (marca != "S") && (dataLD?.isAfter(LocalDate.of(2020, 5, 18)) ?: true)
  val impressoSemNota: Boolean
    get() = (marca == "S") && (nfnoEnt == "") && (dataLD?.isAfter(LocalDate.of(2020, 5, 17)) ?: true)
  val impressoComNota: Boolean
    get() = (marca == "S") && (nfnoEnt != "")
  
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
  }
}