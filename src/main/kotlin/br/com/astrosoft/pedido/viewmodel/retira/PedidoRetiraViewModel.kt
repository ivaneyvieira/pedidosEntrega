package br.com.astrosoft.pedido.viewmodel.retira

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.pedido.model.beans.Pedido

class PedidoRetiraViewModel(view: IPedidoRetiraView) : ViewModel<IPedidoRetiraView>(view) {
  val tabRetiraImprimirViewModel = PedidoRetiraImprimirViewModel(this)
  val tabRetiraImpressoSemNotaViewModel = PedidoRetiraImpressoSemNotaViewModel(this)
  val tabRetiraPendenteViewModel = PedidoRetiraPendenteViewModel(this)
  val tabRetiraImpressoComNotaViewModel = PedidoRetiraImpressoComNotaViewModel(this)
}

interface IPedidoRetiraView : IView {
  val tabRetiraImprimir: IPedidoRetiraImprimir
  val tabRetiraImpressoSemNota: IPedidoRetiraImpressoSemNota
  val tabRetiraPendente: IPedidoRetiraPendente
  val tabRetiraImpressoComNota: IPedidoRetiraImpressoComNota

  //
  fun showRelatorioPedidoMinuta(pedidos: List<Pedido>)

  fun showRelatorioPedido(pedidos: List<Pedido>)
}