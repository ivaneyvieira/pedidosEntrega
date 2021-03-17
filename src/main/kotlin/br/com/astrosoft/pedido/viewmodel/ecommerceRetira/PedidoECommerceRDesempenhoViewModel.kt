package br.com.astrosoft.pedido.viewmodel.ecommerceRetira

import br.com.astrosoft.pedido.model.beans.Entregador
import java.time.LocalDate

class PedidoECommerceRDesempenhoViewModel(val viewModel: PedidoECommerceRViewModel) {
  private val subView
    get() = viewModel.view.tabECommerceRDesempenho

  private fun listECommerceDesempenho(): List<Entregador> {
    val dateI = subView.dateI
    val dateF = subView.dateF
    return Entregador.findEntregador(dateI, dateF, ecommerce = true)
  }

  fun updateGridECommerceDesempenho() {
    subView.updateGrid(listECommerceDesempenho())
  }
}

interface IPedidoECommerceRDesempenho {
  fun updateGrid(itens: List<Entregador>)
  val dateI: LocalDate
  val dateF: LocalDate
}
