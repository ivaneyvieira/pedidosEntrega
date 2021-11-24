package br.com.astrosoft.pedido.view.ecommerceRetira

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.SubWindowPDF
import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.framework.view.tabPanel
import br.com.astrosoft.pedido.model.beans.Pedido
import br.com.astrosoft.pedido.view.PedidoEntregaLayout
import br.com.astrosoft.pedido.view.reports.RelatorioPedido
import br.com.astrosoft.pedido.viewmodel.ecommerceRetira.IPedidoECommerceRView
import br.com.astrosoft.pedido.viewmodel.ecommerceRetira.PedidoECommerceRViewModel
import com.github.mvysny.karibudsl.v10.tabSheet
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = PedidoEntregaLayout::class, value = "ecommerceR")
@PageTitle("E-Commerce")
class PedidoECommerceRView : ViewLayout<PedidoECommerceRViewModel>(), IPedidoECommerceRView {
  override val viewModel: PedidoECommerceRViewModel = PedidoECommerceRViewModel(this)
  override val tabECommerceRImprimir = TabECommerceRImprimir(viewModel.tabECommerceRImprimirViewModel)
  override val tabECommerceRImpressoSemNota =
          TabECommerceRImpressoSemNota(viewModel.tabECommerceRImpressoSemNotaViewModel)

  //override val tabECommercePendente = TabECommercePendente(viewModel
  // .tabECommercePendenteViewModel)
  override val tabECommerceRImpressoComNota =
          TabECommerceRImpressoComNota(viewModel.tabECommerceRImpressoComNotaViewModel)
  override val tabECommerceRDesempenho = TabECommerceRDesempenho(viewModel.tabECommerceRDesempenhoViewModel)

  init {
    tabSheet {
      val user = AppConfig.userSaci
      setSizeFull()
      if (user?.ecommerceR_imprimir == true) tabPanel(tabECommerceRImprimir)
      if (user?.ecommerceR_impressoSemNota == true) tabPanel(tabECommerceRImpressoSemNota)
      if (user?.ecommerceR_impressoComNota == true) tabPanel(tabECommerceRImpressoComNota)
    }
  }

  override fun showRelatorioPedidoMinuta(pedidos: List<Pedido>) {
    val byteArray = RelatorioPedido.processaPedidosMinutaCompacta(pedidos)
    showRelatorio(byteArray)
  }

  override fun showRelatorioPedido(pedidos: List<Pedido>) {
    val byteArray = RelatorioPedido.processaPedidosMinutaCompacta(pedidos)
    showRelatorio(byteArray)
  }

  private fun showRelatorio(byteArray: ByteArray) {
    val chave = "PedidoEntrega"
    SubWindowPDF(chave, byteArray).open()
  }
}

