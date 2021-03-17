package br.com.astrosoft.pedido.viewmodel.ecommerceEntrega

import br.com.astrosoft.pedido.model.beans.Entregador
import java.time.LocalDate

class PedidoECommerceEDesempenhoViewModel(val viewModel: PedidoECommerceEViewModel) {
  private val subView
    get() = viewModel.view.tabECommerceEDesempenho

  private fun listECommerceDesempenho(): List<Entregador> {
    val dateI = subView.dateI
    val dateF = subView.dateF
    return Entregador.findEntregador(dateI, dateF, ecommerce = true)
  }

  fun updateGridECommerceDesempenho() {
    subView.updateGrid(listECommerceDesempenho())
  }
}

interface IPedidoECommerceEDesempenho {
  fun updateGrid(itens: List<Entregador>)
  val dateI: LocalDate
  val dateF: LocalDate
}
