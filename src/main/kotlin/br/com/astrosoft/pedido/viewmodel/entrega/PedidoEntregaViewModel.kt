package br.com.astrosoft.pedido.viewmodel.entrega

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.pedido.model.beans.Pedido

class PedidoEntregaViewModel(view: IPedidoEntregaView) : ViewModel<IPedidoEntregaView>(view) {
  val tabEntregaImprimirViewModel = PedidoEntregaImprimirViewModel(this)
  val tabEntregaImpressoSemNotaViewModel = PedidoEntregaImpressoSemNotaViewModel(this)
  val tabEntregaPendenteViewModel = PedidoEntregaPendenteViewModel(this)
  val tabEntregaImpressoSepararViewModel = PedidoEntregaImpressoSepararViewModel(this)
  val tabEntregaImpressoComNotaViewModel = PedidoEntregaImpressoComNotaViewModel(this)
  val tabEntregadorViewModel = PedidoEntregadorViewModel(this)
  val tabRotaViewModel = PedidoEntregaRotaViewModel(this)
}

interface IPedidoEntregaView : IView {
  val tabEntregaImprimir: IPedidoEntregaImprimir
  val tabEntregaImpressoSemNota: IPedidoEntregaImpressoSemNota
  val tabEntregaPendente: IPedidoEntregaPendente
  val tabEntregaImpressoComNota: IPedidoEntregaImpressoComNota
  val tabEntregaImpressoSeparar: IPedidoEntregaImpressoSeparar
  val tabEntregador: IPedidoEntregador
  val tabEntregaRota: IPedidoEntregaRota

  //
  fun showRelatorioPedidoMinuta(pedidos: List<Pedido>)

  fun showRelatorioPedido(pedidos: List<Pedido>)
}

