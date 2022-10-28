package br.com.astrosoft.pedido.view.entrega

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.addColumnSeq
import br.com.astrosoft.framework.view.shiftSelect
import br.com.astrosoft.pedido.model.beans.Pedido
import br.com.astrosoft.pedido.model.beans.PedidoGroup
import br.com.astrosoft.pedido.view.*
import br.com.astrosoft.pedido.viewmodel.entrega.PedidoEntregaImpressoSeparadoViewModel
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import org.claspina.confirmdialog.ButtonOption
import org.claspina.confirmdialog.ConfirmDialog

class DlgPedidoSeparado(val viewModel: PedidoEntregaImpressoSeparadoViewModel, val grupo : PedidoGroup) {
  private var form: SubWindowForm? = null
  val gridDetail = Grid(Pedido::class.java, false)

  fun showDialog() {
    form = SubWindowForm("Pedidos", toolBar = {
      this.toolBarConfig()
    }) {
      HorizontalLayout().apply {
        setSizeFull()
        gridDetail.apply {
          this.setItems(grupo.list)
          this.gridPanel()
        }
        addAndExpand(gridDetail)
      }
    }
    form?.open()
  }

  private lateinit var edtPedidoPesquisa: TextField
  val pedidoPesquisa: String
    get() = edtPedidoPesquisa.value ?: ""

  fun HasComponents.toolBarConfig() {
    if (AppConfig.isAdmin || (AppConfig.userSaci?.entrega_carga_separado_visualizar == true)){
      button("Visualizar") {
        icon = VaadinIcon.EYE.create()
        addClickListener {
          viewModel.imprimirPedidos(gridDetail.selectedItems.orEmpty().toList())
        }
      }
    }

    if (AppConfig.isAdmin || (AppConfig.userSaci?.entrega_voltaSeparar == true)) {
      button("Volta") {
        icon = VaadinIcon.ARROW_LEFT.create()
        addClickListener {
          viewModel.desmarcaSeparado()
        }
      }
    }

    edtPedidoPesquisa = textField("Pesquisa") {
      this.valueChangeMode = ValueChangeMode.LAZY
      this.valueChangeTimeout = 1500
      this.isAutofocus = true
      addValueChangeListener {
        viewModel.updateGridImpressoSeparado()
      }
    }
  }

  fun Grid<Pedido>.gridPanel() {
    setSizeFull()
    addThemeVariants(GridVariant.LUMO_COMPACT)
    isMultiSort = false
    setSelectionMode(Grid.SelectionMode.MULTI)

    addColumnSeq("Seq")

    pedidoTipoECommerce()
    pedidoLoja()
    pedidoPedido()
    pedidoRota()
    pedidoCarga()
    pedidoEntrega()

    pedidoNfFat()
    pedidoDataFat()
    pedidoLoc()
    pedidoPiso()

    pedidoValor()
    pedidoNfEnt()
    pedidoDataEnt()
    pedidoObs()

    shiftSelect()
  }
}