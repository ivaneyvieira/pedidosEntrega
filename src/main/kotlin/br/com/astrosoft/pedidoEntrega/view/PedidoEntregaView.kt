package br.com.astrosoft.pedidoEntrega.view

import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.framework.view.addColumnDate
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnString
import br.com.astrosoft.framework.view.addColumnTime
import br.com.astrosoft.framework.view.right
import br.com.astrosoft.framework.view.selectedChange
import br.com.astrosoft.pedidoEntrega.model.beans.PedidoEntrega
import br.com.astrosoft.pedidoEntrega.model.beans.UserSaci
import br.com.astrosoft.pedidoEntrega.viewmodel.IPedidoEntregaView
import br.com.astrosoft.pedidoEntrega.viewmodel.PedidoEntregaViewModel
import com.github.mvysny.karibudsl.v10.TabSheet
import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.comboBox
import com.github.mvysny.karibudsl.v10.contents
import com.github.mvysny.karibudsl.v10.datePicker
import com.github.mvysny.karibudsl.v10.grid
import com.github.mvysny.karibudsl.v10.horizontalLayout
import com.github.mvysny.karibudsl.v10.isExpand
import com.github.mvysny.karibudsl.v10.tabSheet
import com.github.mvysny.karibudsl.v10.textField
import com.github.mvysny.karibudsl.v10.verticalLayout
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.grid.ColumnTextAlign.END
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode
import com.vaadin.flow.component.grid.GridVariant.LUMO_COMPACT
import com.vaadin.flow.component.icon.VaadinIcon.PRINT
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.tabs.Tabs.SelectedChangeEvent
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.textfield.TextFieldVariant.LUMO_SMALL
import com.vaadin.flow.data.provider.DataProvider
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.provider.SortDirection.DESCENDING
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import java.time.LocalDate
import kotlin.reflect.KProperty1

@Route(layout = AppPedidoLayout::class)
@PageTitle("Pedidos")
@HtmlImport("frontend://styles/shared-styles.html")
class PedidoEntregaView: ViewLayout<PedidoEntregaViewModel>(), IPedidoEntregaView {
  private lateinit var cmbRotaImprimir: TextField
  private val listAreaImprimir = ListDataProvider<String>(mutableListOf())
  private val listRotaImprimir = ListDataProvider<String>(mutableListOf())
  private lateinit var cmbAreaImprimir: TextField
  private lateinit var edtDataImprimir: DatePicker
  private lateinit var edtPedidoImpressoSemNota: TextField
  private lateinit var edtPedidoImpressoComNota: TextField
  private lateinit var edtPedidoImprimir: TextField
  private lateinit var gridPedidosEntregaImpressoComNota: Grid<PedidoEntrega>
  private lateinit var gridPedidosEntregaImpressoSemNota: Grid<PedidoEntrega>
  private lateinit var gridPedidosEntregaImprimir: Grid<PedidoEntrega>
  override val viewModel: PedidoEntregaViewModel = PedidoEntregaViewModel(this)
  private val dataProviderProdutosImprimir = ListDataProvider<PedidoEntrega>(mutableListOf())
  private val dataProviderProdutosImpressoSemNota = ListDataProvider<PedidoEntrega>(mutableListOf())
  private val dataProviderProdutosImpressoComNota = ListDataProvider<PedidoEntrega>(mutableListOf())
  
  override fun isAccept(user: UserSaci) = true
  
  init {
    tabSheet {
      setSizeFull()
      val tab1 = tab(Companion.TAB_IMPRESSO) {
        painelImprimir()
      }
      val tab2 = tab("Impresso sem nota") {
        painelImpressoSemNota()
      }
      val tab3 = tab("Impresso com nota") {
        painelImpressoComNota()
      }
      this.selectedChange {event->
        event.apply {
          when(source.selectedTab) {
            tab1 -> viewModel.updateGridImprimir()
            tab2 -> viewModel.updateGridImpressoSemNota()
            tab3 -> viewModel.updateGridImpressoComNota()
          }
        }
      }
    }

  }
  
  fun HasComponents.painelImprimir(): VerticalLayout {
    return verticalLayout {
      this.setSizeFull()
      isMargin = false
      isPadding = false
      horizontalLayout {
        edtPedidoImprimir = textField("Numero Pedido") {
          placeholder = "Pressione Enter"
          this.isAutofocus = true
          addValueChangeListener {
            viewModel.updateGridImprimir()
          }
        }
        edtDataImprimir = datePicker("Data") {
          placeholder = "Pressione Enter"
          addValueChangeListener {
            viewModel.updateGridImprimir()
          }
        }
        cmbAreaImprimir = textField("Área") {
          placeholder = "Pressione Enter"
          this.isAutofocus = true
          addValueChangeListener {
            viewModel.updateGridImprimir()
          }
        }
        cmbRotaImprimir = textField("Rota") {
          placeholder = "Pressione Enter"
          this.isAutofocus = true
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
        addColumnDate(PedidoEntrega::data) {
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
        addColumnDate(PedidoEntrega::dataFat) {
          this.setHeader("Data")
        }
        addColumnTime(PedidoEntrega::horaFat) {
          this.setHeader("Hora")
        }
        
        addColumnString(PedidoEntrega::nfEnt) {
          this.setHeader("NF Ent")
        }
        addColumnDate(PedidoEntrega::dataEnt) {
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
      this.toolbar {
        isExpand = false
        button("Imprimir") {
          icon = PRINT.create()
          addClickListener {
            viewModel.imprimir()
          }
        }
      }
      viewModel.updateGridImprimir()
    }
  }
  
  private fun @VaadinDsl Grid<PedidoEntrega>.addColumnSeq(label: String) {
    addColumn {
      list(this).indexOf(it) + 1
    }.apply {
      this.textAlign = END
      isAutoWidth = true
      setHeader(label)
    }
  }
  
  fun HasComponents.painelImpressoSemNota(): VerticalLayout {
    return verticalLayout {
      this.setSizeFull()
      isMargin = false
      isPadding = false
      
      edtPedidoImpressoSemNota = textField("Numero Pedido") {
        placeholder = "Pressione Enter"
        this.addThemeVariants(LUMO_SMALL)
        this.isAutofocus = true
        addValueChangeListener {
          viewModel.updateGridImpressoSemNota()
        }
      }
      gridPedidosEntregaImpressoSemNota = grid(dataProvider = dataProviderProdutosImpressoSemNota) {
        this.isExpand = true
        isMultiSort = true
        addThemeVariants(LUMO_COMPACT)
        
        addColumnSeq("Num")
        addColumnInt(PedidoEntrega::loja) {
          this.setHeader("Loja")
        }
        addColumnInt(PedidoEntrega::pedido) {
          this.setHeader("Pedido")
        }
        addColumnDate(PedidoEntrega::data) {
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
        addColumnDate(PedidoEntrega::dataFat) {
          this.setHeader("Data")
        }
        addColumnTime(PedidoEntrega::horaFat) {
          this.setHeader("Hora")
        }
        
        addColumnString(PedidoEntrega::nfEnt) {
          this.setHeader("NF Ent")
        }
        addColumnDate(PedidoEntrega::dataEnt) {
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
      }
      viewModel.updateGridImpressoSemNota()
    }
  }
  
  fun HasComponents.painelImpressoComNota(): VerticalLayout {
    return verticalLayout {
      this.setSizeFull()
      isMargin = false
      isPadding = false
      
      edtPedidoImpressoComNota = textField("Numero Pedido") {
        placeholder = "Pressione Enter"
        this.addThemeVariants(LUMO_SMALL)
        this.isAutofocus = true
        addValueChangeListener {
          viewModel.updateGridImpressoComNota()
        }
      }
      gridPedidosEntregaImpressoComNota = grid(dataProvider = dataProviderProdutosImpressoComNota) {
        this.isExpand = true
        isMultiSort = true
        addThemeVariants(LUMO_COMPACT)
        
        addColumnInt(PedidoEntrega::loja) {
          this.setHeader("Loja")
        }
        addColumnInt(PedidoEntrega::pedido) {
          this.setHeader("Pedido")
        }
        addColumnDate(PedidoEntrega::data) {
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
        addColumnDate(PedidoEntrega::dataFat) {
          this.setHeader("Data")
        }
        addColumnTime(PedidoEntrega::horaFat) {
          this.setHeader("Hora")
        }
        
        addColumnString(PedidoEntrega::nfEnt) {
          this.setHeader("NF Ent")
        }
        addColumnDate(PedidoEntrega::dataEnt) {
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
        //shiftSelect()
      }
      viewModel.updateGridImpressoComNota()
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
    return grade.sortOrder.mapNotNull {gridSort ->
      val prop = PedidoEntrega::class.members.toList()
        .filterIsInstance<KProperty1<PedidoEntrega, Comparable<*>>>()
        .firstOrNull {prop ->
          prop.name == gridSort.sorted.key
        }
      if(gridSort.direction == DESCENDING)
        compareByDescending {
          prop?.get(it)
        }
      else
        compareBy<PedidoEntrega> {
          prop?.get(it)
        }
    }
      .reduce {acc, comparator ->
        acc.thenComparing(comparator)
      }
  }
  
  override fun updateGridImprimir(itens: List<PedidoEntrega>) {
    dataProviderProdutosImprimir.items.clear()
    dataProviderProdutosImprimir.items.addAll(itens)
    dataProviderProdutosImprimir.refreshAll()
  }
  
  override fun updateGridImpressoComNota(itens: List<PedidoEntrega>) {
    dataProviderProdutosImpressoComNota.items.clear()
    dataProviderProdutosImpressoComNota.items.addAll(itens)
    dataProviderProdutosImpressoComNota.refreshAll()
  }
  
  override fun updateGridImpressoSemNota(itens: List<PedidoEntrega>) {
    dataProviderProdutosImpressoSemNota.items.clear()
    dataProviderProdutosImpressoSemNota.items.addAll(itens)
    dataProviderProdutosImpressoSemNota.refreshAll()
  }
  
  override fun itensSelecionado(): List<PedidoEntrega> {
    return gridPedidosEntregaImprimir.selectedItems.toList()
  }
  
  override val pedidoImprimir: Int
    get() = edtPedidoImprimir.value?.toIntOrNull() ?: 0
  override val pedidoImpressoSemNota: Int
    get() = edtPedidoImpressoSemNota.value?.toIntOrNull() ?: 0
  override val pedidoImpressoComNota: Int
    get() = edtPedidoImpressoComNota.value?.toIntOrNull() ?: 0
  override val dataImprimir: LocalDate?
    get() = edtDataImprimir.value
  override val areaImprimir: String
    get() = cmbAreaImprimir.value ?: ""
  override val rotaImprimir: String
    get() = cmbRotaImprimir.value ?: ""
  
  override fun updateComboAreaImprimir(itens: List<String>) {
    listAreaImprimir.items.clear()
    listAreaImprimir.items.addAll(itens)
    listAreaImprimir.refreshAll()
  }
  
  override fun updateComboRotaImprimir(itens: List<String>) {
    listRotaImprimir.items.clear()
    listRotaImprimir.items.addAll(itens)
    listRotaImprimir.refreshAll()
  }
  
  companion object {
    const val TAB_IMPRESSO: String = "Imprimir"
  }
}
