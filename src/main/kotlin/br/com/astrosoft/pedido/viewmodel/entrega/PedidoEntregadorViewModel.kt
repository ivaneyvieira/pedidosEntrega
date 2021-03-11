package br.com.astrosoft.pedido.viewmodel.entrega

import br.com.astrosoft.pedido.model.beans.Entregador
import java.time.LocalDate

class PedidoEntregadorViewModel(val viewModel: PedidoEntregaViewModel) {
  private val subView
    get() = viewModel.view.tabEntregador

  private fun listEntregador(): List<Entregador> {
    val dateI = subView.dateI
    val dateF = subView.dateF
    return Entregador.findEntregador(dateI, dateF, ecommerce = false)
  }

  fun updateGridEntregador() {
    subView.updateGrid(listEntregador())
  }
}

interface IPedidoEntregador {
  fun updateGrid(itens: List<Entregador>)
  val dateI: LocalDate
  val dateF: LocalDate
}
