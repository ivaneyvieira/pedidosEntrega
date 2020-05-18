package br.com.astrosoft.pedidoEntrega.viewmodel

import br.com.astrosoft.framework.util.Ssh
import br.com.astrosoft.framework.util.execCommand
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.pedidoEntrega.model.beans.PedidoEntrega

class PedidoEntregaViewModel(view: IPedidoEntregaView): ViewModel<IPedidoEntregaView>(view) {
  fun imprimir() {
  }
  
  private fun printPrdido(storeno: Int, ordno: Int, impressora: String) {
    Ssh("172.20.47.1", "ivaney", "ivaney").shell {
      execCommand("/u/saci/shells/printPedidos.sh $storeno $ordno $impressora")
    }
  }
  
  fun updateGridImprimir() {
    view.updateGridImprimir(listPedidosEntregaImprimir)
  }
  
  fun updateGridImpresso() {
    view.updateGridImpresso(listPedidosEntregaImpresso)
  }
  
  val listPedidosEntregaImprimir: List<PedidoEntrega>
    get() = PedidoEntrega.listaPedidoImprimir()
  val listPedidosEntregaImpresso: List<PedidoEntrega>
    get() = PedidoEntrega.listaPedidoImpresso()
}

interface IPedidoEntregaView: IView {
  fun updateGridImprimir(itens: List<PedidoEntrega>)
  fun updateGridImpresso(itens: List<PedidoEntrega>)
  fun itensSelecionado(): List<PedidoEntrega>
}