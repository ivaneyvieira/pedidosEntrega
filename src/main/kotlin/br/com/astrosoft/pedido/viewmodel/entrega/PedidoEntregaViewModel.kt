package br.com.astrosoft.pedido.viewmodel.entrega

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.pedido.model.beans.Pedido

class PedidoEntregaViewModel(view: IPedidoEntregaView): ViewModel<IPedidoEntregaView>(view) {
  val tabEntregaImprimirViewModel = PedidoEntregaImprimirViewModel(this)
  val tabEntregaImpressoSemNotaViewModel = PedidoEntregaImpressoSemNotaViewModel(this)
  val tabEntregaPendenteViewModel = PedidoEntregaPendenteViewModel(this)
  val tabEntregaImpressoComNotaViewModel = PedidoEntregaImpressoComNotaViewModel(this)
  val tabEntregadorViewModel = PedidoEntregadorViewModel(this)
}

interface IPedidoEntregaView: IView {
  val tabEntregaImprimir: IPedidoEntregaImprimir
  val tabEntregaImpressoSemNota: IPedidoEntregaImpressoSemNota
  val tabEntregaPendente: IPedidoEntregaPendente
  val tabEntregaImpressoComNota: IPedidoEntregaImpressoComNota
  val tabEntregador: IPedidoEntregador
  
  //
  fun showRelatorioPedidoMinuta(pedidos: List<Pedido>)
  
  fun showRelatorioPedido(pedidos: List<Pedido>)
}

