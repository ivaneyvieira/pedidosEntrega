package br.com.astrosoft.pedido.view.entrega

import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnButton
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import br.com.astrosoft.framework.view.localePtBr
import br.com.astrosoft.pedido.model.beans.Entregador
import br.com.astrosoft.pedido.model.beans.EntregadorNotas
import br.com.astrosoft.pedido.view.entregadorEmpno
import br.com.astrosoft.pedido.view.entregadorFuncaoName
import br.com.astrosoft.pedido.view.entregadorNome
import br.com.astrosoft.pedido.view.entregadorPisoCxs
import br.com.astrosoft.pedido.view.entregadorPisoPeso
import br.com.astrosoft.pedido.view.entregadorQtdEnt
import br.com.astrosoft.pedido.view.entregadorValorNota
import br.com.astrosoft.pedido.viewmodel.entrega.IPedidoEntregador
import br.com.astrosoft.pedido.viewmodel.entrega.PedidoEntregadorViewModel
import com.github.mvysny.karibudsl.v10.datePicker
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant.LUMO_COMPACT
import com.vaadin.flow.component.icon.VaadinIcon.TABLE
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import java.time.LocalDate

class TabEntregador(val viewModel: PedidoEntregadorViewModel): TabPanelGrid<Entregador>(), IPedidoEntregador {
  private lateinit var edtEntregadorDateI: DatePicker
  private lateinit var edtEntregadorDateF: DatePicker
  
  private fun showDialogDetail(entregador: Entregador?) {
    entregador ?: return
    val form = SubWindowForm("${entregador.funcaoName} ${entregador.nome}") {
      val gridDetail = Grid(EntregadorNotas::class.java, false)
      gridDetail.apply {
        addThemeVariants(LUMO_COMPACT)
        isMultiSort = false
        val itens =
          entregador.findEntregadoresNotas(dateI, dateF)
            .groupByNota()
        setItems(itens)
        //
        addColumnInt(EntregadorNotas::carganoCol) {
          setHeader("Carga")
        }
        addColumnInt(EntregadorNotas::lojaCol) {
          setHeader("Loja")
        }
        addColumnInt(EntregadorNotas::numPedidoCol) {
          setHeader("Pedido")
        }
        addColumnLocalDate(EntregadorNotas::datePedidoCol) {
          setHeader("Data Pedido")
        }
        addColumnString(EntregadorNotas::notaCol) {
          setHeader("Nota")
        }
        addColumnLocalDate(EntregadorNotas::dateCol) {
          setHeader("Data")
        }
        addColumnString(EntregadorNotas::prdno) {
          setHeader("Código")
        }
        addColumnString(EntregadorNotas::descricao) {
          setHeader("Descrição")
        }
        addColumnString(EntregadorNotas::grade) {
          setHeader("Grade")
        }
        addColumnInt(EntregadorNotas::pisoCxs) {
          setHeader("Piso CXS")
        }
        addColumnDouble(EntregadorNotas::pisoPeso) {
          setHeader("Piso Peso")
        }
        addColumnDouble(EntregadorNotas::valor) {
          setHeader("Valor")
        }
      }
    }
    form.open()
  }
  
  override val label = "Desempenho Entrega"
  
  override fun updateComponent() {
    viewModel.updateGridEntregador()
  }
  
  override val dateI: LocalDate
    get() = edtEntregadorDateI.value ?: LocalDate.now()
  override val dateF: LocalDate
    get() = edtEntregadorDateF.value ?: LocalDate.now()
  override fun classPanel() = Entregador::class
  
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
  
  override fun Grid<Entregador>.gridPanel() {
    addColumnButton(TABLE, "Notas", execButton = {entregador ->
      showDialogDetail(entregador)
    })
    entregadorFuncaoName()
    entregadorEmpno()
    entregadorNome()
    entregadorQtdEnt()
    entregadorPisoCxs()
    entregadorPisoPeso()
    entregadorValorNota()
  }
}

private fun List<EntregadorNotas>.groupByNota(): List<EntregadorNotas> {
  val group = this.groupBy {entregadorNota ->
    entregadorNota.groupByNota()
  }.entries.map {entry ->
    EntregadorNotas(cargano = 0,
                    funcaoName = "",
                    nome = "",
                    date = null,
                    empno = 0,
                    loja = entry.key.loja,
                    nota = entry.key.nota,
                    numPedido = entry.key.numPedido,
                    datePedido = entry.key.datePedido,
                    prdno = "",
                    grade = "",
                    descricao = "Total parcial + Frete: ${entry.value.firstOrNull()?.valorFrete.format()}",
                    pisoCxs = entry.value.sumBy {it.pisoCxs},
                    pisoPeso = entry.value.sumByDouble {it.pisoPeso},
                    valor = entry.value.sumByDouble {it.valor},
                    valorNota = entry.value.firstOrNull()?.valorNota ?: 0.00,
                    valorFrete = entry.value.firstOrNull()?.valorFrete ?: 0.00
                   )
  }
  val totalGeral = EntregadorNotas(cargano = 0,
                                   funcaoName = "",
                                   nome = "",
                                   date = null,
                                   empno = 0,
                                   loja = 999,
                                   nota = "",
                                   numPedido = 0,
                                   datePedido = null,
                                   prdno = "",
                                   grade = "",
                                   descricao = "Total geral + Frete: ${
                                     group.sumByDouble {it.valorFrete}
                                       .format()
                                   }",
                                   pisoCxs = this.sumBy {it.pisoCxs},
                                   pisoPeso = this.sumByDouble {it.pisoPeso},
                                   valor = this.sumByDouble {it.valor},
                                   valorNota = group.sumByDouble {it.valorNota},
                                   valorFrete = group.sumByDouble {it.valorFrete}
                                  )
  val joinList = group + this + totalGeral
  return joinList.sortedWith(compareBy({it.loja},
                                       {it.nota},
                                       {it.numPedido},
                                       {it.datePedido},
                                       {if(it.prdno == "") "ZZZZZZ" else it.prdno}))
}
