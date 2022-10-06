package br.com.astrosoft.pedido.viewmodel.ecommerceEntrega

import br.com.astrosoft.framework.viewmodel.exec
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.pedido.model.beans.ETipoPedido.ENTREGA
import br.com.astrosoft.pedido.model.beans.FiltroPedido
import br.com.astrosoft.pedido.model.beans.Pedido

class PedidoECommerceEImpressoComNotaViewModel(val viewModel: PedidoECommerceEViewModel) {
  private val subView
    get() = viewModel.view.tabECommerceEImpressoComNota

  private fun listPedidosECommerceImpressoComNota(): List<Pedido> {
    val numPedido = subView.pedidoImpressoComNota
    return Pedido
      .listaPedidoImpressoComNota(FiltroPedido(tipo = ENTREGA, ecommerce = true, dataInicial = null, dataFinal = null))
      .filter { pedido ->
        pedido.pedido == numPedido || numPedido == 0
      }
  }

  fun updateGridImpressoComNota() {
    subView.updateGrid(listPedidosECommerceImpressoComNota())
  }

  fun desmarcaComNota() = exec(viewModel.view) {
    val pedidos = subView.itensSelecionado().ifEmpty { fail("Não há pedido selecionado") }
    pedidos.forEach { pedido ->
      pedido.desmarcaImpresso()
    }

    updateGridImpressoComNota()
  }
}

interface IPedidoECommerceEImpressoComNota {
  fun updateGrid(itens: List<Pedido>)
  fun itensSelecionado(): List<Pedido>

  val pedidoImpressoComNota: Int
}