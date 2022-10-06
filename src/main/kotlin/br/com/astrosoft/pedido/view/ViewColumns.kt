package br.com.astrosoft.pedido.view

import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.*
import br.com.astrosoft.pedido.model.beans.Entregador
import br.com.astrosoft.pedido.model.beans.EntregadorNotas
import br.com.astrosoft.pedido.model.beans.Pedido
import br.com.astrosoft.pedido.model.beans.Rota
import com.github.mvysny.kaributools.fetchAll
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.treegrid.TreeGrid

fun Grid<Pedido>.pedidoLoja() = addColumnInt(Pedido::loja) {
  this.setHeader("Loja")
}

fun Grid<Pedido>.pedidoPedido() = addColumnInt(Pedido::pedido) {
  this.setHeader("Pedido")
}

fun Grid<Pedido>.pedidoTipo() = addColumnString(Pedido::tipoStr) {
  this.setHeader("Tipo")
}

fun Grid<Pedido>.pedidoTipoECommerce() = addColumnString(Pedido::tipoEcommece) {
  this.setHeader("Tipo")
}

fun Grid<Pedido>.pedidoData() = addColumnLocalDate(Pedido::data) {
  this.setHeader("Data")
  this.setSortProperty(Pedido::data.name, Pedido::loja.name, Pedido::pedido.name)
}

fun Grid<Pedido>.pedidoHora() = addColumnTime(Pedido::hora) {
  this.setHeader("Hora")
}

fun Grid<Pedido>.pedidoArea() = addColumnString(Pedido::area) {
  this.setHeader("Área")
}

fun Grid<Pedido>.pedidoRota() = addColumnString(Pedido::rota) {
  this.setHeader("Rota")
}

fun Grid<Pedido>.pedidoCarga() = addColumnString(Pedido::descricaoZonaCarga) {
  this.setHeader("Carga")
}

fun Grid<Pedido>.pedidoNfFat() = addColumnString(Pedido::nfFat) {
  this.setHeader("NF Fat")
}

fun Grid<Pedido>.pedidoDataFat() = addColumnLocalDate(Pedido::dataFat) {
  this.setHeader("Data")
  this.setSortProperty(Pedido::dataFat.name, Pedido::loja.name, Pedido::pedido.name)
}

fun Grid<Pedido>.pedidoHoraFat() = addColumnTime(Pedido::horaFat) {
  this.setHeader("Hora")
}

fun Grid<Pedido>.pedidoLoc() = addColumnString(Pedido::loc) {
  this.setHeader("CD")
}

fun Grid<Pedido>.pedidoPiso() = addColumnDouble(Pedido::piso) {
  this.setHeader("Piso")
}

fun Grid<Pedido>.pedidoNfEnt() = addColumnString(Pedido::nfEnt) {
  this.setHeader("NF Ent")
}

fun Grid<Pedido>.pedidoDataEnt() = addColumnLocalDate(Pedido::dataEnt) {
  this.setHeader("Data")
  this.setSortProperty(Pedido::dataEnt.name, Pedido::loja.name, Pedido::pedido.name)
}

fun Grid<Pedido>.pedidoHoraEnt() = addColumnTime(Pedido::horaEnt) {
  this.setHeader("Hora")
}

fun Grid<Pedido>.pedidoVendno() = addColumnInt(Pedido::vendno) {
  this.setHeader("Vendedor")
}

fun Grid<Pedido>.pedidoFrete() = addColumnDouble(Pedido::frete) {
  this.setHeader("R$ Frete")
}

fun Grid<Pedido>.pedidoValor() = addColumnDouble(Pedido::valorComFrete) {
  this.setHeader("R$ Nota")
}

fun Grid<Pedido>.pedidoObs() = addColumnString(Pedido::obs) {
  this.setHeader("Obs")
}

fun Grid<Pedido>.pedidoUsername() = addColumnString(Pedido::username) {
  this.setHeader("Usuário")
}

fun Grid<Pedido>.pedidoDataHoraPrint() = addColumnLocalDateTime(Pedido::dataHoraPrint) {
  this.setHeader("Data Hora Impressão")
}

//
fun Grid<Entregador>.entregadorFuncaoName() = addColumnString(Entregador::funcaoName) {
  this.setHeader("Função")
}

fun Grid<Entregador>.entregadorEmpno() = addColumnInt(Entregador::empno) {
  this.setHeader("Número")
}

fun Grid<Entregador>.entregadorNome() = addColumnString(Entregador::nome) {
  this.setHeader("Nome")
}

fun Grid<Entregador>.entregadorQtdEnt() = addColumnInt(Entregador::qtdEnt) {
  this.setHeader("Qtd Ent")
  this.grid.dataProvider.addDataProviderListener {
    val total = this.grid.dataProvider.fetchAll().mapNotNull {
      it as? Entregador
    }.sumOf { it.qtdEnt }
    this.setFooter(total.format())
  }
}

fun Grid<Entregador>.entregadorPisoCxs() = addColumnInt(Entregador::pisoCxs) {
  this.setHeader("Piso Cxs")
  this.grid.dataProvider.addDataProviderListener {
    val total = this.grid.dataProvider.fetchAll().mapNotNull {
      it as? Entregador
    }.sumOf { it.pisoCxs }
    this.setFooter(total.format())
  }
}

fun Grid<Entregador>.entregadorPisoPeso() = addColumnDouble(Entregador::pisoPeso) {
  this.setHeader("Piso Peso")
  this.grid.dataProvider.addDataProviderListener {
    val total = this.grid.dataProvider.fetchAll().mapNotNull {
      it as? Entregador
    }.sumOf { it.pisoPeso }
    this.setFooter(total.format())

  }
}

fun Grid<Entregador>.entregadorValorNota() = addColumnDouble(Entregador::valorNota) {
  this.setHeader("Valor")
  this.grid.dataProvider.addDataProviderListener {
    val total = this.grid.dataProvider.fetchAll().mapNotNull {
      it as? Entregador
    }.sumOf { it.valorNota }
    this.setFooter(total.format())
  }
}

//
fun Grid<EntregadorNotas>.entregadorNotasCarganoCol() = addColumnInt(EntregadorNotas::carganoCol) {
  setHeader("Carga")
  isSortable = false
}

fun Grid<EntregadorNotas>.entregadorNotasLojaCol() = addColumnInt(EntregadorNotas::lojaCol) {
  setHeader("Loja")
  isSortable = false
}

fun Grid<EntregadorNotas>.entregadorNotasNumPedidoCol() = addColumnInt(EntregadorNotas::numPedidoCol) {
  setHeader("Pedido")
  isSortable = false
}

fun Grid<EntregadorNotas>.entregadorNotasDatePedidoCol() = addColumnLocalDate(EntregadorNotas::datePedidoCol) {
  setHeader("Data Pedido")
  isSortable = false
}

fun Grid<EntregadorNotas>.entregadorNotasNotaFatCol() = addColumnString(EntregadorNotas::notaFatCol) {
  setHeader("Nota Fat")
  isSortable = false
}

fun Grid<EntregadorNotas>.entregadorNotasDateFatCol() = addColumnLocalDate(EntregadorNotas::dateFatCol) {
  setHeader("Data Fat")
  isSortable = false
}

fun Grid<EntregadorNotas>.entregadorNotasNotaEntCol() = addColumnString(EntregadorNotas::notaEntCol) {
  setHeader("Nota Ent")
  isSortable = false
}

fun Grid<EntregadorNotas>.entregadorNotasDateEntCol() = addColumnLocalDate(EntregadorNotas::dateEntCol) {
  setHeader("Data Ent")
  isSortable = false
}

fun Grid<EntregadorNotas>.entregadorNotasEntregaCol() = addColumnLocalDate(EntregadorNotas::entregaCol) {
  setHeader("Entrega")
  isSortable = false
}

fun Grid<EntregadorNotas>.entregadorNotasPrdno() = addColumnString(EntregadorNotas::prdnoCol) {
  setHeader("Código")
  isSortable = false
}

fun Grid<EntregadorNotas>.entregadorNotasDescricao() = addColumnString(EntregadorNotas::descricao) {
  setHeader("Descrição")
  isSortable = false
}

fun Grid<EntregadorNotas>.entregadorNotasGrade() = addColumnString(EntregadorNotas::grade) {
  setHeader("Grade")
  isSortable = false
}

fun Grid<EntregadorNotas>.entregadorNotasPisoCxs() = addColumnInt(EntregadorNotas::pisoCxs) {
  setHeader("Piso CXS")
  isSortable = false
}

fun Grid<EntregadorNotas>.entregadorNotasPisoPeso() = addColumnDouble(EntregadorNotas::pisoPeso) {
  setHeader("Piso Peso")
  isSortable = false
}

fun Grid<EntregadorNotas>.entregadorNotasValor() = addColumnDouble(EntregadorNotas::valor) {
  setHeader("Valor")
  isSortable = false
}

/****************************************/

fun TreeGrid<Rota>.rotaNome() = addColumnString(Rota::nomeRota) {
  setHeader("Rota")
  this.isAutoWidth = false
  this.width = "50px"
}

fun TreeGrid<Rota>.rotaLojaNumero() = addHierarchyColumn { it.loja?.toString() ?: "" }.apply {
  setHeader("Loja")
  this.isAutoWidth = false
  this.width = "20px"
}

fun Grid<Rota>.rotaPedido() = addColumnInt(Rota::pedido) {
  this.setHeader("Pedido")
}

fun Grid<Rota>.rotaData() = addColumnLocalDate(Rota::data) {
  this.setHeader("Data")
  this.setSortProperty(Rota::data.name, Rota::loja.name, Rota::pedido.name)
}

fun Grid<Rota>.rotaArea() = addColumnString(Rota::area) {
  this.setHeader("Área")
}

fun Grid<Rota>.rotaRota() = addColumnString(Rota::rota) {
  this.setHeader("Rota")
}

fun Grid<Rota>.rotaNfFat() = addColumnString(Rota::nfFat) {
  this.setHeader("NF Fat")
}

fun Grid<Rota>.rotaDataFat() = addColumnLocalDate(Rota::dataFat) {
  this.setHeader("Data")
  this.setSortProperty(Pedido::dataFat.name, Pedido::loja.name, Rota::pedido.name)
}

fun Grid<Rota>.rotaNfEnt() = addColumnString(Rota::nfEnt) {
  this.setHeader("NF Ent")
}

fun Grid<Rota>.rotaDataEnt() = addColumnLocalDate(Rota::dataEnt) {
  this.setHeader("Data")
  this.setSortProperty(Rota::dataEnt.name, Rota::loja.name, Rota::pedido.name)
}

fun Grid<Rota>.rotaVendno() = addColumnInt(Rota::vendno) {
  this.setHeader("Vendedor")
}

fun Grid<Rota>.rotaQuantEntrada() = addColumnInt(Rota::quantEntradas) {
  this.setHeader("Qtd Entr")
}

fun Grid<Rota>.rotaFrete() = addColumnDouble(Rota::frete) {
  this.setHeader("R$ Frete")
}

fun Grid<Rota>.rotaValor() = addColumnDouble(Rota::valorFat) {
  this.setHeader("R$ Nota")
}




