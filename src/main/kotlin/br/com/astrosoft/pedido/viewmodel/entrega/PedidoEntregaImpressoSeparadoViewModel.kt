package br.com.astrosoft.pedido.viewmodel.entrega

import br.com.astrosoft.framework.viewmodel.exec
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.pedido.model.beans.ETipoPedido.ENTREGA
import br.com.astrosoft.pedido.model.beans.FiltroPedido
import br.com.astrosoft.pedido.model.beans.Pedido

class PedidoEntregaImpressoSeparadoViewModel(val viewModel: PedidoEntregaViewModel) {
  private val subView
    get() = viewModel.view.tabEntregaImpressoSeparado

  private fun listPedidosEntregaImpressoSeparado(): List<Pedido> {
    val pesquisa = subView.pedidoPesquisa
    return Pedido
      .listaPedidoImpressoSeparado(FiltroPedido(tipo = ENTREGA,
                                                pesquisa = pesquisa,
                                                ecommerce = false,
                                                dataInicial = null,
                                                dataFinal = null))
      .filter {
        it.separado == "S"
      }
  }

  fun updateGridImpressoSeparado() {
    subView.updateGrid(listPedidosEntregaImpressoSeparado())
  }

  fun desmarcaSeparado() = exec(viewModel.view) {
    val pedidos = subView.itensSelecionado().ifEmpty { fail("Não há pedido selecionado") }
    pedidos.forEach { pedido ->
      pedido.marcaSeparado(" ")
    }

    updateGridImpressoSeparado()
  }

  fun imprimirPedidos(itensSelecionado: List<Pedido>) {
    viewModel.tabEntregaImprimirViewModel.imprimirPedidos(itensSelecionado)
  }
}

interface IPedidoEntregaImpressoSeparado {
  fun updateGrid(itens: List<Pedido>)
  fun itensSelecionado(): List<Pedido>
  val pedidoPesquisa: String
}