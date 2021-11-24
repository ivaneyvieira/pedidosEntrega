package br.com.astrosoft.pedido.model.beans

import br.com.astrosoft.AppConfig
import br.com.astrosoft.pedido.model.beans.ETipoPedido.ENTREGA
import br.com.astrosoft.pedido.model.beans.ETipoPedido.RETIRA
import br.com.astrosoft.pedido.model.saci
import java.sql.Time
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class Pedido(val loja: Int,
             val nomeLoja: String,
             val siglaLoja: String,
             val pedido: Int,
             val marca: String,
             val data: LocalDate?,
             val dataEntrega: LocalDate?,
             val pdvno: Int,
             val pdvnoVenda: Int?,
             val hora: Time?,
             val nfnoFat: String,
             val nfseFat: String,
             val dataFat: LocalDate?,
             val horaFat: Time?,
             val valorFat: Double,
             val nfnoEnt: String,
             val nfseEnt: String,
             val dataEnt: LocalDate?,
             val horaEnt: Time?,
             val valorEnt: Double,
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
             val tipo: String,
             val metodo: String) {
  val dataHoraPrint
    get() = if (dataPrint == null || horaPrint == null) null
    else LocalDateTime.of(dataPrint, horaPrint)
  val nfFat: String
    get() = numeroNota(nfnoFat, nfseFat)
  val nfEnt: String
    get() = numeroNota(nfnoEnt, nfseEnt)
  val tipoStr
    get() = if (tipo == "E") "Entrega" else "Retira"

  val rotaArea
    get() = when {
      area.startsWith("NORTE")    -> "Norte"
      area.startsWith("SUL")      -> "Sul"
      area.startsWith("LESTE")    -> "Leste"
      area.startsWith("NORDESTE") -> "Nordeste"
      area.startsWith("SUDESTE")  -> "Sudeste"
      else                        -> null
    }

  val isEcommerce
    get() = vendno == 440 && loja == 4
  val tipoEcommece
    get() = if (isEcommerce) "WEB" else ""
  val paraImprimir: Boolean
    get() = (marca != "S") && (nfnoEnt == "") && (data?.isAfter(LocalDate.of(2017, 6, 1)) ?: true)
  val impressoSemNota: Boolean
    get() = (marca == "S") && (nfnoEnt == "") && (data?.isAfter(LocalDate.of(2017, 6, 1)) ?: true)
  val impressoComNota: Boolean
    get() = (nfnoEnt != "") && (data?.isAfter(LocalDate.of(2017, 6, 1)) ?: true)
  val pedidoPendente: Boolean
    get() = (nfnoEnt == "") && (data?.isAfter(LocalDate.of(2017, 6, 1)) ?: true)
  val valorComFrete
    get() = valorFat

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
    fun listaPedido(tipo: ETipoPedido, ecommerce: Boolean): List<Pedido> {
      val storeno = AppConfig.userSaci?.storeno ?: 0
      val lista = when (tipo) {
        ENTREGA -> saci.listaPedido(0, tipo, ecommerce)
        RETIRA  -> saci.listaPedido(storeno, tipo, ecommerce)
      }
      return lista.sortedWith(compareBy<Pedido> { it.data }.thenBy {
        it.hora
      })
    }

    fun listaPedidoImprimir(tipo: ETipoPedido, ecommerce: Boolean): List<Pedido> =
            listaPedido(tipo, ecommerce).filter { it.paraImprimir }

    fun listaPedidoImpressoSemNota(tipo: ETipoPedido, ecommerce: Boolean): List<Pedido> =
            listaPedido(tipo, ecommerce).filter { it.impressoSemNota }

    fun listaPedidoImpressoComNota(tipo: ETipoPedido, ecommerce: Boolean): List<Pedido> =
            listaPedido(tipo, ecommerce).filter { it.impressoComNota }

    fun listaPedidoPendente(tipo: ETipoPedido, ecommerce: Boolean): List<Pedido> =
            listaPedido(tipo, ecommerce).filter { it.pedidoPendente }
  }
}

enum class ETipoPedido(val sigla: String) {
  ENTREGA("E"), RETIRA("R")
}

fun List<Pedido>.rotaPedido(): List<Rota> = map {
  Rota(nomeRota = null,
       loja = null,
       siglaLoja = null,
       pedido = it.pedido,
       data = it.data,
       hora = it.hora,
       nfnoFat = it.nfnoFat,
       nfseFat = it.nfseFat,
       dataFat = it.dataFat,
       horaFat = it.horaFat,
       valorFat = it.valorFat,
       nfnoEnt = it.nfnoEnt,
       nfseEnt = it.nfseEnt,
       dataEnt = it.dataEnt,
       horaEnt = it.horaEnt,
       vendno = it.vendno,
       custno = it.custno,
       frete = it.frete,
       area = it.area,
       rota = it.rota,
       obs = it.obs,
       username = it.username,
       listRota = emptyList(),
       listPedidos = emptyList())
}

fun List<Pedido>.groupLoja() = this.groupBy { pedido ->
  pedido.loja
}.map { entry ->
  val rota = entry.value.firstOrNull()?.rotaArea ?: ""
  val pedidos = entry.value
  Rota(nomeRota = null,
       loja = pedidos.firstOrNull()?.loja,
       siglaLoja = pedidos.firstOrNull()?.siglaLoja,
       valorFat = pedidos.sumOf { it.valorFat },
       frete = pedidos.sumOf { it.frete },
       listRota = pedidos.rotaPedido(),
       listPedidos = pedidos)
}

fun List<Pedido>.groupRota() = this.groupBy { pedido ->
  pedido.rotaArea
}.mapNotNull { entry ->
  val rota = entry.key ?: return@mapNotNull null
  val pedidos = entry.value
  val lojas = entry.value.groupLoja()
  Rota(nomeRota = rota,

       valorFat = pedidos.sumOf { it.valorFat },

       frete = pedidos.sumOf { it.frete },

       listPedidos = pedidos)
}

data class Rota(val nomeRota: String? = "",
                val loja: Int? = null,
                val siglaLoja: String? = "",
                val pedido: Int? = null,
                val data: LocalDate? = null,
                val hora: Time? = null,
                val nfnoFat: String? = "",
                val nfseFat: String? = "",
                val dataFat: LocalDate? = null,
                val horaFat: Time? = null,
                val valorFat: Double? = null,
                val nfnoEnt: String? = "",
                val nfseEnt: String? = "",
                val dataEnt: LocalDate? = null,
                val horaEnt: Time? = null,
                val vendno: Int? = null,
                val custno: Int? = null,
                val frete: Double? = null,
                val area: String? = "",
                val rota: String? = "",
                val obs: String? = "",
                val username: String? = "",
                val listRota: List<Rota> = emptyList(),
                val listPedidos: List<Pedido> = emptyList()) {
  val nfFat: String
    get() = numeroNota(nfnoFat, nfseFat)
  val nfEnt: String
    get() = numeroNota(nfnoEnt, nfseEnt)
  val valorComFrete
    get() = valorFat
}

private fun numeroNota(nfno: String?, nfse: String?): String {
  return when {
    nfno.isNullOrBlank() -> ""
    nfno == ""           -> ""
    nfse.isNullOrBlank() -> nfno
    else                 -> "$nfno/$nfse"
  }
}