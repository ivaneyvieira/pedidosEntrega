package br.com.astrosoft.pedido.viewmodel.ecommerceEntrega

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.pedido.model.beans.Pedido

class PedidoECommerceEViewModel(view: IPedidoECommerceEView) : ViewModel<IPedidoECommerceEView>
                                                             (view) {
  val tabECommerceEImprimirViewModel = PedidoECommerceEImprimirViewModel(this)
  val tabECommerceEImpressoSemNotaViewModel = PedidoECommerceEImpressoSemNotaViewModel(this)

  //  val tabECommercePendenteViewModel = PedidoECommercePendenteViewModel(this)
  val tabECommerceEImpressoComNotaViewModel = PedidoECommerceEImpressoComNotaViewModel(this)
  val tabECommerceEDesempenhoViewModel = PedidoECommerceEDesempenhoViewModel(this)
}

interface IPedidoECommerceEView : IView {
  val tabECommerceEImprimir: IPedidoECommerceEImprimir
  val tabECommerceEImpressoSemNota: IPedidoECommerceEImpressoSemNota

  //  val tabECommercePendente: IPedidoECommercePendente
  val tabECommerceEImpressoComNota: IPedidoECommerceEImpressoComNota
  val tabECommerceEDesempenho: IPedidoECommerceEDesempenho

  //
  fun showRelatorioPedidoMinuta(pedidos: List<Pedido>)

  fun showRelatorioPedido(pedidos: List<Pedido>)
}

