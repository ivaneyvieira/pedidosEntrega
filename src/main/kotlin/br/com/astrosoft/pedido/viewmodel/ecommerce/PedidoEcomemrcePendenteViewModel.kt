package br.com.astrosoft.pedido.viewmodel.ecommerce

import br.com.astrosoft.pedido.model.beans.ETipoPedido.ENTREGA
import br.com.astrosoft.pedido.model.beans.Pedido
import java.time.LocalDate

class PedidoEcomemrcePendenteViewModel(val viewModel : PedidoEcommerceViewModel) {
  private val subView
    get() = viewModel.view.tabEcommercePendente
  private fun listPedidosEcommercePendente(): List<Pedido> {
    val numPedido = subView.pedidoPendente
    val data = subView.dataPendente
    val area = subView.areaPendente.trim()
    val rota = subView.rotaPendente.trim()
    return Pedido.listaPedidoPendente(ENTREGA, ecommerce = true)
      .filter {pedido ->
        (pedido.pedido == numPedido || numPedido == 0) &&
        (pedido.data == data || data == null) &&
        (pedido.rota.contains(rota) || rota == "") &&
        (pedido.area.contains(area) || area == "")
      }
  }
  
  fun updateGridPendente() {
    subView.updateGrid(listPedidosEcommercePendente())
  }
}

interface IPedidoEcommercePendente {
  fun updateGrid(itens: List<Pedido>)
  val pedidoPendente: Int
  val dataPendente: LocalDate?
  val areaPendente: String
  val rotaPendente: String
}