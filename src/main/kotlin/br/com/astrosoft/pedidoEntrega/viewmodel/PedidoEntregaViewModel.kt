package br.com.astrosoft.pedidoEntrega.viewmodel

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.pedidoEntrega.model.beans.PedidoEntrega


class PedidoEntregaViewModel(view: IPedidoEntregaView): ViewModel<IPedidoEntregaView>(view) {
  fun findPedidos(value: Int?): List<PedidoEntrega> {
    TODO("Not yet implemented")
  }
  
  fun imprimir() {
    TODO("Not yet implemented")
  }
}

interface IPedidoEntregaView: IView {
}