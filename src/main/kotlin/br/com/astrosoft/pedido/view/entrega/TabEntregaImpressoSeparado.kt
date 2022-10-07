package br.com.astrosoft.pedido.view.entrega

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnSeq
import br.com.astrosoft.framework.view.shiftSelect
import br.com.astrosoft.pedido.model.beans.Pedido
import br.com.astrosoft.pedido.view.*
import br.com.astrosoft.pedido.viewmodel.entrega.IPedidoEntregaImpressoSeparado
import br.com.astrosoft.pedido.viewmodel.entrega.PedidoEntregaImpressoSeparadoViewModel
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import org.claspina.confirmdialog.ButtonOption
import org.claspina.confirmdialog.ConfirmDialog

class TabEntregaImpressoSeparado(val viewModel: PedidoEntregaImpressoSeparadoViewModel) : TabPanelGrid<Pedido>(),
        IPedidoEntregaImpressoSeparado {
  private lateinit var edtPedidoPesquisa: TextField
  override val label = "Separado"

  override fun updateComponent() {
    viewModel.updateGridImpressoSeparado()
  }

  override val pedidoPesquisa: String
    get() = edtPedidoPesquisa.value ?: ""

  override fun confirmaVolta(exec: () -> Unit) {
    ConfirmDialog
      .createQuestion()
      .withCaption("Confirmação")
      .withMessage("Volta para a separação?")
      .withYesButton({
                       exec()
                     },
                     ButtonOption.caption("Sim"))
      .withNoButton({ }, ButtonOption.caption("Não"))
      .open()
  }

  override fun classPanel() = Pedido::class

  override fun HorizontalLayout.toolBarConfig() {
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
    pedidoRota()
    pedidoCarga()

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