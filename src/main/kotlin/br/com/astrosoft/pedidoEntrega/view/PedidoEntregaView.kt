package br.com.astrosoft.pedidoEntrega.view

import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.framework.view.addColumnDate
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnString
import br.com.astrosoft.framework.view.addColumnTime
import br.com.astrosoft.pedidoEntrega.model.beans.PedidoEntrega
import br.com.astrosoft.pedidoEntrega.model.beans.UserSaci
import br.com.astrosoft.pedidoEntrega.viewmodel.IPedidoEntregaView
import br.com.astrosoft.pedidoEntrega.viewmodel.PedidoEntregaViewModel
import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.contents
import com.github.mvysny.karibudsl.v10.grid
import com.github.mvysny.karibudsl.v10.isExpand
import com.github.mvysny.karibudsl.v10.tabSheet
import com.github.mvysny.karibudsl.v10.textField
import com.github.mvysny.karibudsl.v10.verticalLayout
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode
import com.vaadin.flow.component.grid.GridVariant.LUMO_COMPACT
import com.vaadin.flow.component.icon.VaadinIcon.PRINT
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.textfield.TextFieldVariant.LUMO_SMALL
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.provider.SortDirection.DESCENDING
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import kotlin.reflect.KProperty1

@Route(layout = AppPedidoLayout::class)
@PageTitle("Pedidos")
@HtmlImport("frontend://styles/shared-styles.html")
class PedidoEntregaView: ViewLayout<PedidoEntregaViewModel>(), IPedidoEntregaView {
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
      tab("Imprimir"){
        painelImprimir()
      }
      tab("Impresso sem nota") {
        painelImpressoSemNota()
      }
      tab("Impresso com nota") {
        painelImpressoComNota()
      }
    }
  }
  
  fun HasComponents.painelImprimir(): VerticalLayout {
    return verticalLayout {
      this.setSizeFull()
      isMargin = false
      isPadding = false
      edtPedidoImprimir = textField("Numero Pedido") {
        placeholder = "Pressione Enter"
        this.addThemeVariants(LUMO_SMALL)
        this.isAutofocus = true
        addValueChangeListener {event ->
          viewModel.updateGridImprimir()
        }
      }
      gridPedidosEntregaImprimir = this.grid(dataProvider = dataProviderProdutosImprimir) {
        isExpand = true
        isMultiSort = true
        addThemeVariants(LUMO_COMPACT)
        setSelectionMode(SelectionMode.MULTI)
        
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
  
  fun HasComponents.painelImpressoSemNota(): VerticalLayout {
    return verticalLayout {
      this.setSizeFull()
      isMargin = false
      isPadding = false
      
      edtPedidoImpressoSemNota = textField("Numero Pedido") {
        placeholder = "Pressione Enter"
        this.addThemeVariants(LUMO_SMALL)
        this.isAutofocus = true
        addValueChangeListener {event ->
          viewModel.updateGridImpressoSemNota()
        }
      }
      gridPedidosEntregaImpressoSemNota = grid(dataProvider = dataProviderProdutosImpressoSemNota) {
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
        addValueChangeListener {event ->
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
    val filter = dataProviderProdutosImprimir.filter
    val queryOrdem = comparator(grade)
    return dataProviderProdutosImprimir.items.toList()
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
}
