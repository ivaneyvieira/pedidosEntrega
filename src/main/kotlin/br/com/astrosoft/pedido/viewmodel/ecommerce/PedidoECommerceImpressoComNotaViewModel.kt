package br.com.astrosoft.pedido.viewmodel.ecommerce

import br.com.astrosoft.framework.viewmodel.exec
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.pedido.model.beans.ETipoPedido.ENTREGA
import br.com.astrosoft.pedido.model.beans.Pedido

class PedidoECommerceImpressoComNotaViewModel(val viewModel : PedidoECommerceViewModel) {
  private val subView
    get() = viewModel.view.tabECommerceImpressoComNota
  private fun listPedidosECommerceImpressoComNota(): List<Pedido> {
    val numPedido = subView.pedidoImpressoComNota
    return Pedido.listaPedidoImpressoComNota(ENTREGA, ecommerce = true)
      .filter {pedido ->
        pedido.pedido == numPedido || numPedido == 0
      }
  }
  
  fun updateGridImpressoComNota() {
    subView.updateGrid(listPedidosECommerceImpressoComNota())
  }
  
  fun desmarcaComNota() = exec(viewModel.view) {
    val pedidos =
      subView.itensSelecionado()
        .ifEmpty {fail("Não há pedido selecionado")}
    pedidos.forEach {pedido ->
      pedido.desmarcaImpresso()
    }
    
    updateGridImpressoComNota()
  }
}

interface IPedidoECommerceImpressoComNota {
  fun updateGrid(itens: List<Pedido>)
  fun itensSelecionado(): List<Pedido>
  
  val pedidoImpressoComNota: Int
}