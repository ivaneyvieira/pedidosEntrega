package br.com.astrosoft.pedido.view.entrega.columns

import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import br.com.astrosoft.pedido.model.beans.PedidoGroup
import com.vaadin.flow.component.grid.Grid

fun Grid<PedidoGroup>.pedidoCarga() = addColumnString(PedidoGroup::carga) {
  this.setHeader("Carga")
}

fun Grid<PedidoGroup>.pedidoData() = addColumnLocalDate(PedidoGroup::data) {
  this.setHeader("Data")
}

fun Grid<PedidoGroup>.pedidoLoc() = addColumnString(PedidoGroup::loc) {
  this.setHeader("Loc")
}

fun Grid<PedidoGroup>.pedidoPiso() = addColumnInt(PedidoGroup::piso) {
  this.setHeader("Piso")
}

fun Grid<PedidoGroup>.pedidoTotal() = addColumnDouble(PedidoGroup::total) {
  this.setHeader("Total")
}

fun Grid<PedidoGroup>.pedidoQuant() = addColumnInt(PedidoGroup::quant) {
  this.setHeader("Quant")
}

