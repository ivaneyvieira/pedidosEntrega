package br.com.astrosoft.pedido.view.ecommerceEntrega

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.shiftSelect
import br.com.astrosoft.pedido.model.beans.Pedido
import br.com.astrosoft.pedido.view.*
import br.com.astrosoft.pedido.viewmodel.ecommerceEntrega.IPedidoECommerceEImpressoSemNota
import br.com.astrosoft.pedido.viewmodel.ecommerceEntrega.PedidoECommerceEImpressoSemNotaViewModel
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode
import com.vaadin.flow.component.icon.VaadinIcon.CLOSE
import com.vaadin.flow.component.icon.VaadinIcon.EYE
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode.TIMEOUT

class TabECommerceEImpressoSemNota(val viewModel: PedidoECommerceEImpressoSemNotaViewModel) : TabPanelGrid<Pedido>(),
        IPedidoECommerceEImpressoSemNota {
  private lateinit var edtPedidoImpressoSemNota: TextField
  override val label = "Impresso sem Nota"

  override fun updateComponent() {
    viewModel.updateGridImpressoSemNota()
  }

  override val pedidoImpressoSemNota: Int
    get() = edtPedidoImpressoSemNota.value?.toIntOrNull() ?: 0

  override fun classPanel() = Pedido::class

  override fun HorizontalLayout.toolBarConfig() {
    if (AppConfig.isAdmin) button("Desmarcar") {
      icon = CLOSE.create()
      addClickListener {
        viewModel.desmarcaSemNota()
      }
    }
    if (AppConfig.isAdmin) button("Visualizar") {
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
    setSelectionMode(SelectionMode.MULTI)
    pedidoLoja()
    pedidoPedido()
    pedidoTipo()
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
    pedidoObs()
    pedidoNfEnt()
    pedidoDataEnt()
    pedidoHoraEnt()

    pedidoUsername()
    shiftSelect()
  }
}