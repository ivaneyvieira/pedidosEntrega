package br.com.astrosoft.pedido.view.entrega

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.addColumnSeq
import br.com.astrosoft.framework.view.shiftSelect
import br.com.astrosoft.pedido.model.beans.Pedido
import br.com.astrosoft.pedido.model.beans.PedidoGroup
import br.com.astrosoft.pedido.view.*
import br.com.astrosoft.pedido.viewmodel.entrega.PedidoEntregaImpressoSepararViewModel
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode

class DlgPedidoSeparar(val viewModel: PedidoEntregaImpressoSepararViewModel, val grupo : PedidoGroup) {
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
    if (AppConfig.isAdmin) button("Desmarcar") {
      icon = VaadinIcon.CLOSE.create()
      addClickListener {
        viewModel.desmarcaSeparar()
      }
    }

    if (AppConfig.isAdmin || (AppConfig.userSaci?.entrega_visualizar == true)) {
      button("Visualizar") {
        icon = VaadinIcon.EYE.create()
        addClickListener {
          viewModel.imprimirPedidos(gridDetail.selectedItems.orEmpty().toList())
        }
      }
    }

    if (AppConfig.isAdmin || (AppConfig.userSaci?.entrega_imprimir_termica == true)) {
      button("Imprimrir") {
        icon = VaadinIcon.PRINT.create()
        addClickListener {
          viewModel.printRelatorio(gridDetail.selectedItems.orEmpty().toList())
        }
      }
    }


    if (AppConfig.isAdmin || (AppConfig.userSaci?.entrega_removerCarga == true)) {
      button("Remove Carga") {
        icon = VaadinIcon.ARROW_LEFT.create()
        addClickListener {
          viewModel.removeCarga()
        }
      }
    }

    if (AppConfig.isAdmin || (AppConfig.userSaci?.entrega_enviarSeparado == true)) {
      button("Separado") {
        icon = VaadinIcon.ARROW_RIGHT.create()
        addClickListener {
          viewModel.marcaSeparado()
        }
      }
    }

    edtPedidoPesquisa = textField("Pesquisa") {
      this.valueChangeMode = ValueChangeMode.LAZY
      this.valueChangeTimeout = 1000
      this.isAutofocus = true
      addValueChangeListener {
        viewModel.updateGridImpressoSeparar()
      }
    }
  }

  fun Grid<Pedido>.gridPanel() {
    setSizeFull()
    addThemeVariants(GridVariant.LUMO_COMPACT)
    isMultiSort = false
    setSelectionMode(Grid.SelectionMode.MULTI)

    addColumnSeq("Seq")

    setSelectionMode(Grid.SelectionMode.MULTI)

    addColumnSeq("Seq")

    pedidoTipoECommerce()
    pedidoLoja()
    pedidoPedido()
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