package br.com.astrosoft.framework.view

import com.github.mvysny.karibudsl.v10.horizontalLayout
import com.github.mvysny.karibudsl.v10.isExpand
import com.github.mvysny.kaributools.fetchAll
import com.vaadin.flow.component.grid.GridVariant.LUMO_COMPACT
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.treegrid.TreeGrid
import kotlin.reflect.KClass

abstract class TabPanelTree<T : Any>(classGrid: KClass<T>) : TabPanel<VerticalLayout> {
  //private val dataProviderPanel = TreeDataProvider<T>(TreeData())
  protected val gridPanel: TreeGrid<T> = TreeGrid()
  protected abstract fun HorizontalLayout.toolBarConfig()
  protected abstract fun TreeGrid<T>.gridPanel()

  override val createComponent = VerticalLayout().apply {
    this.setSizeFull()
    isMargin = false
    isPadding = false
    horizontalLayout {
      setWidthFull()
      toolBarConfig()
    }

    gridPanel.apply { // this.dataProvider = dataProviderPanel
      isExpand = true
      isMultiSort = true
      addThemeVariants(LUMO_COMPACT)
      gridPanel()
    }
    addAndExpand(gridPanel)
  }

  fun updateGrid(itens: List<T>, findChild: (T) -> List<T>) {
    gridPanel.deselectAll()
    gridPanel.setItems(itens, findChild)
  }

  fun listBeans() = gridPanel.dataProvider.fetchAll()

  fun itensSelecionados() = gridPanel.selectedItems.toList()
}