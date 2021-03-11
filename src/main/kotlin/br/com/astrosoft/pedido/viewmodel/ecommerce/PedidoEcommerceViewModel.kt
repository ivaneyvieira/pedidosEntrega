package br.com.astrosoft.pedido.viewmodel.ecommerce

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.pedido.model.beans.Pedido


class PedidoEcommerceViewModel(view: IPedidoEcommerceView): ViewModel<IPedidoEcommerceView>(view) {
  val tabEcommerceImprimirViewModel = PedidoEcommerceImprimirViewModel(this)
  val tabEcommerceImpressoSemNotaViewModel = PedidoEcommerceImpressoSemNotaViewModel(this)
  val tabEcommercePendenteViewModel = PedidoEcomemrcePendenteViewModel(this)
  val tabEcommerceImpressoComNotaViewModel = PedidoEcommerceImpressoComNotaViewModel(this)
  val tabEcommerceDesempenhoViewModel = PedidoEcommerceDesempenhoViewModel(this)
}

interface IPedidoEcommerceView: IView {
  val tabEcommerceImprimir: IPedidoEcommerceImprimir
  val tabEcommerceImpressoSemNota: IPedidoEcommerceImpressoSemNota
  val tabEcommercePendente: IPedidoEcommercePendente
  val tabEcommerceImpressoComNota: IPedidoEcommerceImpressoComNota
  val tabEcommerceDesempenho: IPedidoEcommerceDesempenho
  
  //
  fun showRelatorioPedidoMinuta(pedidos: List<Pedido>)
  
  fun showRelatorioPedido(pedidos: List<Pedido>)
}

