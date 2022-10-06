package br.com.astrosoft.pedido.viewmodel.entrega

import br.com.astrosoft.framework.viewmodel.exec
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.pedido.model.beans.ETipoPedido.ENTREGA
import br.com.astrosoft.pedido.model.beans.FiltroPedido
import br.com.astrosoft.pedido.model.beans.Pedido

class PedidoEntregaImpressoSepararViewModel(val viewModel: PedidoEntregaViewModel) {
  private val subView
    get() = viewModel.view.tabEntregaImpressoSeparar

  private fun listPedidosEntregaImpressoSeparar(): List<Pedido> {
    val pesquisa = subView.pedidoPesquisa
    return Pedido
      .listaPedidoImpressoSeparar(FiltroPedido(tipo = ENTREGA,
                                               pesquisa = pesquisa,
                                               ecommerce = false,
                                               dataInicial = null,
                                               dataFinal = null))
      .filter {
        it.separado != "S"
      }
  }

  fun updateGridImpressoSeparar() {
    subView.updateGrid(listPedidosEntregaImpressoSeparar())
  }

  fun desmarcaSeparar() = exec(viewModel.view) {
    val pedidos = subView.itensSelecionado().ifEmpty { fail("Não há pedido selecionado") }
    pedidos.forEach { pedido ->
      pedido.desmarcaImpresso()
    }

    updateGridImpressoSeparar()
  }

  fun marcaSeparado() = exec(viewModel.view) {
    val pedidos = subView.itensSelecionado().ifEmpty { fail("Não há pedido selecionado") }
    subView.confirmaSeparado {
      pedidos.forEach { pedido ->
        pedido.marcaSeparado("S")
      }

      updateGridImpressoSeparar()
    }
  }

  fun imprimirPedidos(itensSelecionado: List<Pedido>) {
    viewModel.tabEntregaImprimirViewModel.imprimirPedidos(itensSelecionado)
  }
}

interface IPedidoEntregaImpressoSeparar {
  fun updateGrid(itens: List<Pedido>)
  fun itensSelecionado(): List<Pedido>
  val pedidoPesquisa: String

  fun confirmaSeparado(exec : () -> Unit)
}