package br.com.astrosoft.pedido.viewmodel.retira

import br.com.astrosoft.pedido.model.beans.ETipoPedido.RETIRA
import br.com.astrosoft.pedido.model.beans.FiltroPedido
import br.com.astrosoft.pedido.model.beans.Pedido
import java.time.LocalDate

class PedidoRetiraPendenteViewModel(val viewModel: PedidoRetiraViewModel) {
  private val subView
    get() = viewModel.view.tabRetiraPendente

  private fun listPedidosEntregaPendente(): List<Pedido> {
    val numPedido = subView.pedidoPendente
    val data = subView.dataPendente
    val area = subView.areaPendente.trim()
    val rota = subView.rotaPendente.trim()
    return Pedido
      .listaPedidoPendente(FiltroPedido(tipo = RETIRA, ecommerce = false, dataInicial = null, dataFinal = null))
      .filter { pedido ->
        (pedido.pedido == numPedido || numPedido == 0) && (pedido.data == data || data == null) && (pedido.rota.contains(
          rota) || rota == "") && (pedido.area.contains(area) || area == "")
      }
  }

  fun updateGridPendente() {
    subView.updateGrid(listPedidosEntregaPendente())
  }
}

interface IPedidoRetiraPendente {
  fun updateGrid(itens: List<Pedido>)
  val pedidoPendente: Int
  val dataPendente: LocalDate?
  val areaPendente: String
  val rotaPendente: String
}