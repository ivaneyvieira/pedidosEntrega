package br.com.astrosoft.pedido.viewmodel.entrega

import br.com.astrosoft.pedido.model.beans.ETipoPedido.ENTREGA
import br.com.astrosoft.pedido.model.beans.FiltroPedido
import br.com.astrosoft.pedido.model.beans.Pedido
import br.com.astrosoft.pedido.model.beans.Rota
import br.com.astrosoft.pedido.model.beans.groupRota

class PedidoEntregaRotaViewModel(val viewModel: PedidoEntregaViewModel) {
  private val subView
    get() = viewModel.view.tabEntregaRota

  private fun listPedidosEntregaRota(): List<Rota> {
    val filtro = subView.filtro()
    return Pedido.listaPedidoImpressoComNota(filtro).groupRota()
  }

  fun updateGrid() {
    subView.updateGrid(listPedidosEntregaRota())
  }
}

interface IPedidoEntregaRota {
  fun updateGrid(itens: List<Rota>)

  fun filtro(): FiltroPedido
}