package br.com.astrosoft.pedido.viewmodel.ecommerce

import br.com.astrosoft.pedido.model.beans.Entregador
import java.time.LocalDate

class PedidoECommerceDesempenhoViewModel(val viewModel: PedidoECommerceViewModel) {
  private val subView
    get() = viewModel.view.tabECommerceDesempenho

  private fun listECommerceDesempenho(): List<Entregador> {
    val dateI = subView.dateI
    val dateF = subView.dateF
    return Entregador.findEntregador(dateI, dateF, ecommerce = true)
  }

  fun updateGridECommerceDesempenho() {
    subView.updateGrid(listECommerceDesempenho())
  }
}

interface IPedidoECommerceDesempenho {
  fun updateGrid(itens: List<Entregador>)
  val dateI: LocalDate
  val dateF: LocalDate
}
