package br.com.astrosoft.framework.view

import com.github.mvysny.karibudsl.v10.TabSheet
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant

fun <T: Component> TabSheet.tabPanel(tabPanel: TabPanel<T>) {
  this.tab {
    tabPanel.createComponent
    
  }
    .apply {
      val button = Button(tabPanel.label) {
        tabPanel.updateComponent()
      }
      button.addThemeVariants(ButtonVariant.LUMO_SMALL)
      this.addComponentAsFirst(button)
      tabPanel.updateComponent()
    }
}

interface TabPanel<T: Component> {
  val createComponent:  T
  val label: String
  fun updateComponent()
}

