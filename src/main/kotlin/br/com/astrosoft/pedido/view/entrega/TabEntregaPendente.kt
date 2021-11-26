package br.com.astrosoft.pedido.view.entrega

import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.localePtBr
import br.com.astrosoft.framework.view.shiftSelect
import br.com.astrosoft.pedido.model.beans.Pedido
import br.com.astrosoft.pedido.view.*
import br.com.astrosoft.pedido.viewmodel.entrega.IPedidoEntregaPendente
import br.com.astrosoft.pedido.viewmodel.entrega.PedidoEntregaPendenteViewModel
import com.github.mvysny.karibudsl.v10.datePicker
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.value.ValueChangeMode.TIMEOUT
import java.time.LocalDate

class TabEntregaPendente(val viewModel: PedidoEntregaPendenteViewModel) : TabPanelGrid<Pedido>(),
                                                                          IPedidoEntregaPendente {
  private lateinit var edtPedidoPendente: TextField
  private lateinit var edtRotaPendente: TextField
  private lateinit var edtAreaPendente: TextField
  private lateinit var edtDataPendente: DatePicker
  private lateinit var gridPedidosPendente: Grid<Pedido>
  private val dataProviderPedidoPendente = ListDataProvider<Pedido>(mutableListOf())
  override val label = "Entrega Pendente"

  override fun updateComponent() {
    viewModel.updateGridPendente()
  }

  override val pedidoPendente: Int
    get() = edtPedidoPendente.value?.toIntOrNull() ?: 0
  override val dataPendente: LocalDate?
    get() = edtDataPendente.value
  override val areaPendente: String
    get() = edtAreaPendente.value?.toUpperCase() ?: ""
  override val rotaPendente: String
    get() = edtRotaPendente.value?.toUpperCase() ?: ""

  override fun classPanel() = Pedido::class

  override fun HorizontalLayout.toolBarConfig() {
    edtPedidoPendente = textField("Numero Pedido") {
      this.valueChangeMode = TIMEOUT
      this.isAutofocus = true
      addValueChangeListener {
        updateComponent()
      }
    }
    edtDataPendente = datePicker("Data") {
      localePtBr()
      isClearButtonVisible = true
      addValueChangeListener {
        updateComponent()
      }
    }
    edtAreaPendente = textField("Área") {
      this.valueChangeMode = TIMEOUT
      addValueChangeListener {
        updateComponent()
      }
    }
    edtRotaPendente = textField("Rota") {
      this.valueChangeMode = TIMEOUT

      addValueChangeListener {
        updateComponent()
      }
    }
  }

  override fun Grid<Pedido>.gridPanel() {
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