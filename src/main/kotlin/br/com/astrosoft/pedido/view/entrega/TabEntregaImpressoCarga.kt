package br.com.astrosoft.pedido.view.entrega

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnSeq
import br.com.astrosoft.framework.view.shiftSelect
import br.com.astrosoft.pedido.model.beans.Pedido
import br.com.astrosoft.pedido.view.*
import br.com.astrosoft.pedido.viewmodel.entrega.EZonaCarga
import br.com.astrosoft.pedido.viewmodel.entrega.IPedidoEntregaImpressoCarga
import br.com.astrosoft.pedido.viewmodel.entrega.PedidoEntregaImpressoCargaViewModel
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import org.claspina.confirmdialog.ButtonOption
import org.claspina.confirmdialog.ConfirmDialog

class TabEntregaImpressoCarga(val viewModel: PedidoEntregaImpressoCargaViewModel) : TabPanelGrid<Pedido>(),
        IPedidoEntregaImpressoCarga {
  private lateinit var edtPedidoPesquisa: TextField
  override val label = "Carga"

  override fun updateComponent() {
    viewModel.updateGridImpressoCarga()
  }

  override val pedidoPesquisa: String
    get() = edtPedidoPesquisa.value ?: ""

  override fun selecionaCarga(exec: (EZonaCarga) -> Unit) {
    val combo = Select<EZonaCarga>().apply {
      this.label = "Zona"
      this.setItems(EZonaCarga.values().toList())
      this.setItemLabelGenerator {
        it.descricao
      }
    }
    ConfirmDialog
      .createQuestion()
      .withCaption("Selecione a zona da carga")
      .withMessage(combo)
      .withOkButton({
                      val value =
                        combo.value
                      if (value != null) exec(value)
                    },
                    ButtonOption.caption("Ok"))
      .withCancelButton({ }, ButtonOption.caption("Cancelar"))
      .open()
  }

  override fun classPanel() = Pedido::class

  override fun HorizontalLayout.toolBarConfig() {
    if (AppConfig.isAdmin) button("Desmarcar") {
      icon = VaadinIcon.CLOSE.create()
      addClickListener {
        viewModel.desmarcaCarga()
      }
    }

    if (AppConfig.isAdmin) button("Visualizar") {
      icon = VaadinIcon.EYE.create()
      addClickListener {
        viewModel.imprimirPedidos(itensSelecionado())
      }
    }

    button("Carga") {
      icon = VaadinIcon.TRUCK.create()
      addClickListener {
        viewModel.marcaCarga()
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

    this.setClassNameGenerator {
      if (it.separado == "S") "fonteAzul"
      else ""
    }
  }
}