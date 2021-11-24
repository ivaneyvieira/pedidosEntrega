package br.com.astrosoft.pedido.view.ecommerceEntrega

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.SubWindowPDF
import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.framework.view.tabPanel
import br.com.astrosoft.pedido.model.beans.Pedido
import br.com.astrosoft.pedido.model.beans.UserSaci
import br.com.astrosoft.pedido.view.PedidoEntregaLayout
import br.com.astrosoft.pedido.view.reports.RelatorioPedido
import br.com.astrosoft.pedido.viewmodel.ecommerceEntrega.IPedidoECommerceEView
import br.com.astrosoft.pedido.viewmodel.ecommerceEntrega.PedidoECommerceEViewModel
import com.github.mvysny.karibudsl.v10.tabSheet
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = PedidoEntregaLayout::class, value = "ecommerceE")
@PageTitle("E-Commerce")
class PedidoECommerceEView : ViewLayout<PedidoECommerceEViewModel>(), IPedidoECommerceEView {
  override val viewModel: PedidoECommerceEViewModel = PedidoECommerceEViewModel(this)
  override val tabECommerceEImprimir = TabECommerceEImprimir(
    viewModel.tabECommerceEImprimirViewModel
                                                            )
  override val tabECommerceEImpressoSemNota =
    TabECommerceEImpressoSemNota(viewModel.tabECommerceEImpressoSemNotaViewModel)

  //override val tabECommercePendente = TabECommercePendente(viewModel
  // .tabECommercePendenteViewModel)
  override val tabECommerceEImpressoComNota =
    TabECommerceEImpressoComNota(viewModel.tabECommerceEImpressoComNotaViewModel)
  override val tabECommerceEDesempenho = TabECommerceEDesempenho(
    viewModel.tabECommerceEDesempenhoViewModel
                                                               )

  init {
    tabSheet {
      val user = AppConfig.userSaci
      setSizeFull()
      if (user?.ecommerceE_imprimir == true) tabPanel(tabECommerceEImprimir)
      if (user?.ecommerceE_impressoSemNota == true) tabPanel(tabECommerceEImpressoSemNota)
      if (user?.ecommerceE_impressoComNota == true) tabPanel(tabECommerceEImpressoComNota)
      if (user?.ecommerceE_entregador == true) tabPanel(tabECommerceEDesempenho)
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

