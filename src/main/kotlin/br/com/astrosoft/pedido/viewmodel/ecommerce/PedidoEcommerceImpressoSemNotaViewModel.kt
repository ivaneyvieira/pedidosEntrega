package br.com.astrosoft.pedido.viewmodel.ecommerce

import br.com.astrosoft.framework.viewmodel.exec
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.pedido.model.beans.ETipoPedido.ENTREGA
import br.com.astrosoft.pedido.model.beans.Pedido

class PedidoEcommerceImpressoSemNotaViewModel(val viewModel : PedidoEcommerceViewModel) {
  private val subView
    get() = viewModel.view.tabEcommerceImpressoSemNota
  private fun listPedidosEcommerceImpressoSemNota(): List<Pedido> {
    val numPedido = subView.pedidoImpressoSemNota
    return Pedido.listaPedidoImpressoSemNota(ENTREGA, ecommerce = true)
      .filter {pedido ->
        pedido.pedido == numPedido || numPedido == 0
      }
  }
  
  fun updateGridImpressoSemNota() {
    subView.updateGrid(listPedidosEcommerceImpressoSemNota())
  }
  
  fun desmarcaSemNota() = exec(viewModel.view) {
    val pedidos =
      subView.itensSelecionado()
        .ifEmpty {fail("Não há pedido selecionado")}
    pedidos.forEach {pedido ->
      pedido.desmarcaImpresso()
    }
    
    updateGridImpressoSemNota()
  }
  
  fun imprimirPedidos(itensSelecionado: List<Pedido>) {
    viewModel.tabEcommerceImprimirViewModel.imprimirPedidos(itensSelecionado)
  }
}

interface IPedidoEcommerceImpressoSemNota {
  fun updateGrid(itens: List<Pedido>)
  fun itensSelecionado(): List<Pedido>
  val pedidoImpressoSemNota: Int
}