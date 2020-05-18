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
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.grid
import com.github.mvysny.karibudsl.v10.isExpand
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode
import com.vaadin.flow.component.grid.GridVariant.LUMO_COMPACT
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.icon.VaadinIcon.PRINT
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = AppPedidoLayout::class)
@PageTitle("Pedidos")
@HtmlImport("frontend://styles/shared-styles.html")
class PedidoEntregaView: ViewLayout<PedidoEntregaViewModel>(), IPedidoEntregaView {
  private lateinit var lblGravado: Label
  private var gridPedidosEntrega: Grid<PedidoEntrega>
  private lateinit var edtPedido: IntegerField
  override val viewModel: PedidoEntregaViewModel = PedidoEntregaViewModel(this)
  private val dataProviderProdutos = ListDataProvider<PedidoEntrega>(mutableListOf())
  
  override fun isAccept(user: UserSaci) = true
  
  init {
    form("Editar pedidos")
    gridPedidosEntrega = grid(dataProvider = dataProviderProdutos) {
      isExpand = true
      isMultiSort = true
      addThemeVariants(LUMO_COMPACT)
      setSelectionMode(SelectionMode.MULTI)
  
      addColumnInt(PedidoEntrega::loja){
        this.setHeader("Loja")
      }
      addColumnInt(PedidoEntrega::pedido){
        this.setHeader("Pedido")
      }
      addColumnDate(PedidoEntrega::data){
        this.setHeader("Data")
      }
      addColumnTime(PedidoEntrega::hora){
        this.setHeader("Hora")
      }
      addColumnString(PedidoEntrega::area){
        this.setHeader("Área")
      }
      addColumnString(PedidoEntrega::rota){
        this.setHeader("Rota")
      }
  
      addColumnString(PedidoEntrega::nfFat){
        this.setHeader("NF Fat")
      }
      addColumnDate(PedidoEntrega::dataFat){
        this.setHeader("Data")
      }
      addColumnTime(PedidoEntrega::horaFat){
        this.setHeader("Hora")
      }
  
      addColumnString(PedidoEntrega::nfEnt){
        this.setHeader("NF Ent")
      }
      addColumnDate(PedidoEntrega::dataEnt){
        this.setHeader("Data")
      }
      addColumnTime(PedidoEntrega::horaEnt){
        this.setHeader("Hora")
      }
  
      addColumnInt(PedidoEntrega::vendno){
        this.setHeader("Vendedor")
      }
      addColumnDouble(PedidoEntrega::frete){
        this.setHeader("R$ Frete")
      }
      addColumnDouble(PedidoEntrega::valor){
        this.setHeader("R$ Nota")
      }
      addColumnInt(PedidoEntrega::custno){
        this.setHeader("Cliente")
      }
      addColumnString(PedidoEntrega::obs){
        this.setHeader("Obs")
      }
      addColumnString(PedidoEntrega::username){
        this.setHeader("Usuário")
      }
      shiftSelect()
    }
    toolbar {
      button("Imprimir") {
        icon = PRINT.create()
        addClickListener {
          viewModel.imprimir()
        }
      }
    }
    viewModel.updateGrid()
  }
  
  override fun updateGrid(itens: List<PedidoEntrega>) {
    dataProviderProdutos.items.clear()
    dataProviderProdutos.items.addAll(itens)
    dataProviderProdutos.refreshAll()
  }
  
  override fun itensSelecionado(): List<PedidoEntrega> {
    return gridPedidosEntrega.selectedItems.toList()
  }
}
