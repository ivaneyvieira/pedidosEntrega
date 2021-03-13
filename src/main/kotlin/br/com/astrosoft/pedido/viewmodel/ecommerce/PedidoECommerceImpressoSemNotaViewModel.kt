package br.com.astrosoft.pedido.viewmodel.ecommerce

import br.com.astrosoft.framework.viewmodel.exec
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.pedido.model.beans.ETipoPedido.ENTREGA
import br.com.astrosoft.pedido.model.beans.Pedido

class PedidoECommerceImpressoSemNotaViewModel(val viewModel: PedidoECommerceViewModel) {
  private val subView
    get() = viewModel.view.tabECommerceImpressoSemNota

  private fun listPedidosECommerceImpressoSemNota(): List<Pedido> {
    val numPedido = subView.pedidoImpressoSemNota
    return Pedido.listaPedidoImpressoSemNota(ENTREGA, ecommerce = true).filter { pedido ->
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
    viewModel.tabECommerceImprimirViewModel.imprimirPedidos(itensSelecionado)
  }
}

interface IPedidoECommerceImpressoSemNota {
  fun updateGrid(itens: List<Pedido>)
  fun itensSelecionado(): List<Pedido>
  val pedidoImpressoSemNota: Int
}