package br.com.astrosoft.pedido.viewmodel.retira

import br.com.astrosoft.framework.viewmodel.exec
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.pedido.model.beans.ETipoPedido.RETIRA
import br.com.astrosoft.pedido.model.beans.FiltroPedido
import br.com.astrosoft.pedido.model.beans.Pedido

class PedidoRetiraImpressoSemNotaViewModel(val viewModel: PedidoRetiraViewModel) {
  private val subView
    get() = viewModel.view.tabRetiraImpressoSemNota

  private fun listPedidosEntregaImpressoSemNota(): List<Pedido> {
    val numPedido = subView.pedidoImpressoSemNota
    return Pedido
      .listaPedidoImpressoSemNota(FiltroPedido(tipo = RETIRA, ecommerce = false, dataInicial = null, dataFinal = null))
      .filter { pedido ->
        pedido.pedido == numPedido || numPedido == 0
      }
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
    viewModel.tabRetiraImprimirViewModel.imprimirPedidos(itensSelecionado)
  }
}

interface IPedidoRetiraImpressoSemNota {
  fun updateGrid(itens: List<Pedido>)
  fun itensSelecionado(): List<Pedido>
  val pedidoImpressoSemNota: Int
}