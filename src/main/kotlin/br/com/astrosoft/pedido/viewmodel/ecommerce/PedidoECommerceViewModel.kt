package br.com.astrosoft.pedido.viewmodel.ecommerce

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.pedido.model.beans.Pedido


class PedidoECommerceViewModel(view: IPedidoECommerceView): ViewModel<IPedidoECommerceView>(view) {
  val tabECommerceImprimirViewModel = PedidoECommerceImprimirViewModel(this)
  val tabECommerceImpressoSemNotaViewModel = PedidoECommerceImpressoSemNotaViewModel(this)
  val tabECommercePendenteViewModel = PedidoECommercePendenteViewModel(this)
  val tabECommerceImpressoComNotaViewModel = PedidoECommerceImpressoComNotaViewModel(this)
  val tabECommerceDesempenhoViewModel = PedidoECommerceDesempenhoViewModel(this)
}

interface IPedidoECommerceView: IView {
  val tabECommerceImprimir: IPedidoECommerceImprimir
  val tabECommerceImpressoSemNota: IPedidoECommerceImpressoSemNota
  val tabECommercePendente: IPedidoECommercePendente
  val tabECommerceImpressoComNota: IPedidoECommerceImpressoComNota
  val tabECommerceDesempenho: IPedidoECommerceDesempenho
  
  //
  fun showRelatorioPedidoMinuta(pedidos: List<Pedido>)
  
  fun showRelatorioPedido(pedidos: List<Pedido>)
}

