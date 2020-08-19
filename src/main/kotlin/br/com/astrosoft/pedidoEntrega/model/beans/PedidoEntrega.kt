package br.com.astrosoft.pedidoEntrega.model.beans

import br.com.astrosoft.AppConfig
import br.com.astrosoft.pedidoEntrega.model.saci
import java.sql.Time
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

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
  val vendedor: String,
  val custno: Int,
  val cliente: String,
  val endereco: String,
  val bairro: String,
  val frete: Double,
  val valor: Double,
  val status: String,
  val area: String,
  val rota: String,
  val obs: String,
  val codArea: Int,
  val userno: Int,
  val username: String,
  val dataPrint: LocalDate?,
  val horaPrint: LocalTime?
                        ) {
  val dataHoraPrint
    get() = if(dataPrint == null || horaPrint == null) null
    else LocalDateTime.of(dataPrint, horaPrint)
  val nfFat: String
    get() = numeroNota(nfnoFat, nfseFat)
  val nfEnt: String
    get() = numeroNota(nfnoEnt, nfseEnt)
  
  private fun numeroNota(nfno: String, nfse: String): String {
    return when {
      nfno == "" -> ""
      nfse == "" -> nfno
      else       -> "$nfno/$nfse"
    }
  }
  
  val paraImprimir: Boolean
    get() = (marca != "S") && (nfnoEnt == "") && (data?.isAfter(LocalDate.of(2020, 5, 18)) ?: true)
  val impressoSemNota: Boolean
    get() = (marca == "S") && (nfnoEnt == "") && (data?.isAfter(LocalDate.of(2020, 5, 17)) ?: true)
  val impressoComNota: Boolean
    get() = (nfnoEnt != "") && (data?.isAfter(LocalDate.of(2020, 5, 17)) ?: true)
  val pedidoPendente: Boolean
    get() = (nfnoEnt == "") && (data?.isAfter(LocalDate.of(2020, 1, 1)) ?: true)
  
  fun marcaImpresso() {
    saci.ativaMarca(loja, pedido, "S")
  }
  
  fun desmarcaImpresso() {
    saci.ativaMarca(loja, pedido, " ")
    desmarcaDataHora()
  }
  
  fun marcaDataHora(dataHora: LocalDateTime) {
    saci.ativaDataHoraImpressao(loja, pedido, dataHora.toLocalDate(), dataHora.toLocalTime())
  }
  
  private fun desmarcaDataHora() {
    saci.ativaDataHoraImpressao(loja, pedido, null, null)
  }
  
  fun sigla() = "MF"
  fun pdv() = ""
  
  fun canPrint(): Boolean = dataHoraPrint == null || (AppConfig.userSaci?.admin ?: false)
  
  fun produtos(): List<ProdutoPedido> = saci.produtoPedido(loja, pedido)
  
  companion object {
    fun listaPedido(): List<PedidoEntrega> = saci.listaPedido()
      .sortedWith(compareBy<PedidoEntrega> {it.data}.thenBy {
        it.hora
      })
    
    fun listaPedidoImprimir(): List<PedidoEntrega> = listaPedido()
      .filter {it.paraImprimir}
    
    fun listaPedidoImpressoSemNota(): List<PedidoEntrega> = listaPedido()
      .filter {it.impressoSemNota}
    
    fun listaPedidoImpressoComNota(): List<PedidoEntrega> = listaPedido()
      .filter {it.impressoComNota}
    
    fun listaPedidoPendente(): List<PedidoEntrega> = listaPedido()
      .filter {it.pedidoPendente}
  }
}