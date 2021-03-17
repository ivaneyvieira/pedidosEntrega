package br.com.astrosoft.pedido.viewmodel.ecommerceRetira

import br.com.astrosoft.framework.viewmodel.exec
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.pedido.model.beans.ETipoPedido
import br.com.astrosoft.pedido.model.beans.Pedido

class PedidoECommerceRImpressoSemNotaViewModel(val viewModel: PedidoECommerceRViewModel) {
  private val subView
    get() = viewModel.view.tabECommerceRImpressoSemNota

  private fun listPedidosECommerceImpressoSemNota(): List<Pedido> {
    val numPedido = subView.pedidoImpressoSemNota
    return Pedido.listaPedidoImpressoSemNota(ETipoPedido.RETIRA, ecommerce = true).filter { pedido ->
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
    viewModel.tabECommerceRImprimirViewModel.imprimirPedidos(itensSelecionado)
  }
}

interface IPedidoECommerceRImpressoSemNota {
  fun updateGrid(itens: List<Pedido>)
  fun itensSelecionado(): List<Pedido>
  val pedidoImpressoSemNota: Int
}