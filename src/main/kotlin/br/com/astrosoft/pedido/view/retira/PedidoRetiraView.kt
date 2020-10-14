package br.com.astrosoft.pedido.view.retira

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.SubWindowPDF
import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.framework.view.tabPanel
import br.com.astrosoft.pedido.model.beans.Pedido
import br.com.astrosoft.pedido.model.beans.UserSaci
import br.com.astrosoft.pedido.view.PedidoEntregaLayout
import br.com.astrosoft.pedido.view.reports.RelatorioPedido
import br.com.astrosoft.pedido.viewmodel.retira.IPedidoRetiraView
import br.com.astrosoft.pedido.viewmodel.retira.PedidoRetiraViewModel
import com.github.mvysny.karibudsl.v10.tabSheet
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route( layout = PedidoEntregaLayout::class)
@PageTitle("Retira")
class PedidoRetiraView: ViewLayout<PedidoRetiraViewModel>(), IPedidoRetiraView {
  override val viewModel: PedidoRetiraViewModel = PedidoRetiraViewModel(this)
  override val tabRetiraImprimir = TabRetiraImprimir(viewModel.tabRetiraImprimirViewModel)
  override val tabRetiraImpressoSemNota = TabRetiraImpressoSemNota(viewModel.tabRetiraImpressoSemNotaViewModel)
  override val tabRetiraPendente = TabRetiraPendente(viewModel.tabRetiraPendenteViewModel)
  override val tabRetiraImpressoComNota = TabRetiraImpressoComNota(viewModel.tabRetiraImpressoComNotaViewModel)
  
  override fun isAccept(user: UserSaci) = true
  
  init {
   tabSheet {
      val user = AppConfig.userSaci
      setSizeFull()
      if(user?.retira_imprimir == true)
        tabPanel(tabRetiraImprimir)
      if(user?.retira_impressoSemNota == true)
        tabPanel(tabRetiraImpressoSemNota)
      if(user?.retira_pendente == true)
        tabPanel(tabRetiraPendente)
      if(user?.retira_impressoComNota == true)
        tabPanel(tabRetiraImpressoComNota)
    }
    
  }
  
  override fun showRelatorioPedidoMinuta(pedidos: List<Pedido>) {
    val byteArray = RelatorioPedido.processaPedidosMinutaCompacta(pedidos)
    showRelatorio(byteArray)
  }
  
  override fun showRelatorioPedido(pedidos: List<Pedido>) {
    val byteArray = RelatorioPedido.processaPedidos(pedidos)
    showRelatorio(byteArray)
  }
  
  private fun showRelatorio(byteArray: ByteArray) {
    val chave = "PedidoRetira"
    SubWindowPDF(chave, byteArray).open()
  }
}

