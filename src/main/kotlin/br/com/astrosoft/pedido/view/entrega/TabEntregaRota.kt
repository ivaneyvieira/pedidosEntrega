package br.com.astrosoft.pedido.view.entrega

import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.TabPanelTree
import br.com.astrosoft.framework.view.localePtBr
import br.com.astrosoft.pedido.model.beans.ETipoPedido
import br.com.astrosoft.pedido.model.beans.FiltroPedido
import br.com.astrosoft.pedido.model.beans.Rota
import br.com.astrosoft.pedido.view.*
import br.com.astrosoft.pedido.viewmodel.entrega.IPedidoEntregaRota
import br.com.astrosoft.pedido.viewmodel.entrega.PedidoEntregaRotaViewModel
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.datePicker
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.treegrid.TreeGrid
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery
import java.time.LocalDate
import kotlin.streams.toList

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
                                       pesquisa = "",
                                       dataInicial = edtEntregadorDateI.value,
                                       dataFinal = edtEntregadorDateF.value)

  override fun showRotaLoja(listRotasLoja: List<Rota>) {
    val form = SubWindowForm("Período: ${edtEntregadorDateI.value.format()} à ${
      edtEntregadorDateF.value.format()
    }") {
      createGridDetailRotaLoja(listRotasLoja)
    }
    form.open()
  }

  override fun rotaAberta(): Rota? =
    gridPanel.dataProvider.fetchChildren(HierarchicalQuery(null, null)).toList().firstOrNull {
      gridPanel.isExpanded(it)
    }

  private fun createGridDetailRotaLoja(entregadorList: List<Rota>): Grid<Rota> {
    val gridDetail = TreeGrid<Rota>()
    return gridDetail.apply {
      addThemeVariants(GridVariant.LUMO_COMPACT)
      isMultiSort = false
      setItems(entregadorList) //
      rotaNome()
      rotaLojaNumero()

      rotaQuantEntrada()
      rotaFrete()
      rotaValor()
    }
  }

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
    button("Relatório") {
      icon = VaadinIcon.PRINT.create()
      addClickListener {
        viewModel.relatorioSimplificado()
      }
    }
  }

  override fun TreeGrid<Rota>.gridPanel() {
    rotaLojaNumero()
    rotaNome()
    rotaQuantEntrada().apply {
      this.setHeader("Qt")
      this.isAutoWidth = false
      this.width = "20px"
    }
    rotaFrete()
    rotaValor()
    rotaPedido()
    rotaData()
    rotaArea()
    rotaRota()
    rotaNfFat()
    rotaDataFat()
    rotaNfEnt()
    rotaDataEnt()
    rotaVendno()
  }

  override val label: String
    get() = "Desempenho Rota"

  override fun updateComponent() {
    viewModel.updateGrid()
  }
}