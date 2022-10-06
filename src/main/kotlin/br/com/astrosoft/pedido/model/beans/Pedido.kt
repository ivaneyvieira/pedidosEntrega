package br.com.astrosoft.pedido.model.beans

import br.com.astrosoft.AppConfig
import br.com.astrosoft.pedido.model.saci
import br.com.astrosoft.pedido.viewmodel.entrega.EZonaCarga
import java.sql.Time
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class Pedido(
  val loja: Int,
  val nomeLoja: String,
  val siglaLoja: String,
  val pedido: Int,
  val marca: String,
  val separado: String,
  val zonaCarga: String,
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
  val metodo: String,
  val piso: Double,
  val loc: String,
            ) {
  var seq: Int = 0

  val dataHoraPrint
    get() = if (dataPrint == null || horaPrint == null) null
    else LocalDateTime.of(dataPrint, horaPrint)
  val nfFat: String
    get() = numeroNota(nfnoFat, nfseFat)
  val nfEnt: String
    get() = numeroNota(nfnoEnt, nfseEnt)
  val tipoStr
    get() = if (tipo == "E") "Entrega" else "Retira"

  val descricaoZonaCarga: String?
    get() = EZonaCarga.values().firstOrNull {
      it.codigo.toString() == zonaCarga
    }?.descricao

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
    get() = (marca != "S") && (nfnoEnt == "")
  val impressoSemNota: Boolean
    get() = (marca == "S") && (nfnoEnt == "")
  val impressoComNota: Boolean
    get() = (nfnoEnt != "")
  val pedidoPendente: Boolean
    get() = (nfnoEnt == "")
  val valorComFrete
    get() = valorFat

  fun marcaImpresso() {
    saci.ativaMarca(loja, pedido, "S")
  }

  fun desmarcaImpresso() {
    saci.ativaMarca(loja, pedido, " ")
    desmarcaDataHora()
  }

  fun marcaSeparado(marca: String) {
    saci.marcaSeparado(loja, pedido, marca)
  }

  fun marcaDataHora(dataHora: LocalDateTime) {
    saci.ativaDataHoraImpressao(loja, pedido, dataHora.toLocalDate(), dataHora.toLocalTime())
  }

  private fun desmarcaDataHora() {
    saci.ativaDataHoraImpressao(loja, pedido, null, null)
  }

  fun canPrint(): Boolean = dataHoraPrint == null || (AppConfig.isAdmin)

  fun produtos(): List<ProdutoPedido> = saci.produtoPedido(loja, pedido, tipo)
  fun marcaCarga(carga: EZonaCarga) {
    saci.marcaCarga(loja, pedido, carga)
  }

  fun removeCarga() {
    saci.marcaCarga(loja, pedido, EZonaCarga.SemZona)
  }

  companion object {
    fun listaPedido(filtro: FiltroPedido): List<Pedido> {
      val lista = saci.listaPedido(filtro)
      return lista.sortedWith(compareBy<Pedido> { it.data }.thenBy {
        it.hora
      })
    }

    fun listaPedidoImprimir(filtro: FiltroPedido): List<Pedido> = listaPedido(filtro).filter { it.paraImprimir }

    fun listaPedidoImpressoSemNota(filtro: FiltroPedido): List<Pedido> =
      listaPedido(filtro).filter { it.impressoSemNota }

    fun listaPedidoImpressoSeparar(filtro: FiltroPedido): List<Pedido> = listaPedido(filtro)
    fun listaPedidoImpressoCarga(filtro: FiltroPedido): List<Pedido> = listaPedido(filtro)

    fun listaPedidoImpressoSeparado(filtro: FiltroPedido): List<Pedido> = listaPedido(filtro)

    fun listaPedidoImpressoComNota(filtro: FiltroPedido): List<Pedido> =
      listaPedido(filtro).filter { it.impressoComNota }

    fun listaPedidoPendente(filtro: FiltroPedido): List<Pedido> = listaPedido(filtro).filter { it.pedidoPendente }
  }
}

enum class ETipoPedido(val sigla: String) {
  ENTREGA("E"), RETIRA("R")
}

fun List<Pedido>.rotaPedido(): List<Rota> = map {
  Rota(pedido = it.pedido,
       data = it.data,
       nfFat = it.nfFat,
       dataFat = it.dataFat,
       valorFat = it.valorFat,
       nfEnt = it.nfEnt,
       dataEnt = it.dataEnt,
       vendno = it.vendno,
       custno = it.custno,
       frete = it.frete,
       area = it.area,
       rota = it.rota,
       quantEntradas = null,
       listRota = emptyList(),
       listPedidos = emptyList())
}

fun List<Pedido>.groupRotaLoja() = this.groupBy { pedido ->
  "${pedido.rotaArea} - ${pedido.loja}"
}.mapNotNull { entry ->
  val rota = entry.value.firstOrNull()?.rotaArea ?: return@mapNotNull null
  val loja = entry.value.firstOrNull()?.loja ?: return@mapNotNull null
  val pedidos = entry.value
  Rota(nomeRota = rota,
       loja = loja,
       valorFat = pedidos.sumOf { it.valorFat },
       frete = pedidos.sumOf { it.frete },
       quantEntradas = pedidos.size)
}.sortedBy { it.data }

fun List<Pedido>.groupRotas() = this.groupBy { pedido ->
  pedido.rotaArea
}.mapNotNull { entry ->
  val nomeRota = entry.key ?: return@mapNotNull null
  val pedidos = entry.value
  Rota(nomeRota = nomeRota,
       valorFat = pedidos.sumOf { it.valorFat },
       frete = pedidos.sumOf { it.frete },
       quantEntradas = pedidos.size,
       listRota = pedidos.rotaPedido(),
       listPedidos = pedidos)
}.sortedBy { it.nomeRota }

fun List<Pedido>.groupRoot() = this.groupBy { pedido ->
  pedido.loja
}.mapNotNull { entry ->
  val loja = entry.key
  val pedidos = entry.value
  val rotas = pedidos.groupRotas()
  Rota(nomeRota = "",
       loja = loja,
       valorFat = pedidos.sumOf { it.valorFat },
       frete = pedidos.sumOf { it.frete },
       quantEntradas = pedidos.size,
       listRota = rotas,
       listPedidos = pedidos)
}.sortedBy { it.loja }

data class Rota(val nomeRota: String? = "",
                val loja: Int? = null,
                val pedido: Int? = null,
                val data: LocalDate? = null,
                val area: String? = "",
                val rota: String? = "",
                val nfFat: String? = "",
                val dataFat: LocalDate? = null,
                val nfEnt: String? = "",
                val dataEnt: LocalDate? = null,
                val vendno: Int? = null,
                val frete: Double? = null,
                val valorFat: Double? = null,
                val custno: Int? = null,
                val quantEntradas: Int? = null,
                val listRota: List<Rota> = emptyList(),
                val listPedidos: List<Pedido> = emptyList())

private fun numeroNota(nfno: String?, nfse: String?): String {
  return when {
    nfno.isNullOrBlank() -> ""
    nfno == ""           -> ""
    nfse.isNullOrBlank() -> nfno
    else                 -> "$nfno/$nfse"
  }
}

data class FiltroPedido(val tipo: ETipoPedido,
                        val pesquisa: String = "",
                        val ecommerce: Boolean,
                        val dataInicial: LocalDate?,
                        val dataFinal: LocalDate?)