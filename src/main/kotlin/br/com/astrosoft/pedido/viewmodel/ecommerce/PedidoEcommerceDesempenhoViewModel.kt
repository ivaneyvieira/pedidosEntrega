package br.com.astrosoft.pedido.viewmodel.ecommerce

import br.com.astrosoft.pedido.model.beans.Entregador
import java.time.LocalDate

class PedidoEcommerceDesempenhoViewModel(val viewModel : PedidoEcommerceViewModel) {
  private val subView
    get() = viewModel.view.tabEcommerceDesempenho
  private fun listEcommerceDesempenho(): List<Entregador> {
    val dateI = subView.dateI
    val dateF = subView.dateF
    return Entregador.findEntregador(dateI, dateF, ecommerce = true)
  }
  
  fun updateGridEcommerceDesempenho() {
    subView.updateGrid(listEcommerceDesempenho())
  }
}

interface IPedidoEcommerceDesempenho {
  fun updateGrid(itens: List<Entregador>)
  val dateI: LocalDate
  val dateF: LocalDate
}
