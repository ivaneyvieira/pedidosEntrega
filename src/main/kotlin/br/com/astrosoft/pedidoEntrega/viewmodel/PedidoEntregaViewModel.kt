package br.com.astrosoft.pedidoEntrega.viewmodel

import br.com.astrosoft.framework.util.Ssh
import br.com.astrosoft.framework.util.execCommand
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.pedidoEntrega.model.beans.PedidoEntrega
import br.com.astrosoft.pedidoEntrega.model.saci

class PedidoEntregaViewModel(view: IPedidoEntregaView): ViewModel<IPedidoEntregaView>(view) {
  fun imprimir() {
  }
  
  private fun printPrdido(storeno: Int, ordno: Int, impressora: String) {
    Ssh("172.20.47.1", "ivaney", "ivaney").shell {
      execCommand("/u/saci/shells/printPedidos.sh $storeno $ordno $impressora")
    }
  }
  
  fun updateGrid() {
    view.updateGrid(listPedidosEntrega)
  }
  
  val listPedidosEntrega: List<PedidoEntrega>
    get() = saci.listaPedido()
}

interface IPedidoEntregaView: IView {
  fun updateGrid(itens: List<PedidoEntrega>)
  fun itensSelecionado(): List<PedidoEntrega>
}