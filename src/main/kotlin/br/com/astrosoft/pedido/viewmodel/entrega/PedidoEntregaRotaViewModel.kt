package br.com.astrosoft.pedido.viewmodel.entrega

import br.com.astrosoft.pedido.model.beans.*
import br.com.astrosoft.pedido.model.beans.ETipoPedido.ENTREGA

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

  fun relatorioSimplificado() {
    val filtro = subView.filtro()
    val listRotasLoja =Pedido.listaPedidoImpressoComNota(filtro).groupRotaLoja()

    subView.showRotaLoja(listRotasLoja)
  }
}

interface IPedidoEntregaRota {
  fun updateGrid(itens: List<Rota>)

  fun filtro(): FiltroPedido
  fun showRotaLoja(listRotasLoja: List<Rota>)
}