package br.com.astrosoft.pedido.view.entrega

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.shiftSelect
import br.com.astrosoft.pedido.model.beans.Pedido
import br.com.astrosoft.pedido.view.pedidoArea
import br.com.astrosoft.pedido.view.pedidoCustno
import br.com.astrosoft.pedido.view.pedidoData
import br.com.astrosoft.pedido.view.pedidoDataEnt
import br.com.astrosoft.pedido.view.pedidoDataFat
import br.com.astrosoft.pedido.view.pedidoDataHoraPrint
import br.com.astrosoft.pedido.view.pedidoFrete
import br.com.astrosoft.pedido.view.pedidoHora
import br.com.astrosoft.pedido.view.pedidoHoraEnt
import br.com.astrosoft.pedido.view.pedidoHoraFat
import br.com.astrosoft.pedido.view.pedidoLoja
import br.com.astrosoft.pedido.view.pedidoNfEnt
import br.com.astrosoft.pedido.view.pedidoNfFat
import br.com.astrosoft.pedido.view.pedidoNum
import br.com.astrosoft.pedido.view.pedidoObs
import br.com.astrosoft.pedido.view.pedidoPedido
import br.com.astrosoft.pedido.view.pedidoRota
import br.com.astrosoft.pedido.view.pedidoUsername
import br.com.astrosoft.pedido.view.pedidoValor
import br.com.astrosoft.pedido.view.pedidoVendno
import br.com.astrosoft.pedido.viewmodel.entrega.IPedidoEntregaImpressoSemNota
import br.com.astrosoft.pedido.viewmodel.entrega.PedidoEntregaImpressoSemNotaViewModel
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon.CLOSE
import com.vaadin.flow.component.icon.VaadinIcon.EYE
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode.TIMEOUT

class TabEntregaImpressoSemNota(val viewModel: PedidoEntregaImpressoSemNotaViewModel): TabPanelGrid<Pedido>(),
                                                                                       IPedidoEntregaImpressoSemNota {
  private lateinit var edtPedidoImpressoSemNota: TextField
  override val label = "Impresso sem Nota"
  
  override fun updateComponent() {
    viewModel.updateGridImpressoSemNota()
  }
  
  override val pedidoImpressoSemNota: Int
    get() = edtPedidoImpressoSemNota.value?.toIntOrNull() ?: 0
  
  override fun classPanel() = Pedido::class
  
  override fun HorizontalLayout.toolBarConfig() {
    if(AppConfig.isAdmin)
      button("Desmarcar") {
        icon = CLOSE.create()
        addClickListener {
          viewModel.desmarcaSemNota()
        }
      }
    if(AppConfig.isAdmin)
      button("Visualizar") {
        icon = EYE.create()
        addClickListener {
          viewModel.imprimirPedidos(itensSelecionado())
        }
      }
    edtPedidoImpressoSemNota = textField("Numero Pedido") {
      this.valueChangeMode = TIMEOUT
      this.isAutofocus = true
      addValueChangeListener {
        updateComponent()
      }
    }
  }
  
  override fun Grid<Pedido>.gridPanel() {
    pedidoNum()
    pedidoLoja()
    pedidoPedido()
    pedidoDataHoraPrint()
    pedidoData()
    pedidoHora()
    pedidoArea()
    pedidoRota()
    
    pedidoNfFat()
    pedidoDataFat()
    pedidoHoraFat()
    
    pedidoVendno()
    pedidoFrete()
    pedidoValor()
    pedidoCustno()
    pedidoObs()
    pedidoNfEnt()
    pedidoDataEnt()
    pedidoHoraEnt()
    
    pedidoUsername()
    shiftSelect()
  }
}