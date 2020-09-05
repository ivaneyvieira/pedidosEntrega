package br.com.astrosoft.pedidoEntrega.view

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.SubWindowPDF
import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.framework.view.addColumnButton
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnLocalDateTime
import br.com.astrosoft.framework.view.addColumnString
import br.com.astrosoft.framework.view.addColumnTime
import br.com.astrosoft.framework.view.list
import br.com.astrosoft.framework.view.localePtBr
import br.com.astrosoft.framework.view.shiftSelect
import br.com.astrosoft.framework.view.updateItens
import br.com.astrosoft.pedidoEntrega.model.beans.Entregador
import br.com.astrosoft.pedidoEntrega.model.beans.EntregadorNotas
import br.com.astrosoft.pedidoEntrega.model.beans.PedidoEntrega
import br.com.astrosoft.pedidoEntrega.model.beans.UserSaci
import br.com.astrosoft.pedidoEntrega.view.reports.RelatorioPedido
import br.com.astrosoft.pedidoEntrega.viewmodel.IPedidoEntregaView
import br.com.astrosoft.pedidoEntrega.viewmodel.PedidoEntregaViewModel
import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.datePicker
import com.github.mvysny.karibudsl.v10.getColumnBy
import com.github.mvysny.karibudsl.v10.grid
import com.github.mvysny.karibudsl.v10.horizontalLayout
import com.github.mvysny.karibudsl.v10.isExpand
import com.github.mvysny.karibudsl.v10.tabSheet
import com.github.mvysny.karibudsl.v10.textField
import com.github.mvysny.karibudsl.v10.verticalLayout
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.grid.ColumnTextAlign.END
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.grid.GridVariant.LUMO_COMPACT
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.icon.VaadinIcon.CLOSE
import com.vaadin.flow.component.icon.VaadinIcon.PRINT
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.provider.SortDirection
import com.vaadin.flow.data.value.ValueChangeMode.TIMEOUT
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import java.time.LocalDate

@Route(layout = PedidoEntregaLayout::class)
@PageTitle("Pedidos")
class PedidoEntregaView: ViewLayout<PedidoEntregaViewModel>(), IPedidoEntregaView {
  private lateinit var edtPedidoImprimir: TextField
  private lateinit var edtRotaImprimir: TextField
  private lateinit var edtAreaImprimir: TextField
  private lateinit var edtDataImprimir: DatePicker
  private lateinit var edtPedidoPendente: TextField
  private lateinit var edtRotaPendente: TextField
  private lateinit var edtAreaPendente: TextField
  private lateinit var edtDataPendente: DatePicker
  private lateinit var edtPedidoImpressoSemNota: TextField
  private lateinit var edtPedidoImpressoComNota: TextField
  private lateinit var edtEntregadorDateI: DatePicker
  private lateinit var edtEntregadorDateF: DatePicker
  private lateinit var gridPedidosEntregaImpressoComNota: Grid<PedidoEntrega>
  private lateinit var gridPedidosEntregaImpressoSemNota: Grid<PedidoEntrega>
  private lateinit var gridPedidosEntregaImprimir: Grid<PedidoEntrega>
  private lateinit var gridPedidosEntregaPendente: Grid<PedidoEntrega>
  private lateinit var gridEntregador: Grid<Entregador>
  override val viewModel: PedidoEntregaViewModel = PedidoEntregaViewModel(this)
  private val dataProviderPedidoImprimir = ListDataProvider<PedidoEntrega>(mutableListOf())
  private val dataProviderPedidoPendente = ListDataProvider<PedidoEntrega>(mutableListOf())
  private val dataProviderPedidoImpressoSemNota = ListDataProvider<PedidoEntrega>(mutableListOf())
  private val dataProviderPedidoImpressoComNota = ListDataProvider<PedidoEntrega>(mutableListOf())
  private val dataProviderEntregador = ListDataProvider<Entregador>(mutableListOf())
  
  override fun isAccept(user: UserSaci) = true
  
  init {
    tabSheet {
      val user = AppConfig.userSaci
      setSizeFull()
      if(user?.imprimir == true)
        tab {
          painelImprimir()
        }.apply {
          val button = Button(TAB_IMPRESSO) {
            viewModel.updateGridImprimir()
          }
          button.addThemeVariants(ButtonVariant.LUMO_SMALL)
          this.addComponentAsFirst(button)
        }
      if(user?.impressoSemNota == true)
        tab {
          painelImpressoSemNota()
        }.apply {
          val button = Button(TAB_SEM_NOTA) {
            viewModel.updateGridImpressoSemNota()
          }
          button.addThemeVariants(ButtonVariant.LUMO_SMALL)
          this.addComponentAsFirst(button)
        }
      if(user?.pendente == true)
        tab {
          painelPendente()
        }.apply {
          val button = Button(TAB_PENDENTE) {
            viewModel.updateGridPendente()
          }
          button.addThemeVariants(ButtonVariant.LUMO_SMALL)
          this.addComponentAsFirst(button)
        }
      if(user?.impressoComNota == true)
        tab {
          painelImpressoComNota()
        }.apply {
          val button = Button(TAB_COM_NOTA) {
            viewModel.updateGridImpressoComNota()
          }
          button.addThemeVariants(ButtonVariant.LUMO_SMALL)
          this.addComponentAsFirst(button)
        }
      if(user?.entregador == true)
        tab {
          painelEntregador()
        }.apply {
          val button = Button(TAB_ENTREGADOR) {
            viewModel.updateGridEntregador()
          }
          button.addThemeVariants(ButtonVariant.LUMO_SMALL)
          this.addComponentAsFirst(button)
        }
    }
  }
  
  fun HasComponents.painelImprimir(): VerticalLayout {
    return verticalLayout {
      this.setSizeFull()
      isMargin = false
      isPadding = false
      horizontalLayout {
        setWidthFull()
        button("Imprimir") {
          icon = PRINT.create()
          addClickListener {
            viewModel.imprimirPedidoMinuta()
          }
        }
        if(AppConfig.userSaci?.admin == true)
          button("Visualizar") {
            icon = VaadinIcon.EYE.create()
            addClickListener {
              viewModel.imprimirPedidos(itensSelecionadoImprimir())
            }
          }
        
        button("Confirma") {
          icon = VaadinIcon.THUMBS_UP.create()
          addClickListener {
            viewModel.confirmaPrint()
          }
        }
        
        edtPedidoImprimir = textField("Numero Pedido") {
          this.valueChangeMode = TIMEOUT
          this.isAutofocus = true
          addValueChangeListener {
            viewModel.updateGridImprimir()
          }
        }
        edtDataImprimir = datePicker("Data") {
          localePtBr()
          setClearButtonVisible(true)
          addValueChangeListener {
            viewModel.updateGridImprimir()
          }
        }
        edtAreaImprimir = textField("Área") {
          this.valueChangeMode = TIMEOUT
          addValueChangeListener {
            viewModel.updateGridImprimir()
          }
        }
        edtRotaImprimir = textField("Rota") {
          this.valueChangeMode = TIMEOUT
          
          addValueChangeListener {
            viewModel.updateGridImprimir()
          }
        }
      }
      gridPedidosEntregaImprimir = this.grid(dataProvider = dataProviderPedidoImprimir) {
        isExpand = true
        isMultiSort = true
        addThemeVariants(LUMO_COMPACT)
        setSelectionMode(SelectionMode.MULTI)
        
        addColumnSeq("Num")
        addColumnInt(PedidoEntrega::loja) {
          this.setHeader("Loja")
        }
        addColumnInt(PedidoEntrega::pedido) {
          this.setHeader("Pedido")
        }
        addColumnLocalDateTime(PedidoEntrega::dataHoraPrint) {
          this.setHeader("Data Hora Impressão")
        }
        addColumnLocalDate(PedidoEntrega::data) {
          this.setHeader("Data")
        }
        addColumnTime(PedidoEntrega::hora) {
          this.setHeader("Hora")
        }
        addColumnString(PedidoEntrega::area) {
          this.setHeader("Área")
        }
        addColumnString(PedidoEntrega::rota) {
          this.setHeader("Rota")
        }
        
        addColumnString(PedidoEntrega::nfFat) {
          this.setHeader("NF Fat")
        }
        
        addColumnLocalDate(PedidoEntrega::dataFat) {
          this.setHeader("Data")
        }
        addColumnTime(PedidoEntrega::horaFat) {
          this.setHeader("Hora")
        }
        
        addColumnString(PedidoEntrega::nfEnt) {
          this.setHeader("NF Ent")
        }
        addColumnLocalDate(PedidoEntrega::dataEnt) {
          this.setHeader("Data")
        }
        addColumnTime(PedidoEntrega::horaEnt) {
          this.setHeader("Hora")
        }
        
        addColumnInt(PedidoEntrega::vendno) {
          this.setHeader("Vendedor")
        }
        addColumnDouble(PedidoEntrega::frete) {
          this.setHeader("R$ Frete")
        }
        addColumnDouble(PedidoEntrega::valor) {
          this.setHeader("R$ Nota")
        }
        addColumnInt(PedidoEntrega::custno) {
          this.setHeader("Cliente")
        }
        addColumnString(PedidoEntrega::obs) {
          this.setHeader("Obs")
        }
        addColumnString(PedidoEntrega::username) {
          this.setHeader("Usuário")
        }
        this.shiftSelect()
        this.sort(listOf(
          GridSortOrder(getColumnBy(PedidoEntrega::loja), SortDirection.ASCENDING),
          GridSortOrder(getColumnBy(PedidoEntrega::pedido), SortDirection.DESCENDING))
                 )
      }
      
      viewModel.updateGridImprimir()
    }
  }
  
  fun HasComponents.painelImpressoSemNota(): VerticalLayout {
    return verticalLayout {
      this.setSizeFull()
      isMargin = false
      isPadding = false
      horizontalLayout {
        setWidthFull()
        if(AppConfig.userSaci?.admin == true)
          button("Desmarcar") {
            icon = CLOSE.create()
            addClickListener {
              viewModel.desmarcaSemNota()
            }
          }
        if(AppConfig.userSaci?.admin == true)
          button("Visualizar") {
            icon = VaadinIcon.EYE.create()
            addClickListener {
              viewModel.imprimirPedidos(itensSelecionadoImpressoSemNota())
            }
          }
        edtPedidoImpressoSemNota = textField("Numero Pedido") {
          this.valueChangeMode = TIMEOUT
          this.isAutofocus = true
          addValueChangeListener {
            viewModel.updateGridImpressoSemNota()
          }
        }
      }
      gridPedidosEntregaImpressoSemNota = grid(dataProvider = dataProviderPedidoImpressoSemNota) {
        this.isExpand = true
        isMultiSort = true
        addThemeVariants(LUMO_COMPACT)
        setSelectionMode(SelectionMode.MULTI)
        
        addColumnSeq("Num")
        addColumnInt(PedidoEntrega::loja) {
          this.setHeader("Loja")
        }
        addColumnInt(PedidoEntrega::pedido) {
          this.setHeader("Pedido")
        }
        addColumnLocalDateTime(PedidoEntrega::dataHoraPrint) {
          this.setHeader("Data Hora Impressão")
        }
        addColumnLocalDate(PedidoEntrega::data) {
          this.setHeader("Data")
        }
        addColumnTime(PedidoEntrega::hora) {
          this.setHeader("Hora")
        }
        addColumnString(PedidoEntrega::area) {
          this.setHeader("Área")
        }
        addColumnString(PedidoEntrega::rota) {
          this.setHeader("Rota")
        }
        
        addColumnString(PedidoEntrega::nfFat) {
          this.setHeader("NF Fat")
        }
        addColumnLocalDate(PedidoEntrega::dataFat) {
          this.setHeader("Data")
        }
        addColumnTime(PedidoEntrega::horaFat) {
          this.setHeader("Hora")
        }
        
        addColumnInt(PedidoEntrega::vendno) {
          this.setHeader("Vendedor")
        }
        addColumnDouble(PedidoEntrega::frete) {
          this.setHeader("R$ Frete")
        }
        addColumnDouble(PedidoEntrega::valor) {
          this.setHeader("R$ Nota")
        }
        addColumnInt(PedidoEntrega::custno) {
          this.setHeader("Cliente")
        }
        addColumnString(PedidoEntrega::obs) {
          this.setHeader("Obs")
        }
        
        addColumnString(PedidoEntrega::nfEnt) {
          this.setHeader("NF Ent")
        }
        addColumnLocalDate(PedidoEntrega::dataEnt) {
          this.setHeader("Data")
        }
        addColumnTime(PedidoEntrega::horaEnt) {
          this.setHeader("Hora")
        }
        
        addColumnString(PedidoEntrega::username) {
          this.setHeader("Usuário")
        }
        shiftSelect()
      }
    }
  }
  
  fun HasComponents.painelPendente(): VerticalLayout {
    return verticalLayout {
      this.setSizeFull()
      isMargin = false
      isPadding = false
      horizontalLayout {
        setWidthFull()
        edtPedidoPendente = textField("Numero Pedido") {
          this.valueChangeMode = TIMEOUT
          this.isAutofocus = true
          addValueChangeListener {
            viewModel.updateGridPendente()
          }
        }
        edtDataPendente = datePicker("Data") {
          localePtBr()
          setClearButtonVisible(true)
          addValueChangeListener {
            viewModel.updateGridPendente()
          }
        }
        edtAreaPendente = textField("Área") {
          this.valueChangeMode = TIMEOUT
          addValueChangeListener {
            viewModel.updateGridPendente()
          }
        }
        edtRotaPendente = textField("Rota") {
          this.valueChangeMode = TIMEOUT
          
          addValueChangeListener {
            viewModel.updateGridPendente()
          }
        }
      }
      gridPedidosEntregaPendente = this.grid(dataProvider = dataProviderPedidoPendente) {
        isExpand = true
        isMultiSort = true
        addThemeVariants(LUMO_COMPACT)
        
        addColumnSeq("Num")
        addColumnInt(PedidoEntrega::loja) {
          this.setHeader("Loja")
        }
        addColumnInt(PedidoEntrega::pedido) {
          this.setHeader("Pedido")
        }
        addColumnLocalDate(PedidoEntrega::data) {
          this.setHeader("Data")
          this.setSortProperty(PedidoEntrega::data.name, PedidoEntrega::loja.name, PedidoEntrega::pedido.name)
        }
        addColumnTime(PedidoEntrega::hora) {
          this.setHeader("Hora")
        }
        addColumnString(PedidoEntrega::area) {
          this.setHeader("Área")
        }
        addColumnString(PedidoEntrega::rota) {
          this.setHeader("Rota")
        }
        
        addColumnString(PedidoEntrega::nfFat) {
          this.setHeader("NF Fat")
        }
        addColumnLocalDate(PedidoEntrega::dataFat) {
          this.setHeader("Data")
          this.setSortProperty(PedidoEntrega::data.name, PedidoEntrega::loja.name, PedidoEntrega::pedido.name)
        }
        addColumnTime(PedidoEntrega::horaFat) {
          this.setHeader("Hora")
        }
        
        addColumnString(PedidoEntrega::nfEnt) {
          this.setHeader("NF Ent")
        }
        addColumnLocalDate(PedidoEntrega::dataEnt) {
          this.setHeader("Data")
          this.setSortProperty(PedidoEntrega::data.name, PedidoEntrega::loja.name, PedidoEntrega::pedido.name)
        }
        addColumnTime(PedidoEntrega::horaEnt) {
          this.setHeader("Hora")
        }
        
        addColumnInt(PedidoEntrega::vendno) {
          this.setHeader("Vendedor")
        }
        addColumnDouble(PedidoEntrega::frete) {
          this.setHeader("R$ Frete")
        }
        addColumnDouble(PedidoEntrega::valor) {
          this.setHeader("R$ Nota")
        }
        addColumnInt(PedidoEntrega::custno) {
          this.setHeader("Cliente")
        }
        addColumnString(PedidoEntrega::obs) {
          this.setHeader("Obs")
        }
        addColumnString(PedidoEntrega::username) {
          this.setHeader("Usuário")
        }
        shiftSelect()
      }
    }
  }
  
  fun HasComponents.painelImpressoComNota(): VerticalLayout {
    return verticalLayout {
      this.setSizeFull()
      isMargin = false
      isPadding = false
      
      horizontalLayout {
        setWidthFull()
        if(AppConfig.userSaci?.admin == true)
          button("Desmarcar") {
            icon = CLOSE.create()
            addClickListener {
              viewModel.desmarcaComNota()
            }
          }
        
        edtPedidoImpressoComNota = textField("Numero Pedido") {
          this.valueChangeMode = TIMEOUT
          this.isAutofocus = true
          addValueChangeListener {
            viewModel.updateGridImpressoComNota()
          }
        }
      }
      
      gridPedidosEntregaImpressoComNota = grid(dataProvider = dataProviderPedidoImpressoComNota) {
        this.isExpand = true
        isMultiSort = true
        addThemeVariants(LUMO_COMPACT)
        setSelectionMode(SelectionMode.MULTI)
        
        addColumnInt(PedidoEntrega::loja) {
          this.setHeader("Loja")
        }
        addColumnInt(PedidoEntrega::pedido) {
          this.setHeader("Pedido")
        }
        addColumnLocalDate(PedidoEntrega::data) {
          this.setHeader("Data")
        }
        addColumnTime(PedidoEntrega::hora) {
          this.setHeader("Hora")
        }
        addColumnString(PedidoEntrega::area) {
          this.setHeader("Área")
        }
        addColumnString(PedidoEntrega::rota) {
          this.setHeader("Rota")
        }
        
        addColumnString(PedidoEntrega::nfFat) {
          this.setHeader("NF Fat")
        }
        addColumnLocalDate(PedidoEntrega::dataFat) {
          this.setHeader("Data")
        }
        addColumnTime(PedidoEntrega::horaFat) {
          this.setHeader("Hora")
        }
        
        addColumnString(PedidoEntrega::nfEnt) {
          this.setHeader("NF Ent")
        }
        addColumnLocalDate(PedidoEntrega::dataEnt) {
          this.setHeader("Data")
        }
        addColumnTime(PedidoEntrega::horaEnt) {
          this.setHeader("Hora")
        }
        
        addColumnInt(PedidoEntrega::vendno) {
          this.setHeader("Vendedor")
        }
        addColumnDouble(PedidoEntrega::frete) {
          this.setHeader("R$ Frete")
        }
        addColumnDouble(PedidoEntrega::valor) {
          this.setHeader("R$ Nota")
        }
        addColumnInt(PedidoEntrega::custno) {
          this.setHeader("Cliente")
        }
        addColumnString(PedidoEntrega::obs) {
          this.setHeader("Obs")
        }
        addColumnString(PedidoEntrega::username) {
          this.setHeader("Usuário")
        }
        shiftSelect()
      }
    }
  }
  
  fun HasComponents.painelEntregador(): VerticalLayout {
    return verticalLayout {
      this.setSizeFull()
      isMargin = false
      isPadding = false
      
      horizontalLayout {
        setWidthFull()
        edtEntregadorDateI = datePicker("Data Incial") {
          localePtBr()
          isClearButtonVisible = true
          value = LocalDate.now()
          addValueChangeListener {
            viewModel.updateGridEntregador()
          }
        }
        edtEntregadorDateF = datePicker("Data Final") {
          localePtBr()
          isClearButtonVisible = true
          value = LocalDate.now()
          addValueChangeListener {
            viewModel.updateGridEntregador()
          }
        }
      }
      
      gridEntregador = grid(dataProvider = dataProviderEntregador) {
        this.isExpand = true
        isMultiSort = true
        addThemeVariants(LUMO_COMPACT)
        addColumnButton(VaadinIcon.TABLE, "Notas", execButton = {entregador ->
          showDialogDetail(entregador)
        })
        
        addColumnString(Entregador::funcaoName) {
          this.setHeader("Função")
        }
        addColumnInt(Entregador::empno) {
          this.setHeader("Número")
        }
        addColumnString(Entregador::nome) {
          this.setHeader("Nome")
        }
        addColumnInt(Entregador::qtdEnt) {
          this.setHeader("Qtd Ent")
        }
        addColumnInt(Entregador::pisoCxs) {
          this.setHeader("Piso Cxs")
        }
        addColumnDouble(Entregador::pisoPeso) {
          this.setHeader("Piso Peso")
        }
        addColumnDouble(Entregador::valorNota) {
          this.setHeader("Valor")
        }
      }
    }
  }
  
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
  
  private fun @VaadinDsl Grid<PedidoEntrega>.addColumnSeq(label: String) {
    addColumn {
      val lista = list(this)
      lista.indexOf(it) + 1
    }.apply {
      this.textAlign = END
      isAutoWidth = true
      setHeader(label)
    }
  }
  
  override fun updateGridImprimir(itens: List<PedidoEntrega>) {
    gridPedidosEntregaImprimir.deselectAll()
    dataProviderPedidoImprimir.updateItens(itens)
  }
  
  override fun updateGridPendente(itens: List<PedidoEntrega>) {
    gridPedidosEntregaPendente.deselectAll()
    dataProviderPedidoPendente.updateItens(itens)
  }
  
  override fun updateGridImpressoComNota(itens: List<PedidoEntrega>) {
    gridPedidosEntregaImpressoComNota.deselectAll()
    dataProviderPedidoImpressoComNota.updateItens(itens)
  }
  
  override fun updateGridImpressoSemNota(itens: List<PedidoEntrega>) {
    gridPedidosEntregaImpressoSemNota.deselectAll()
    dataProviderPedidoImpressoSemNota.updateItens(itens)
  }
  
  override fun updateGridEntregador(itens: List<Entregador>) {
    gridEntregador.deselectAll()
    dataProviderEntregador.updateItens(itens)
  }
  
  override fun itensSelecionadoImprimir(): List<PedidoEntrega> {
    return gridPedidosEntregaImprimir.selectedItems.toList()
  }
  
  override fun itensSelecionadoImpressoComNota(): List<PedidoEntrega> {
    return gridPedidosEntregaImpressoComNota.selectedItems.toList()
  }
  
  override fun itensSelecionadoImpressoSemNota(): List<PedidoEntrega> {
    return gridPedidosEntregaImpressoSemNota.selectedItems.toList()
  }
  
  override val pedidoImpressoSemNota: Int
    get() = edtPedidoImpressoSemNota.value?.toIntOrNull() ?: 0
  override val pedidoImpressoComNota: Int
    get() = edtPedidoImpressoComNota.value?.toIntOrNull() ?: 0
  override val pedidoImprimir: Int
    get() = edtPedidoImprimir.value?.toIntOrNull() ?: 0
  override val dataImprimir: LocalDate?
    get() = edtDataImprimir.value
  override val areaImprimir: String
    get() = edtAreaImprimir.value?.toUpperCase() ?: ""
  override val rotaImprimir: String
    get() = edtRotaImprimir.value?.toUpperCase() ?: ""
  override val pedidoPendente: Int
    get() = edtPedidoPendente.value?.toIntOrNull() ?: 0
  override val dataPendente: LocalDate?
    get() = edtDataPendente.value
  override val areaPendente: String
    get() = edtAreaPendente.value?.toUpperCase() ?: ""
  override val rotaPendente: String
    get() = edtRotaPendente.value?.toUpperCase() ?: ""
  override val dateI: LocalDate
    get() = edtEntregadorDateI.value ?: LocalDate.now()
  override val dateF: LocalDate
    get() = edtEntregadorDateF.value ?: LocalDate.now()
  
  override fun showRelatorioPedidoMinuta(pedidos: List<PedidoEntrega>) {
    val byteArray = RelatorioPedido.processaPedidosMinuta(pedidos)
    showRelatorio(byteArray)
  }
  
  override fun showRelatorioPedido(pedidos: List<PedidoEntrega>) {
    val byteArray = RelatorioPedido.processaPedidos(pedidos)
    showRelatorio(byteArray)
  }
  
  private fun showRelatorio(byteArray: ByteArray) {
    val chave = "PedidoEntrega"
    SubWindowPDF(chave, byteArray).open()
  }
  
  companion object {
    const val TAB_IMPRESSO: String = "Imprimir"
    const val TAB_PENDENTE: String = "Entrega Pendente"
    const val TAB_COM_NOTA: String = "Impresso com Nota"
    const val TAB_SEM_NOTA: String = "Impresso sem Nota"
    const val TAB_ENTREGADOR: String = "Desempenho Entrega"
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
                    descricao = "Total parcial + frete: ${entry.value.firstOrNull()?.valorFrete.format()}",
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
                                   descricao = "Total geral + Frete: ${this.sumByDouble {it.valorFrete}.format()}",
                                   pisoCxs = this.sumBy {it.pisoCxs},
                                   pisoPeso = this.sumByDouble {it.pisoPeso},
                                   valor = this.sumByDouble {it.valor},
                                   valorNota = this.sumByDouble {it.valorNota},
                                   valorFrete = this.sumByDouble {it.valorFrete}
                                  )
  val joinList = group + this + totalGeral
  return joinList.sortedWith(compareBy({it.loja},
                                       {it.nota},
                                       {it.numPedido},
                                       {it.datePedido},
                                       {if(it.prdno == "") "ZZZZZZ" else it.prdno}))
}
