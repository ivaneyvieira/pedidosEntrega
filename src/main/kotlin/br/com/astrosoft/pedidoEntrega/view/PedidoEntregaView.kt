package br.com.astrosoft.pedidoEntrega.view

import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.GridDataProvider
import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.framework.view.addColumnDate
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import br.com.astrosoft.framework.view.addColumnTime
import br.com.astrosoft.framework.view.localePtBr
import br.com.astrosoft.pedidoEntrega.model.beans.PedidoEntrega
import br.com.astrosoft.pedidoEntrega.model.beans.UserSaci
import br.com.astrosoft.pedidoEntrega.viewmodel.IPedidoEntregaView
import br.com.astrosoft.pedidoEntrega.viewmodel.PedidoEntregaViewModel
import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.addColumnFor
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.datePicker
import com.github.mvysny.karibudsl.v10.getAll
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
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.grid.ColumnTextAlign.END
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.grid.GridVariant.LUMO_COMPACT
import com.vaadin.flow.component.icon.VaadinIcon.CLOSE
import com.vaadin.flow.component.icon.VaadinIcon.PRINT
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.provider.Query
import com.vaadin.flow.data.provider.QuerySortOrder
import com.vaadin.flow.data.provider.SortDirection.DESCENDING
import com.vaadin.flow.data.renderer.TextRenderer
import com.vaadin.flow.data.value.ValueChangeMode.TIMEOUT
import com.vaadin.flow.function.SerializablePredicate
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import java.time.LocalDate
import java.util.*
import kotlin.Comparator
import kotlin.reflect.KProperty1
import kotlin.streams.toList

@Route(layout = AppPedidoLayout::class)
@PageTitle("Pedidos")
@HtmlImport("frontend://styles/shared-styles.html")
class PedidoEntregaView: ViewLayout<PedidoEntregaViewModel>(), IPedidoEntregaView {
  private lateinit var edtPedidoImprimir: TextField
  private lateinit var cmbRotaImprimir: TextField
  private lateinit var cmbAreaImprimir: TextField
  private lateinit var edtDataImprimir: DatePicker
  private lateinit var edtPedidoPendente: TextField
  private lateinit var cmbRotaPendente: TextField
  private lateinit var cmbAreaPendente: TextField
  private lateinit var edtDataPendente: DatePicker
  private lateinit var edtPedidoImpressoSemNota: TextField
  private lateinit var edtPedidoImpressoComNota: TextField
  private lateinit var gridPedidosEntregaImpressoComNota: Grid<PedidoEntrega>
  private lateinit var gridPedidosEntregaImpressoSemNota: Grid<PedidoEntrega>
  private lateinit var gridPedidosEntregaImprimir: Grid<PedidoEntrega>
  private lateinit var gridPedidosEntregaPendente: Grid<PedidoEntrega>
  override val viewModel: PedidoEntregaViewModel = PedidoEntregaViewModel(this)
  private val dataProviderProdutosImprimir = ListDataProvider<PedidoEntrega>(mutableListOf())
  private val dataProviderProdutosPendente = ListDataProvider<PedidoEntrega>(mutableListOf())
  private val dataProviderProdutosImpressoSemNota = ListDataProvider<PedidoEntrega>(mutableListOf())
  private val dataProviderProdutosImpressoComNota = ListDataProvider<PedidoEntrega>(mutableListOf())
  
  override fun isAccept(user: UserSaci) = true
  
  init {
    tabSheet {
      setSizeFull()
      tab {
        painelImprimir()
      }.apply {
        val button = Button(TAB_IMPRESSO) {
          viewModel.updateGridImprimir()
        }
        button.addThemeVariants(ButtonVariant.LUMO_SMALL)
        this.addComponentAsFirst(button)
      }
      tab {
        painelImpressoSemNota()
      }.apply {
        val button = Button(TAB_SEM_NOTA) {
          viewModel.updateGridImpressoSemNota()
        }
        button.addThemeVariants(ButtonVariant.LUMO_SMALL)
        this.addComponentAsFirst(button)
      }
      tab {
        painelPendente()
      }.apply {
        val button = Button(TAB_PENDENTE) {
          viewModel.updateGridPendente()
        }
        button.addThemeVariants(ButtonVariant.LUMO_SMALL)
        this.addComponentAsFirst(button)
      }
      tab {
        painelImpressoComNota()
      }.apply {
        val button = Button(TAB_COM_NOTA) {
          viewModel.updateGridImpressoComNota()
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
            viewModel.imprimir()
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
          addValueChangeListener {
            viewModel.updateGridImprimir()
          }
        }
        cmbAreaImprimir = textField("Área") {
          this.valueChangeMode = TIMEOUT
          addValueChangeListener {
            viewModel.updateGridImprimir()
          }
        }
        cmbRotaImprimir = textField("Rota") {
          this.valueChangeMode = TIMEOUT
          
          addValueChangeListener {
            viewModel.updateGridImprimir()
          }
        }
      }
      gridPedidosEntregaImprimir = this.grid(dataProvider = dataProviderProdutosImprimir) {
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
        if(UserSaci.userAtual?.admin == true)
          button("Desmarcar") {
            icon = CLOSE.create()
            addClickListener {
              viewModel.desmarcaSemNota()
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
      gridPedidosEntregaImpressoSemNota = grid(dataProvider = dataProviderProdutosImpressoSemNota) {
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
          addValueChangeListener {
            viewModel.updateGridPendente()
          }
        }
        cmbAreaPendente = textField("Área") {
          this.valueChangeMode = TIMEOUT
          addValueChangeListener {
            viewModel.updateGridPendente()
          }
        }
        cmbRotaPendente = textField("Rota") {
          this.valueChangeMode = TIMEOUT
          
          addValueChangeListener {
            viewModel.updateGridPendente()
          }
        }
      }
      gridPedidosEntregaPendente = this.grid(dataProvider = dataProviderProdutosPendente) {
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
        if(UserSaci.userAtual?.admin == true)
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
      
      gridPedidosEntregaImpressoComNota = grid(dataProvider = dataProviderProdutosImpressoComNota) {
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
  
  private fun @VaadinDsl Grid<PedidoEntrega>.addColumnSeq(label: String) {
    addColumn {
      /*val listDataProvider = (dataProvider as ListDataProvider)
      listDataProvider.sortComparator
      val query = Query<PedidoEntrega, SerializablePredicate<PedidoEntrega>>(0,
                                                                             Int.Companion.MAX_VALUE,
                                                                             emptyList<QuerySortOrder>(),
                                                                             null,
                                                                             PedicateTrue<PedidoEntrega>())
      val lista = listDataProvider.getAll()
      lista.indexOf(it) + 1
      
       */
      val lista = list(this)
      //lista.indexOf(it) + 1
      //val lista = (this.dataProvider as ListDataProvider).items
      lista.indexOf(it) + 1
    }.apply {
      this.textAlign = END
      isAutoWidth = true
      setHeader(label)
    }
  }
  
  var pedidoInicial: PedidoEntrega? = null
  var pedidoFinal: PedidoEntrega? = null
  
  private fun @VaadinDsl Grid<PedidoEntrega>.shiftSelect() {
    this.addItemClickListener {evento ->
      val grade = evento.source
      if(evento.isShiftKey) {
        val pedido = evento.item
        if(pedidoInicial == null) {
          pedidoInicial = pedido
          grade.select(pedido)
        }
        else {
          if(pedidoFinal == null) {
            val itens = list(grade)
            pedidoFinal = pedido
            val p1 = itens.indexOf(pedidoInicial!!)
            val p2 = itens.indexOf(pedidoFinal!!) + 1
            val subList = itens.subList(p1.coerceAtMost(p2), p1.coerceAtLeast(p2))
            subList.forEach {
              grade.select(it)
            }
            pedidoFinal = null
            pedidoInicial = null
          }
          else {
            pedidoFinal = null
            pedidoInicial = null
          }
        }
      }
      else {
        pedidoFinal = null
        pedidoInicial = null
      }
    }
  }
  
  private fun list(grade: Grid<PedidoEntrega>): List<PedidoEntrega> {
    val dataProvider = grade.dataProvider as ListDataProvider
    val filter = dataProvider.filter
    val queryOrdem = comparator(grade)
    return dataProvider.items.toList()
      .filter {
        filter?.test(it) ?: true
      }
      .let {list ->
        if(queryOrdem == null) list
        else list.sortedWith<PedidoEntrega>(queryOrdem)
      }
  }
  
  private fun comparator(grade: Grid<PedidoEntrega>): Comparator<PedidoEntrega>? {
    if(grade.sortOrder.isEmpty()) return null
    val sortOrder = grade.sortOrder
    return comparator(sortOrder)
  }
  
  private fun comparator(sortOrder: List<GridSortOrder<PedidoEntrega>>): Comparator<PedidoEntrega>? {
    return sortOrder.flatMap {gridSort ->
      val sortOrdem =
        gridSort.sorted.getSortOrder(gridSort.direction)
          .toList()
      val propsBean = PedidoEntrega::class.members.toList()
        .filterIsInstance<KProperty1<PedidoEntrega, Comparable<*>>>()
      val props = sortOrdem.mapNotNull {querySortOrder ->
        propsBean.firstOrNull {prop ->
          prop.name == querySortOrder.sorted
        }
      }
      props.map {prop ->
        if(gridSort.direction == DESCENDING)
          compareByDescending<PedidoEntrega> {
            prop.get(it)
          }
        else
          compareBy<PedidoEntrega> {
            prop.get(it)
          }
      }
    }
      .reduce {acc, comparator ->
        acc.thenComparing(comparator)
      }
  }
  
  override fun updateGridImprimir(itens: List<PedidoEntrega>) {
    gridPedidosEntregaImprimir.deselectAll()
    dataProviderProdutosImprimir.items.clear()
    dataProviderProdutosImprimir.items.addAll(itens.sortedBy {it.hashCode()})
    dataProviderProdutosImprimir.refreshAll()
  }
  
  override fun updateGridPendente(itens: List<PedidoEntrega>) {
    gridPedidosEntregaPendente.deselectAll()
    dataProviderProdutosPendente.items.clear()
    dataProviderProdutosPendente.items.addAll(itens.sortedBy {it.hashCode()})
    dataProviderProdutosPendente.refreshAll()
  }
  
  override fun updateGridImpressoComNota(itens: List<PedidoEntrega>) {
    gridPedidosEntregaImpressoComNota.deselectAll()
    dataProviderProdutosImpressoComNota.items.clear()
    dataProviderProdutosImpressoComNota.items.addAll(itens)
    dataProviderProdutosImpressoComNota.refreshAll()
  }
  
  override fun updateGridImpressoSemNota(itens: List<PedidoEntrega>) {
    gridPedidosEntregaImpressoSemNota.deselectAll()
    dataProviderProdutosImpressoSemNota.items.clear()
    dataProviderProdutosImpressoSemNota.items.addAll(itens)
    dataProviderProdutosImpressoSemNota.refreshAll()
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
    get() = cmbAreaImprimir.value?.toUpperCase() ?: ""
  override val rotaImprimir: String
    get() = cmbRotaImprimir.value?.toUpperCase() ?: ""
  override val pedidoPendente: Int
    get() = edtPedidoPendente.value?.toIntOrNull() ?: 0
  override val dataPendente: LocalDate?
    get() = edtDataPendente.value
  override val areaPendente: String
    get() = cmbAreaPendente.value?.toUpperCase() ?: ""
  override val rotaPendente: String
    get() = cmbRotaPendente.value?.toUpperCase() ?: ""
  
  companion object {
    const val TAB_IMPRESSO: String = "Imprimir"
    const val TAB_PENDENTE: String = "Entrega Pendente"
    const val TAB_COM_NOTA: String = "Impresso com Nota"
    const val TAB_SEM_NOTA: String = "Impresso sem Nota"
  }
}

class PedicateTrue<T>: SerializablePredicate<T> {
  override fun test(p0: T?): Boolean = true
}
