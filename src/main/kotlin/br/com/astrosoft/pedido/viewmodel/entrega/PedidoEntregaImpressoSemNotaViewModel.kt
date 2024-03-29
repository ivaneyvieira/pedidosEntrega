package br.com.astrosoft.pedido.viewmodel.entrega

import br.com.astrosoft.framework.viewmodel.exec
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.pedido.model.beans.ETipoPedido.ENTREGA
import br.com.astrosoft.pedido.model.beans.FiltroPedido
import br.com.astrosoft.pedido.model.beans.Pedido

class PedidoEntregaImpressoSemNotaViewModel(val viewModel: PedidoEntregaViewModel) {
  private val subView
    get() = viewModel.view.tabEntregaImpressoSemNota

  private fun listPedidosEntregaImpressoSemNota(): List<Pedido> {
    val pesquisa = subView.pedidoPesquisa
    return Pedido.listaPedidoImpressoSemNota(FiltroPedido(tipo = ENTREGA,
                                                          pesquisa = pesquisa,
                                                          ecommerce = false,
                                                          dataInicial = null,
                                                          dataFinal = null))
  }

  fun updateGridImpressoSemNota() {
    subView.updateGrid(listPedidosEntregaImpressoSemNota())
  }

  fun desmarcaSemNota() = exec(viewModel.view) {
    val pedidos = subView.itensSelecionado().ifEmpty { fail("Não há pedido selecionado") }
    pedidos.forEach { pedido ->
      pedido.desmarcaImpresso()
    }

    updateGridImpressoSemNota()
  }

  fun imprimirPedidos(itensSelecionado: List<Pedido>) {
    viewModel.tabEntregaImprimirViewModel.imprimirPedidos(itensSelecionado)
  }
}

interface IPedidoEntregaImpressoSemNota {
  fun updateGrid(itens: List<Pedido>)
  fun itensSelecionado(): List<Pedido>
  val pedidoPesquisa: String
}