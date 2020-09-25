package br.com.astrosoft.pedido.view.entrega

import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.SubWindowPDF
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnButton
import br.com.astrosoft.framework.view.localePtBr
import br.com.astrosoft.pedido.model.beans.Entregador
import br.com.astrosoft.pedido.model.beans.EntregadorNotas
import br.com.astrosoft.pedido.model.beans.classificaLinhas
import br.com.astrosoft.pedido.model.beans.groupByNota
import br.com.astrosoft.pedido.model.beans.groupByPedido
import br.com.astrosoft.pedido.model.planilha.PlanilhaEntregador
import br.com.astrosoft.pedido.model.planilha.PlanilhaPedidos
import br.com.astrosoft.pedido.model.planilha.PlanilhaProduto
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
import br.com.astrosoft.pedido.view.reports.RelatorioEntregador
import br.com.astrosoft.pedido.view.reports.RelatorioEntregadorPedido
import br.com.astrosoft.pedido.viewmodel.entrega.IPedidoEntregador
import br.com.astrosoft.pedido.viewmodel.entrega.PedidoEntregadorViewModel
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome.Solid.FILE_EXCEL
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.datePicker
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.tooltip
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.button.ButtonVariant.LUMO_SMALL
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant.LUMO_COMPACT
import com.vaadin.flow.component.icon.VaadinIcon.EYE
import com.vaadin.flow.component.icon.VaadinIcon.TABLE
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import org.vaadin.stefan.LazyDownloadButton
import java.io.ByteArrayInputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@CssImport(value = "./styles/gridTotal.css", themeFor = "vaadin-grid")
class TabEntregador(val viewModel: PedidoEntregadorViewModel): TabPanelGrid<Entregador>(), IPedidoEntregador {
  private lateinit var edtEntregadorDateI: DatePicker
  private lateinit var edtEntregadorDateF: DatePicker
  
  private fun showDialogDetailProduto(entregador: Entregador?) {
    entregador ?: return
    val entregadorList = entregador.findEntregadoresNotas(dateI, dateF)
    val form = SubWindowForm("${entregador.funcaoName} ${entregador.nome}", {
      buttonDownloadProdutos(entregadorList)
    }) {
      createGridDetailProdutos(entregadorList)
    }
    form.open()
  }
  
  private fun showDialogDetailPedido(entregador: Entregador?) {
    entregador ?: return
    val entregadorList =
      entregador.findEntregadoresNotas(dateI, dateF)
        .groupByPedido()
        .classificaLinhas()
    val form = SubWindowForm("${entregador.funcaoName} ${entregador.nome}", {
      buttonDownloadPedidos(entregadorList)
      buttonPdfPedido(entregadorList.filter {it.funcaoName != ""})
    }) {
      createGridDetailPedidos(entregadorList)
    }
    form.open()
  }
  
  private fun createGridDetailProdutos(entregadorList: List<EntregadorNotas>): Grid<EntregadorNotas> {
    val gridDetail = Grid(EntregadorNotas::class.java, false)
    return gridDetail.apply {
      addThemeVariants(LUMO_COMPACT)
      isMultiSort = false
      val itens = entregadorList
        .groupByNota()
        .classificaLinhas()
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
      
      setClassNameGenerator {
        if(it.funcaoName == "")
          if(it.classFormat == 0) "destaque1L" else "destaque2L"
        else
          if(it.classFormat == 0) "destaque1" else "destaque2"
      }
    }
  }
  
  private fun createGridDetailPedidos(entregadorList: List<EntregadorNotas>): Grid<EntregadorNotas> {
    val gridDetail = Grid(EntregadorNotas::class.java, false)
    return gridDetail.apply {
      addThemeVariants(LUMO_COMPACT)
      isMultiSort = false
      setItems(entregadorList)
      //
      entregadorNotasCarganoCol()
      entregadorNotasLojaCol()
      entregadorNotasNumPedidoCol()
      entregadorNotasDatePedidoCol()
      entregadorNotasNotaCol()
      entregadorNotasDateCol()
      entregadorNotasPisoCxs()
      entregadorNotasPisoPeso()
      entregadorNotasValor()
      setClassNameGenerator {
        if(it.funcaoName == "")
          "destaque1L"
        else "destaque1"
      }
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
    buttonXlsxEntregador()
    buttonPdfEntregador()
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
    addColumnButton(TABLE, "Pedidos", execButton = {entregador ->
      showDialogDetailPedido(entregador)
    }, block = {
      setHeader("Pedidos")
    })
    addColumnButton(TABLE, "Produtos", execButton = {entregador ->
      showDialogDetailProduto(entregador)
    }, block = {
      setHeader("Produtos")
    })
    entregadorFuncaoName()
    entregadorEmpno()
    entregadorNome()
    entregadorQtdEnt()
    entregadorPisoCxs()
    entregadorPisoPeso()
    entregadorValorNota()
  }
  
  private fun filename(name: String, ext: String): String {
    val sdf = DateTimeFormatter.ofPattern("yyMMddHHmmss")
    val textTime =
      LocalDateTime.now()
        .format(sdf)
    val filename = "$name$textTime.$ext"
    return filename
  }
  
  private fun HasComponents.buttonXlsxEntregador() {
    val button = LazyDownloadButton(FILE_EXCEL.create(),
                                    {filename("Entregador", "xlsx")}, {
                                      val planilha = PlanilhaEntregador()
                                      val bytes = planilha.grava(listBeans)
                                      ByteArrayInputStream(bytes)
                                    }
                                   )
    button.addThemeVariants(LUMO_SMALL)
    button.text = "Planilha"
    button.tooltip = "Salva a planilha"
    add(button)
  }
  
  private fun HasComponents.buttonPdfEntregador() {
    button {
      addThemeVariants(LUMO_SMALL)
      text = "Relatório"
      tooltip = "Visualiza relatório"
      icon = EYE.create()
      onLeftClick {
        val bytes = RelatorioEntregador.processaEntregadores(listBeans, dateI, dateF)
        showRelatorio("Entregador", bytes)
      }
    }
  }
  
  private fun HasComponents.buttonDownloadPedidos(lista: List<EntregadorNotas>) {
    val button = LazyDownloadButton(FontAwesome.Solid.FILE_EXCEL.create(),
                                    {filename("Pedidos", "xlsx")},
                                    {
                                      val planilha = PlanilhaPedidos()
                                      val bytes =
                                        planilha.grava(lista)
                                      ByteArrayInputStream(bytes)
                                    }
                                   )
    button.addThemeVariants(LUMO_SMALL)
    button.text = "Planilha"
    button.tooltip = "Salva a planilha"
    add(button)
  }
  
  private fun HasComponents.buttonPdfPedido(lista: List<EntregadorNotas>) {
    button {
      addThemeVariants(LUMO_SMALL)
      text = "Relatório"
      tooltip = "Visualiza relatório"
      icon = EYE.create()
      onLeftClick {
        val bytes = RelatorioEntregadorPedido.processaRelatorio(lista, dateI, dateF)
        showRelatorio("Pedido", bytes)
      }
    }
  }
  
  private fun HasComponents.buttonDownloadProdutos(lista: List<EntregadorNotas>) {
    val button = LazyDownloadButton(FontAwesome.Solid.FILE_EXCEL.create(),
                                    {filename("Produtos", "xlsx")},
                                    {
                                      val planilha = PlanilhaProduto()
                                      val bytes = planilha.grava(lista.groupByNota()
                                                                   .classificaLinhas())
                                      ByteArrayInputStream(bytes)
                                    }
                                   )
    button.addThemeVariants(LUMO_SMALL)
    button.text = "Planilha"
    button.tooltip = "Salva a planilha"
    add(button)
  }
  
  private fun showRelatorio(chave: String, byteArray: ByteArray) {
    SubWindowPDF(chave, byteArray).open()
  }
}


