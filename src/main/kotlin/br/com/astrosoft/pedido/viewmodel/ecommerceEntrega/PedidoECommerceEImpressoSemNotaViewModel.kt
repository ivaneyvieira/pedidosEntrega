package br.com.astrosoft.pedido.viewmodel.ecommerceEntrega

import br.com.astrosoft.framework.viewmodel.exec
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.pedido.model.beans.ETipoPedido.ENTREGA
import br.com.astrosoft.pedido.model.beans.FiltroPedido
import br.com.astrosoft.pedido.model.beans.Pedido

class PedidoECommerceEImpressoSemNotaViewModel(val viewModel: PedidoECommerceEViewModel) {
  private val subView
    get() = viewModel.view.tabECommerceEImpressoSemNota

  private fun listPedidosECommerceImpressoSemNota(): List<Pedido> {
    val numPedido = subView.pedidoImpressoSemNota
    return Pedido
      .listaPedidoImpressoSemNota(FiltroPedido(tipo = ENTREGA, ecommerce = true, dataInicial = null, dataFinal = null))
      .filter { pedido ->
        pedido.pedido == numPedido || numPedido == 0
      }
  }

  fun updateGridImpressoSemNota() {
    subView.updateGrid(listPedidosECommerceImpressoSemNota())
  }

  fun desmarcaSemNota() = exec(viewModel.view) {
    val pedidos = subView.itensSelecionado().ifEmpty { fail("Não há pedido selecionado") }
    pedidos.forEach { pedido ->
      pedido.desmarcaImpresso()
    }

    updateGridImpressoSemNota()
  }

  fun imprimirPedidos(itensSelecionado: List<Pedido>) {
    viewModel.tabECommerceEImprimirViewModel.imprimirPedidos(itensSelecionado)
  }
}

interface IPedidoECommerceEImpressoSemNota {
  fun updateGrid(itens: List<Pedido>)
  fun itensSelecionado(): List<Pedido>
  val pedidoImpressoSemNota: Int
}