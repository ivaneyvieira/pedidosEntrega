package br.com.astrosoft.framework.view


import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.horizontalLayout
import com.github.mvysny.karibudsl.v10.isExpand
import com.vaadin.flow.component.grid.ColumnTextAlign.END
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant.LUMO_COMPACT
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.provider.ListDataProvider
import kotlin.reflect.KClass

abstract class TabPanelGrid<T: Any>: TabPanel<VerticalLayout> {
  private val dataProviderPanel = ListDataProvider<T>(mutableListOf())
  private var gridPanel: Grid<T> =  Grid(classPanel().java, false)
  protected abstract fun classPanel(): KClass<T>
  protected abstract fun HorizontalLayout.toolBarConfig()
  protected abstract fun Grid<T>.gridPanel()
  
  override val createComponent = VerticalLayout().apply {
    this.setSizeFull()
    isMargin = false
    isPadding = false
    horizontalLayout {
      setWidthFull()
      toolBarConfig()
    }
    
    gridPanel.apply {
      this.dataProvider = dataProviderPanel
      isExpand = true
      isMultiSort = true
      addThemeVariants(LUMO_COMPACT)
      gridPanel()
    }
    addAndExpand(gridPanel)
  }
  
  fun updateGrid(itens: List<T>) {
    gridPanel.deselectAll()
    dataProviderPanel.updateItens(itens)
  }
  
  fun itensSelecionado() = gridPanel.selectedItems.toList()
}