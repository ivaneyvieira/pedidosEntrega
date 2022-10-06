package br.com.astrosoft.pedido.view.entrega

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnSeq
import br.com.astrosoft.framework.view.shiftSelect
import br.com.astrosoft.pedido.model.beans.Pedido
import br.com.astrosoft.pedido.view.*
import br.com.astrosoft.pedido.viewmodel.entrega.IPedidoEntregaImpressoSemNota
import br.com.astrosoft.pedido.viewmodel.entrega.PedidoEntregaImpressoSemNotaViewModel
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon.CLOSE
import com.vaadin.flow.component.icon.VaadinIcon.EYE
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode

class TabEntregaImpressoSemNota(val viewModel: PedidoEntregaImpressoSemNotaViewModel) : TabPanelGrid<Pedido>(),
        IPedidoEntregaImpressoSemNota {
  private lateinit var edtPedidoPesquisa: TextField
  override val label = "Impresso sem Nota"

  override fun updateComponent() {
    viewModel.updateGridImpressoSemNota()
  }

  override val pedidoPesquisa: String
    get() = edtPedidoPesquisa.value ?: ""

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
    edtPedidoPesquisa = textField("Pesquisa") {
      this.valueChangeMode = ValueChangeMode.LAZY
      this.valueChangeTimeout = 1500
      this.isAutofocus = true
      addValueChangeListener {
        updateComponent()
      }
    }
  }

  override fun Grid<Pedido>.gridPanel() {
    setSelectionMode(Grid.SelectionMode.MULTI)

    addColumnSeq("Seq")

    pedidoTipoECommerce()
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
    pedidoLoc()
    pedidoPiso()

    pedidoVendno()
    pedidoFrete()
    pedidoValor()
    pedidoObs()
    pedidoNfEnt()
    pedidoDataEnt()
    pedidoHoraEnt()

    shiftSelect()
  }
}