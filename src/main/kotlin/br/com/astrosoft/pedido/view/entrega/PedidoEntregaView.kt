package br.com.astrosoft.pedido.view.entrega

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.SubWindowPDF
import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.framework.view.tabPanel
import br.com.astrosoft.pedido.model.beans.Pedido
import br.com.astrosoft.pedido.view.PedidoEntregaLayout
import br.com.astrosoft.pedido.view.reports.RelatorioPedido
import br.com.astrosoft.pedido.viewmodel.entrega.IPedidoEntregaView
import br.com.astrosoft.pedido.viewmodel.entrega.PedidoEntregaViewModel
import com.github.mvysny.karibudsl.v10.tabSheet
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = PedidoEntregaLayout::class, value = "entrega")
@PageTitle("Entrega")
class PedidoEntregaView : ViewLayout<PedidoEntregaViewModel>(), IPedidoEntregaView {
  override val viewModel: PedidoEntregaViewModel = PedidoEntregaViewModel(this)
  override val tabEntregaImprimir = TabEntregaImprimir(viewModel.tabEntregaImprimirViewModel)
  override val tabEntregaImpressoSemNota = TabEntregaImpressoSemNota(viewModel.tabEntregaImpressoSemNotaViewModel)
  override val tabEntregaPendente = TabEntregaPendente(viewModel.tabEntregaPendenteViewModel)
  override val tabEntregaImpressoComNota = TabEntregaImpressoComNota(viewModel.tabEntregaImpressoComNotaViewModel)
  override val tabEntregaImpressoSeparar = TabEntregaImpressoSeparar(viewModel.tabEntregaImpressoSepararViewModel)
  override val tabEntregaImpressoCarga = TabEntregaImpressoCarga(viewModel.tabEntregaImpressoCargaViewModel)
  override val tabEntregaImpressoSeparado = TabEntregaImpressoSeparado(viewModel.tabEntregaImpressoSeparadoViewModel)
  override val tabEntregador = TabEntregador(viewModel.tabEntregadorViewModel)
  override val tabEntregaRota = TabEntregaRota(viewModel.tabRotaViewModel)

  init {
    tabSheet {
      val user = AppConfig.userSaci
      setSizeFull()
      var update = true
      if (user?.entrega_imprimir == true) {
        tabPanel(tabEntregaImprimir, update)
        update = false
      }
      if (user?.entrega_impressoSemNota == true) {
        tabPanel(tabEntregaImpressoSemNota, update)
        update = false
      }
      if (user?.entrega_pendente == true) {
        tabPanel(tabEntregaPendente, update)
        update = false
      }
      if (user?.entrega_impressoComNota == true) {
        tabPanel(tabEntregaImpressoComNota, update)
        update = false
      }
      if (user?.entrega_entregador == true) {
        tabPanel(tabEntregador, update)
        update = false
      }

      if (user?.entrega_rota == true) {
        tabPanel(tabEntregaRota, update)
        update = false
      }

      if (user?.entrega_carga == true) {
        tabPanel(tabEntregaImpressoCarga, update)
        update = false
      }

      if (user?.entrega_separar == true) {
        tabPanel(tabEntregaImpressoSeparar, update)
        update = false
      }

      if (user?.entrega_separado == true) {
        tabPanel(tabEntregaImpressoSeparado, update)
      }
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

