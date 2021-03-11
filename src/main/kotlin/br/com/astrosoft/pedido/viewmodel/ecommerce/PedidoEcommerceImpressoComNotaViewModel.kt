package br.com.astrosoft.pedido.viewmodel.ecommerce

import br.com.astrosoft.framework.viewmodel.exec
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.pedido.model.beans.ETipoPedido.ENTREGA
import br.com.astrosoft.pedido.model.beans.Pedido

class PedidoEcommerceImpressoComNotaViewModel(val viewModel : PedidoEcommerceViewModel) {
  private val subView
    get() = viewModel.view.tabEcommerceImpressoComNota
  private fun listPedidosEcommerceImpressoComNota(): List<Pedido> {
    val numPedido = subView.pedidoImpressoComNota
    return Pedido.listaPedidoImpressoComNota(ENTREGA, ecommerce = true)
      .filter {pedido ->
        pedido.pedido == numPedido || numPedido == 0
      }
  }
  
  fun updateGridImpressoComNota() {
    subView.updateGrid(listPedidosEcommerceImpressoComNota())
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

interface IPedidoEcommerceImpressoComNota {
  fun updateGrid(itens: List<Pedido>)
  fun itensSelecionado(): List<Pedido>
  
  val pedidoImpressoComNota: Int
}