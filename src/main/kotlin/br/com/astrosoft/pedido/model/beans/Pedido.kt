package br.com.astrosoft.pedido.model.beans

import br.com.astrosoft.AppConfig
import br.com.astrosoft.pedido.model.saci
import java.sql.Time
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class Pedido(
  val loja: Int,
  val nomeLoja: String,
  val siglaLoja: String,
  val pedido: Int,
  val marca: String,
  val data: LocalDate?,
  val dataEntrega: LocalDate?,
  val pdvno: Int,
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
  val foneCliente: String,
  val endereco: String,
  val bairro: String,
  val cidade: String,
  val estado: String,
  val enderecoEntrega: String,
  val bairroEntrega: String,
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
  val horaPrint: LocalTime?,
  val obs1: String,
  val obs2: String,
  val obs3: String,
  val obs4: String,
  val obs5: String,
  val obs6: String,
  val obs7: String,
  val tipo: String
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
  
  fun canPrint(): Boolean = dataHoraPrint == null || (AppConfig.isAdmin)
  
  fun produtos(): List<ProdutoPedido> = saci.produtoPedido(loja, pedido, tipo)
  
  companion object {
    fun listaPedido(tipo : ETipoPedido): List<Pedido> = saci.listaPedido(tipo)
      .sortedWith(compareBy<Pedido> {it.data}.thenBy {
        it.hora
      })
    
    fun listaPedidoImprimir(tipo : ETipoPedido): List<Pedido> = listaPedido(tipo)
      .filter {it.paraImprimir}
    
    fun listaPedidoImpressoSemNota(tipo : ETipoPedido): List<Pedido> = listaPedido(tipo)
      .filter {it.impressoSemNota}
    
    fun listaPedidoImpressoComNota(tipo : ETipoPedido): List<Pedido> = listaPedido(tipo)
      .filter {it.impressoComNota}
    
    fun listaPedidoPendente(tipo : ETipoPedido): List<Pedido> = listaPedido(tipo)
      .filter {it.pedidoPendente}
  }
}

enum class ETipoPedido(val sigla : String){
  ENTREGA("E"), RETIRA("R")
}