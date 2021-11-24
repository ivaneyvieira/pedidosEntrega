package br.com.astrosoft.pedido.viewmodel.entrega

import br.com.astrosoft.pedido.model.beans.ETipoPedido.ENTREGA
import br.com.astrosoft.pedido.model.beans.Pedido
import br.com.astrosoft.pedido.model.beans.Rota
import br.com.astrosoft.pedido.model.beans.groupRota

class PedidoEntregaRotaViewModel(val viewModel: PedidoEntregaViewModel) {
  private val subView
    get() = viewModel.view.tabEntregaRota

  private fun listPedidosEntregaRota(): List<Rota> {
    return Pedido.listaPedidoImpressoComNota(ENTREGA, ecommerce = false).groupRota()
  }

  fun updateGrid() {
    subView.updateGrid(listPedidosEntregaRota())
  }

}

interface IPedidoEntregaRota {
  fun updateGrid(itens: List<Rota>)
}