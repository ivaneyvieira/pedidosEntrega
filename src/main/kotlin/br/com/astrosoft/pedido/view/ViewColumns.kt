package br.com.astrosoft.pedido.view

import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.*
import br.com.astrosoft.pedido.model.beans.Entregador
import br.com.astrosoft.pedido.model.beans.EntregadorNotas
import br.com.astrosoft.pedido.model.beans.Pedido
import com.github.mvysny.karibudsl.v10.getAll
import com.vaadin.flow.component.grid.Grid

fun Grid<Pedido>.pedidoNum() = addColumnSeq("Num")

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
  this.grid.dataProvider.addDataProviderListener {
    val total = this.grid.dataProvider.getAll().mapNotNull {
      it as? Entregador
    }.sumBy { it.qtdEnt }
    this.setFooter(total.format())
  }
}

fun Grid<Entregador>.entregadorPisoCxs() = addColumnInt(Entregador::pisoCxs) {
  this.setHeader("Piso Cxs")
  this.grid.dataProvider.addDataProviderListener {
    val total = this.grid.dataProvider.getAll().mapNotNull {
      it as? Entregador
    }.sumBy { it.pisoCxs }
    this.setFooter(total.format())
  }
}

fun Grid<Entregador>.entregadorPisoPeso() = addColumnDouble(Entregador::pisoPeso) {
  this.setHeader("Piso Peso")
  this.grid.dataProvider.addDataProviderListener {
    val total = this.grid.dataProvider.getAll().mapNotNull {
      it as? Entregador
    }.sumByDouble { it.pisoPeso }
    this.setFooter(total.format())

  }
}

fun Grid<Entregador>.entregadorValorNota() = addColumnDouble(Entregador::valorNota) {
  this.setHeader("Valor")
  this.grid.dataProvider.addDataProviderListener {
    val total = this.grid.dataProvider.getAll().mapNotNull {
      it as? Entregador
    }.sumByDouble { it.valorNota }
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

fun Grid<EntregadorNotas>.entregadorNotasNumPedidoCol() =
  addColumnInt(EntregadorNotas::numPedidoCol) {
    setHeader("Pedido")
    isSortable = false
  }

fun Grid<EntregadorNotas>.entregadorNotasDatePedidoCol() =
  addColumnLocalDate(EntregadorNotas::datePedidoCol) {
    setHeader("Data Pedido")
    isSortable = false
  }

fun Grid<EntregadorNotas>.entregadorNotasNotaFatCol() =
  addColumnString(EntregadorNotas::notaFatCol) {
    setHeader("Nota Fat")
    isSortable = false
  }

fun Grid<EntregadorNotas>.entregadorNotasDateFatCol() =
  addColumnLocalDate(EntregadorNotas::dateFatCol) {
    setHeader("Data Fat")
    isSortable = false
  }

fun Grid<EntregadorNotas>.entregadorNotasNotaEntCol() =
  addColumnString(EntregadorNotas::notaEntCol) {
    setHeader("Nota Ent")
    isSortable = false
  }

fun Grid<EntregadorNotas>.entregadorNotasDateEntCol() =
  addColumnLocalDate(EntregadorNotas::dateEntCol) {
    setHeader("Data Ent")
    isSortable = false
  }

fun Grid<EntregadorNotas>.entregadorNotasEntregaCol() =
  addColumnLocalDate(EntregadorNotas::entregaCol) {
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