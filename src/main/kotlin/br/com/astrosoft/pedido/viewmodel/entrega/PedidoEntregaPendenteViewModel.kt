package br.com.astrosoft.pedido.viewmodel.entrega

import br.com.astrosoft.pedido.model.beans.ETipoPedido.ENTREGA
import br.com.astrosoft.pedido.model.beans.Pedido
import java.time.LocalDate

class PedidoEntregaPendenteViewModel(val viewModel: PedidoEntregaViewModel) {
  private val subView
    get() = viewModel.view.tabEntregaPendente

  private fun listPedidosEntregaPendente(): List<Pedido> {
    val numPedido = subView.pedidoPendente
    val data = subView.dataPendente
    val area = subView.areaPendente.trim()
    val rota = subView.rotaPendente.trim()
    return Pedido.listaPedidoPendente(ENTREGA, ecommerce = false).filter { pedido ->
        (pedido.pedido == numPedido || numPedido == 0) && (pedido.data == data || data == null) && (pedido.rota.contains(
          rota
                                                                                                                        ) || rota == "") && (pedido.area.contains(
          area
                                                                                                                                                                 ) || area == "")
      }
  }

  fun updateGridPendente() {
    subView.updateGrid(listPedidosEntregaPendente())
  }
}

interface IPedidoEntregaPendente {
  fun updateGrid(itens: List<Pedido>)
  val pedidoPendente: Int
  val dataPendente: LocalDate?
  val areaPendente: String
  val rotaPendente: String
}