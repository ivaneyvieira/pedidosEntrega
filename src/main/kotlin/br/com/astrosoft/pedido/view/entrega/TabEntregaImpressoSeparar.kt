package br.com.astrosoft.pedido.view.entrega

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnSeq
import br.com.astrosoft.framework.view.shiftSelect
import br.com.astrosoft.pedido.model.beans.Pedido
import br.com.astrosoft.pedido.view.*
import br.com.astrosoft.pedido.viewmodel.entrega.IPedidoEntregaImpressoSeparar
import br.com.astrosoft.pedido.viewmodel.entrega.PedidoEntregaImpressoSepararViewModel
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon.*
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import org.claspina.confirmdialog.ButtonOption
import org.claspina.confirmdialog.ConfirmDialog

class TabEntregaImpressoSeparar(val viewModel: PedidoEntregaImpressoSepararViewModel) : TabPanelGrid<Pedido>(),
        IPedidoEntregaImpressoSeparar {
  private lateinit var edtPedidoPesquisa: TextField
  override val label = "Separar"

  override fun updateComponent() {
    viewModel.updateGridImpressoSeparar()
  }

  override val pedidoPesquisa: String
    get() = edtPedidoPesquisa.value ?: ""

  override fun confirmaSeparado(exec: () -> Unit) {
    ConfirmDialog
      .createQuestion()
      .withCaption("Confirmação")
      .withMessage("Marca como separado?")
      .withYesButton({
                       exec()
                     },
                     ButtonOption.caption("Sim"))
      .withNoButton({ }, ButtonOption.caption("Não"))
      .open()
  }

  override fun confirmaRemoveCarga(exec: () -> Unit) {
    ConfirmDialog
      .createQuestion()
      .withCaption("Confirmação")
      .withMessage("Remove a carga?")
      .withYesButton({
                       exec()
                     },
                     ButtonOption.caption("Sim"))
      .withNoButton({ }, ButtonOption.caption("Não"))
      .open()
  }

  override fun classPanel() = Pedido::class

  override fun HorizontalLayout.toolBarConfig() {
    if (AppConfig.isAdmin) button("Desmarcar") {
      icon = CLOSE.create()
      addClickListener {
        viewModel.desmarcaSeparar()
      }
    }

    if (AppConfig.isAdmin || (AppConfig.userSaci?.entrega_visualizar == true)) {
      button("Visualizar") {
        icon = EYE.create()
        addClickListener {
          viewModel.imprimirPedidos(itensSelecionado())
        }
      }
    }

    if (AppConfig.isAdmin || (AppConfig.userSaci?.entrega_visualizar == true)) {
      button("Imprimrir") {
        icon = PRINT.create()
        addClickListener {
          viewModel.printRelatorio(itensSelecionado())
        }
      }
    }


    if (AppConfig.isAdmin || (AppConfig.userSaci?.entrega_removerCarga == true)) {
      button("Remove Carga") {
        icon = ARROW_LEFT.create()
        addClickListener {
          viewModel.removeCarga()
        }
      }
    }

    if (AppConfig.isAdmin || (AppConfig.userSaci?.entrega_enviarSeparado == true)) {
      button("Separado") {
        icon = ARROW_RIGHT.create()
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
    pedidoArea()
    pedidoCarga()
    pedidoEntrega()

    pedidoNfFat()
    pedidoDataFat()
    pedidoHoraFat()
    pedidoLoc()
    pedidoPiso()

    pedidoVendno()
    pedidoValor()
    pedidoObs()
    pedidoNfEnt()
    pedidoDataEnt()
    pedidoHoraEnt()

    shiftSelect()
  }
}