package br.com.astrosoft.pedido.view.entrega

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnSeq
import br.com.astrosoft.framework.view.localePtBr
import br.com.astrosoft.framework.view.shiftSelect
import br.com.astrosoft.pedido.model.beans.Pedido
import br.com.astrosoft.pedido.view.*
import br.com.astrosoft.pedido.viewmodel.entrega.EZonaCarga
import br.com.astrosoft.pedido.viewmodel.entrega.IPedidoEntregaImpressoCarga
import br.com.astrosoft.pedido.viewmodel.entrega.PedidoEntregaImpressoCargaViewModel
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import org.claspina.confirmdialog.ButtonOption
import org.claspina.confirmdialog.ConfirmDialog
import java.time.LocalDate

class TabEntregaImpressoCarga(val viewModel: PedidoEntregaImpressoCargaViewModel) : TabPanelGrid<Pedido>(),
        IPedidoEntregaImpressoCarga {
  private lateinit var edtPedidoPesquisa: TextField
  override val label = "Carga"

  override fun updateComponent() {
    viewModel.updateGridImpressoCarga()
  }

  override val pedidoPesquisa: String
    get() = edtPedidoPesquisa.value ?: ""

  override fun selecionaCarga(exec: (EZonaCarga, LocalDate?) -> Unit) {
    val combo = Select<EZonaCarga>().apply {
      this.label = "Zona"
      this.setItems(EZonaCarga.values().toList())
      this.setItemLabelGenerator {
        it.descricao
      }
    }
    val pick = DatePicker("Entrada").apply {
      this.localePtBr()
      this.value = LocalDate.now()
    }
    ConfirmDialog
      .createQuestion()
      .withCaption("Selecione a zona da carga")
      .withMessage(VerticalLayout(combo, pick))
      .withOkButton({
                      val carga = combo.value
                      val entrada = pick.value
                      if (carga != null) exec(carga, entrada)
                    }, ButtonOption.caption("Ok"))
      .withCancelButton({ }, ButtonOption.caption("Cancelar"))
      .open()
  }

  override fun selecionaSemCarga(exec: () -> Unit) {
    ConfirmDialog
      .createQuestion()
      .withCaption("Envia para o painel separado")
      .withMessage("Confirma o envio?")
      .withOkButton({ exec() }, ButtonOption.caption("Ok"))
      .withCancelButton({ }, ButtonOption.caption("Cancelar"))
      .open()
  }

  override fun classPanel() = Pedido::class

  override fun HorizontalLayout.toolBarConfig() {
    if (AppConfig.isAdmin) {
      button("Desmarcar") {
        icon = VaadinIcon.CLOSE.create()
        addClickListener {
          viewModel.desmarcaCarga()
        }
      }
    }

    if (AppConfig.isAdmin || AppConfig.userSaci?.entrega_carga_visualizar == true) {
      button("Visualizar") {
        icon = VaadinIcon.EYE.create()
        addClickListener {
          viewModel.imprimirPedidos(itensSelecionado())
        }
      }
    }

    if (AppConfig.isAdmin || AppConfig.userSaci?.entrega_carga_criacarga == true) {
      button("Carga") {
        icon = VaadinIcon.TRUCK.create()
        addClickListener {
          viewModel.marcaCarga()
        }
      }
    }

    if (AppConfig.isAdmin || AppConfig.userSaci?.entrega_carga_separado == true) {
      button("Envia para Separado") {
        icon = VaadinIcon.ARROW_RIGHT.create()
        addClickListener {
          viewModel.marcaSemcarga()
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
    pedidoCarga()

    pedidoNfFat()
    pedidoDataFat()
    pedidoLoc()
    pedidoPiso()

    pedidoValor()
    pedidoNfEnt()
    pedidoDataEnt()
    pedidoObs()

    shiftSelect()

    this.setClassNameGenerator {
      if (it.separado == "S") "fonteAzul"
      else ""
    }
  }
}