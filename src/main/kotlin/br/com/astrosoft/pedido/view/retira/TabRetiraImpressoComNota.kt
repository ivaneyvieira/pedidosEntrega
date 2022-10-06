package br.com.astrosoft.pedido.view.retira

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.shiftSelect
import br.com.astrosoft.pedido.model.beans.Pedido
import br.com.astrosoft.pedido.view.*
import br.com.astrosoft.pedido.viewmodel.retira.IPedidoRetiraImpressoComNota
import br.com.astrosoft.pedido.viewmodel.retira.PedidoRetiraImpressoComNotaViewModel
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode
import com.vaadin.flow.component.icon.VaadinIcon.CLOSE
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode.TIMEOUT

class TabRetiraImpressoComNota(val viewModel: PedidoRetiraImpressoComNotaViewModel) : TabPanelGrid<Pedido>(),
        IPedidoRetiraImpressoComNota {
  private lateinit var edtPedidoImpressoComNota: TextField
  override val label = "Editor de nota"

  override fun updateComponent() {
    viewModel.updateGridImpressoComNota()
  }

  override val pedidoImpressoComNota: Int
    get() = edtPedidoImpressoComNota.value?.toIntOrNull() ?: 0

  override fun classPanel() = Pedido::class

  override fun HorizontalLayout.toolBarConfig() {
    if (AppConfig.isAdmin) button("Desmarcar") {
      icon = CLOSE.create()
      addClickListener {
        viewModel.desmarcaComNota()
      }
    }

    edtPedidoImpressoComNota = textField("Número Pedido") {
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
    pedidoObs()
    pedidoUsername()
    shiftSelect()
  }
}