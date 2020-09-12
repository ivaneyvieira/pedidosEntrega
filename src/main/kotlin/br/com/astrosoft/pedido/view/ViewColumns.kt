package br.com.astrosoft.pedido.view

import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnLocalDateTime
import br.com.astrosoft.framework.view.addColumnSeq
import br.com.astrosoft.framework.view.addColumnString
import br.com.astrosoft.framework.view.addColumnTime
import br.com.astrosoft.pedido.model.beans.Entregador
import br.com.astrosoft.pedido.model.beans.EntregadorNotas
import br.com.astrosoft.pedido.model.beans.Pedido
import com.vaadin.flow.component.grid.Grid

fun Grid<Pedido>.pedidoNum() = addColumnSeq("Num")

fun Grid<Pedido>.pedidoLoja() = addColumnInt(Pedido::loja) {
  this.setHeader("Loja")
}
fun Grid<Pedido>.pedidoPedido() = addColumnInt(Pedido::pedido) {
  this.setHeader("Pedido")
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
fun Grid<Pedido>.pedidoCustno() = addColumnInt(Pedido::custno) {
  this.setHeader("Cliente")
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
}
fun Grid<Entregador>.entregadorPisoCxs() = addColumnInt(Entregador::pisoCxs) {
  this.setHeader("Piso Cxs")
}
fun Grid<Entregador>.entregadorPisoPeso() = addColumnDouble(Entregador::pisoPeso) {
  this.setHeader("Piso Peso")
}
fun Grid<Entregador>.entregadorValorNota() = addColumnDouble(Entregador::valorNota) {
  this.setHeader("Valor")
}
//
fun Grid<EntregadorNotas>.entregadorNotasCarganoCol() = addColumnInt(EntregadorNotas::carganoCol) {
  setHeader("Carga")
}
fun Grid<EntregadorNotas>.entregadorNotasLojaCol() = addColumnInt(EntregadorNotas::lojaCol) {
  setHeader("Loja")
}
fun Grid<EntregadorNotas>.entregadorNotasNumPedidoCol() = addColumnInt(EntregadorNotas::numPedidoCol) {
  setHeader("Pedido")
}
fun Grid<EntregadorNotas>.entregadorNotasDatePedidoCol() = addColumnLocalDate(EntregadorNotas::datePedidoCol) {
  setHeader("Data Pedido")
}
fun Grid<EntregadorNotas>.entregadorNotasNotaCol() = addColumnString(EntregadorNotas::notaCol) {
  setHeader("Nota")
}
fun Grid<EntregadorNotas>.entregadorNotasDateCol() = addColumnLocalDate(EntregadorNotas::dateCol) {
  setHeader("Data")
}
fun Grid<EntregadorNotas>.entregadorNotasPrdno() = addColumnString(EntregadorNotas::prdnoCol) {
  setHeader("Código")
}
fun Grid<EntregadorNotas>.entregadorNotasDescricao() = addColumnString(EntregadorNotas::descricao) {
  setHeader("Descrição")
}
fun Grid<EntregadorNotas>.entregadorNotasGrade() = addColumnString(EntregadorNotas::grade) {
  setHeader("Grade")
}
fun Grid<EntregadorNotas>.entregadorNotasPisoCxs() = addColumnInt(EntregadorNotas::pisoCxs) {
  setHeader("Piso CXS")
}
fun Grid<EntregadorNotas>.entregadorNotasPisoPeso() = addColumnDouble(EntregadorNotas::pisoPeso) {
  setHeader("Piso Peso")
}
fun Grid<EntregadorNotas>.entregadorNotasValor() = addColumnDouble(EntregadorNotas::valor) {
  setHeader("Valor")
}