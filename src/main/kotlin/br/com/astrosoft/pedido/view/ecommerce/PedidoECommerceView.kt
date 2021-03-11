package br.com.astrosoft.pedido.view.ecommerce

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.SubWindowPDF
import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.framework.view.tabPanel
import br.com.astrosoft.pedido.model.beans.Pedido
import br.com.astrosoft.pedido.model.beans.UserSaci
import br.com.astrosoft.pedido.view.PedidoEntregaLayout
import br.com.astrosoft.pedido.view.reports.RelatorioPedido
import br.com.astrosoft.pedido.viewmodel.ecommerce.IPedidoECommerceView
import br.com.astrosoft.pedido.viewmodel.ecommerce.PedidoECommerceViewModel
import com.github.mvysny.karibudsl.v10.tabSheet
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = PedidoEntregaLayout::class, value = "ecommerce")
@PageTitle("E-Commerce")
class PedidoECommerceView : ViewLayout<PedidoECommerceViewModel>(), IPedidoECommerceView {
  override val viewModel: PedidoECommerceViewModel = PedidoECommerceViewModel(this)
  override val tabECommerceImprimir = TabECommerceImprimir(viewModel.tabECommerceImprimirViewModel)
  override val tabECommerceImpressoSemNota =
    TabECommerceImpressoSemNota(viewModel.tabECommerceImpressoSemNotaViewModel)
  override val tabECommercePendente = TabECommercePendente(viewModel.tabECommercePendenteViewModel)
  override val tabECommerceImpressoComNota =
    TabECommerceImpressoComNota(viewModel.tabECommerceImpressoComNotaViewModel)
  override val tabECommerceDesempenho = TabECommerceDesempenho(
    viewModel.tabECommerceDesempenhoViewModel
                                                              )

  override fun isAccept(user: UserSaci) = true

  init {
    tabSheet {
      val user = AppConfig.userSaci
      setSizeFull()
      if (user?.admin == true) tabPanel(tabECommerceImprimir)
      if (user?.admin == true) tabPanel(tabECommerceImpressoSemNota)
      if (user?.admin == true) tabPanel(tabECommercePendente)
      if (user?.admin == true) tabPanel(tabECommerceImpressoComNota)
      if (user?.admin == true) tabPanel(tabECommerceDesempenho)
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

