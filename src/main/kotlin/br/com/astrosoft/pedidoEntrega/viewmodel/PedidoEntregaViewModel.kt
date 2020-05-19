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
    get() {
      val numPedido = view.pedidoImprimir
      return PedidoEntrega.listaPedidoImprimir().filter{pedido ->
        pedido.pedido == numPedido || numPedido == 0
      }
    }
  val listPedidosEntregaImpresso: List<PedidoEntrega>get() {
    val numPedido = view.pedidoImpresso
    return PedidoEntrega.listaPedidoImpresso().filter{pedido ->
      pedido.pedido == numPedido || numPedido == 0
    }
  }
}

interface IPedidoEntregaView: IView {
  fun updateGridImprimir(itens: List<PedidoEntrega>)
  fun updateGridImpresso(itens: List<PedidoEntrega>)
  fun itensSelecionado(): List<PedidoEntrega>
  val pedidoImprimir : Int
  val pedidoImpresso : Int
}