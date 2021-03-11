package br.com.astrosoft.pedido.view.ecommerce

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.localePtBr
import br.com.astrosoft.framework.view.shiftSelect
import br.com.astrosoft.pedido.model.beans.Pedido
import br.com.astrosoft.pedido.view.*
import br.com.astrosoft.pedido.viewmodel.ecommerce.IPedidoECommerceImprimir
import br.com.astrosoft.pedido.viewmodel.ecommerce.PedidoECommerceImprimirViewModel
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.datePicker
import com.github.mvysny.karibudsl.v10.getColumnBy
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.icon.VaadinIcon.*
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.provider.SortDirection.ASCENDING
import com.vaadin.flow.data.provider.SortDirection.DESCENDING
import com.vaadin.flow.data.value.ValueChangeMode.TIMEOUT
import java.time.LocalDate

class TabECommerceImprimir(val viewModel: PedidoECommerceImprimirViewModel): TabPanelGrid<Pedido>(),
                                                                             IPedidoECommerceImprimir {
  private lateinit var edtPedidoImprimir: TextField
  private lateinit var edtRotaImprimir: TextField
  private lateinit var edtAreaImprimir: TextField
  private lateinit var edtDataImprimir: DatePicker
  override val label: String = "Imprimir"
  
  override fun updateComponent() {
    viewModel.updateGridImprimir()
  }
  
  override val pedidoImprimir: Int
    get() = edtPedidoImprimir.value?.toIntOrNull() ?: 0
  override val dataImprimir: LocalDate?
    get() = edtDataImprimir.value
  override val areaImprimir: String
    get() = edtAreaImprimir.value?.toUpperCase() ?: ""
  override val rotaImprimir: String
    get() = edtRotaImprimir.value?.toUpperCase() ?: ""
  
  override fun classPanel() = Pedido::class
  
  override fun HorizontalLayout.toolBarConfig() {
    button("Imprimir") {
      icon = PRINT.create()
      addClickListener {
        viewModel.imprimirPedidoMinuta()
      }
    }
    if(AppConfig.isAdmin)
      button("Visualizar") {
        icon = EYE.create()
        addClickListener {
          viewModel.imprimirPedidos(itensSelecionado())
        }
      }
    
    button("Confirma") {
      icon = THUMBS_UP.create()
      addClickListener {
        viewModel.confirmaPrint()
      }
    }
    
    edtPedidoImprimir = textField("Numero Pedido") {
      this.valueChangeMode = TIMEOUT
      this.isAutofocus = true
      addValueChangeListener {
        viewModel.updateGridImprimir()
      }
    }
    edtDataImprimir = datePicker("Data") {
      localePtBr()
      setClearButtonVisible(true)
      addValueChangeListener {
        viewModel.updateGridImprimir()
      }
    }
    edtAreaImprimir = textField("Área") {
      this.valueChangeMode = TIMEOUT
      addValueChangeListener {
        viewModel.updateGridImprimir()
      }
    }
    edtRotaImprimir = textField("Rota") {
      this.valueChangeMode = TIMEOUT
      
      addValueChangeListener {
        viewModel.updateGridImprimir()
      }
    }
  }
  
  override fun Grid<Pedido>.gridPanel() {
    setSelectionMode(SelectionMode.MULTI)
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
    pedidoNfEnt()
    pedidoDataEnt()
    pedidoHoraEnt()
    
    pedidoVendno()
    pedidoFrete()
    pedidoValor()
    pedidoCustno()
    pedidoObs()
    pedidoUsername()
    this.shiftSelect()
    this.sort(listOf(
      GridSortOrder(getColumnBy(Pedido::loja), ASCENDING),
      GridSortOrder(getColumnBy(Pedido::pedido), DESCENDING))
             )
  }
}