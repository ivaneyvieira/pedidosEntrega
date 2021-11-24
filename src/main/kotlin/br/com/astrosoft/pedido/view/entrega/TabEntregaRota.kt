package br.com.astrosoft.pedido.view.entrega

import br.com.astrosoft.framework.view.TabPanelTree
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnString
import br.com.astrosoft.framework.view.shiftSelect
import br.com.astrosoft.pedido.model.beans.Pedido
import br.com.astrosoft.pedido.model.beans.Rota
import br.com.astrosoft.pedido.view.*
import br.com.astrosoft.pedido.viewmodel.entrega.IPedidoEntregaRota
import br.com.astrosoft.pedido.viewmodel.entrega.PedidoEntregaRotaViewModel
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.treegrid.TreeGrid

class TabEntregaRota(val viewModel: PedidoEntregaRotaViewModel) : TabPanelTree<Rota>(Rota::class), IPedidoEntregaRota {

  override fun updateGrid(itens: List<Rota>) {
    updateGrid(itens) {
      it.listRota
    }
  }

  override fun HorizontalLayout.toolBarConfig() {
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
    rotaHora()
    rotaArea()
    rotaRota()
    rotaNfFat()
    rotaDataFat()
    rotaHoraFat()
    rotaNfEnt()
    rotaDataEnt()
    rotaHoraEnt()
    rotaVendno()
    rotaFrete()
    rotaValor()
    rotaCustno()
    rotaObs()
    rotaUsername()
  }

  override val label: String
    get() = "Desempenho Rota"

  override fun updateComponent() {
    viewModel.updateGrid()
  }
}