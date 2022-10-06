package br.com.astrosoft.pedido.viewmodel.ecommerceRetira

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.pedido.model.beans.Pedido

class PedidoECommerceRViewModel(view: IPedidoECommerceRView) : ViewModel<IPedidoECommerceRView>(view) {
  val tabECommerceRImprimirViewModel = PedidoECommerceRImprimirViewModel(this)
  val tabECommerceRImpressoSemNotaViewModel = PedidoECommerceRImpressoSemNotaViewModel(this)

  val tabECommerceRImpressoComNotaViewModel = PedidoECommerceRImpressoComNotaViewModel(this)
  val tabECommerceRDesempenhoViewModel = PedidoECommerceRDesempenhoViewModel(this)
}

interface IPedidoECommerceRView : IView {
  val tabECommerceRImprimir: IPedidoECommerceRImprimir
  val tabECommerceRImpressoSemNota: IPedidoECommerceRImpressoSemNota

  val tabECommerceRImpressoComNota: IPedidoECommerceRImpressoComNota
  val tabECommerceRDesempenho: IPedidoECommerceRDesempenho

  //
  fun showRelatorioPedidoMinuta(pedidos: List<Pedido>)

  fun showRelatorioPedido(pedidos: List<Pedido>)
}

