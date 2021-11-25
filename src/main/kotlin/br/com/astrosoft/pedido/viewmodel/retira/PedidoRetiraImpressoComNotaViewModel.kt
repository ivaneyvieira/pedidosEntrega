package br.com.astrosoft.pedido.viewmodel.retira

import br.com.astrosoft.framework.viewmodel.exec
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.pedido.model.beans.ETipoPedido.RETIRA
import br.com.astrosoft.pedido.model.beans.FiltroPedido
import br.com.astrosoft.pedido.model.beans.Pedido

class PedidoRetiraImpressoComNotaViewModel(val viewModel: PedidoRetiraViewModel) {
  private val subView
    get() = viewModel.view.tabRetiraImpressoComNota

  private fun listPedidosEntregaImpressoComNota(): List<Pedido> {
    val numPedido = subView.pedidoImpressoComNota
    return Pedido.listaPedidoImpressoComNota(FiltroPedido(tipo = RETIRA,
                                                          ecommerce = false,
                                                          dataInicial = null,
                                                          dataFinal = null)).filter { pedido ->
      pedido.pedido == numPedido || numPedido == 0
    }
  }

  fun updateGridImpressoComNota() {
    subView.updateGrid(listPedidosEntregaImpressoComNota())
  }

  fun desmarcaComNota() = exec(viewModel.view) {
    val pedidos = subView.itensSelecionado().ifEmpty { fail("Não há pedido selecionado") }
    pedidos.forEach { pedido ->
      pedido.desmarcaImpresso()
    }

    updateGridImpressoComNota()
  }
}

interface IPedidoRetiraImpressoComNota {
  fun updateGrid(itens: List<Pedido>)
  fun itensSelecionado(): List<Pedido>

  val pedidoImpressoComNota: Int
}