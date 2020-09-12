package br.com.astrosoft.pedido.view.entrega


import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnButton
import br.com.astrosoft.framework.view.localePtBr
import br.com.astrosoft.pedido.model.beans.Entregador
import br.com.astrosoft.pedido.model.beans.EntregadorNotas
import br.com.astrosoft.pedido.model.beans.groupByNota
import br.com.astrosoft.pedido.view.entregadorEmpno
import br.com.astrosoft.pedido.view.entregadorFuncaoName
import br.com.astrosoft.pedido.view.entregadorNome
import br.com.astrosoft.pedido.view.entregadorNotasCarganoCol
import br.com.astrosoft.pedido.view.entregadorNotasDateCol
import br.com.astrosoft.pedido.view.entregadorNotasDatePedidoCol
import br.com.astrosoft.pedido.view.entregadorNotasDescricao
import br.com.astrosoft.pedido.view.entregadorNotasGrade
import br.com.astrosoft.pedido.view.entregadorNotasLojaCol
import br.com.astrosoft.pedido.view.entregadorNotasNotaCol
import br.com.astrosoft.pedido.view.entregadorNotasNumPedidoCol
import br.com.astrosoft.pedido.view.entregadorNotasPisoCxs
import br.com.astrosoft.pedido.view.entregadorNotasPisoPeso
import br.com.astrosoft.pedido.view.entregadorNotasPrdno
import br.com.astrosoft.pedido.view.entregadorNotasValor
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
      createGridDetail(entregador)
    }
    form.open()
  }
  
  private fun createGridDetail(entregador: Entregador): Grid<EntregadorNotas> {
    val gridDetail = Grid(EntregadorNotas::class.java, false)
    return gridDetail.apply {
      addThemeVariants(LUMO_COMPACT)
      isMultiSort = false
      val itens =
        entregador.findEntregadoresNotas(dateI, dateF)
          .groupByNota()
      setItems(itens)
      //
      entregadorNotasCarganoCol()
      entregadorNotasLojaCol()
      entregadorNotasNumPedidoCol()
      entregadorNotasDatePedidoCol()
      entregadorNotasNotaCol()
      entregadorNotasDateCol()
      entregadorNotasPrdno()
      entregadorNotasDescricao()
      entregadorNotasGrade()
      entregadorNotasPisoCxs()
      entregadorNotasPisoPeso()
      entregadorNotasValor()
    }
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

