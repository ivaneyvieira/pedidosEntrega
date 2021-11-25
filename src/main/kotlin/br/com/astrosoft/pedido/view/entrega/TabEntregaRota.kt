package br.com.astrosoft.pedido.view.entrega

import br.com.astrosoft.framework.view.TabPanelTree
import br.com.astrosoft.framework.view.localePtBr
import br.com.astrosoft.framework.view.shiftSelect
import br.com.astrosoft.pedido.model.beans.ETipoPedido
import br.com.astrosoft.pedido.model.beans.FiltroPedido
import br.com.astrosoft.pedido.model.beans.Pedido
import br.com.astrosoft.pedido.model.beans.Rota
import br.com.astrosoft.pedido.view.*
import br.com.astrosoft.pedido.viewmodel.entrega.IPedidoEntregaRota
import br.com.astrosoft.pedido.viewmodel.entrega.PedidoEntregaRotaViewModel
import com.github.mvysny.karibudsl.v10.datePicker
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.treegrid.TreeGrid
import java.time.LocalDate

class TabEntregaRota(val viewModel: PedidoEntregaRotaViewModel) : TabPanelTree<Rota>(Rota::class), IPedidoEntregaRota {
  private lateinit var edtEntregadorDateI: DatePicker
  private lateinit var edtEntregadorDateF: DatePicker

  override fun updateGrid(itens: List<Rota>) {
    updateGrid(itens) {
      it.listRota
    }
  }

  override fun filtro() = FiltroPedido(tipo = ETipoPedido.ENTREGA,
                                       ecommerce = false,
                                       dataInicial = edtEntregadorDateI.value,
                                       dataFinal = edtEntregadorDateF.value)

  override fun HorizontalLayout.toolBarConfig() {
    edtEntregadorDateI = datePicker("Data Inicial") {
      localePtBr()
      isClearButtonVisible = true
      value = LocalDate.now()
      addValueChangeListener {
        updateComponent()
      }
    }
    edtEntregadorDateF = datePicker("Data Final") {
      localePtBr()
      isClearButtonVisible = true
      value = LocalDate.now()
      addValueChangeListener {
        updateComponent()
      }
    }
  }

  fun Grid<Pedido>.gridPanel() {
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
    pedidoCustno()
    pedidoObs()
    pedidoUsername()
    shiftSelect()
  }

  override fun TreeGrid<Rota>.gridPanel() {
    rotaNome()
    rotaLojaNumero()
    rotaPedido()

    rotaData()
    rotaArea()

    rotaRota()
    rotaNfFat()
    rotaDataFat()

    rotaNfEnt()
    rotaDataEnt()

    rotaVendno()
    rotaFrete()

    rotaValor()
    rotaCustno()
  }

  override val label: String
    get() = "Desempenho Rota"

  override fun updateComponent() {
    viewModel.updateGrid()
  }
}