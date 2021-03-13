package br.com.astrosoft.framework.view

import com.vaadin.flow.component.charts.model.style.SolidColor
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.router.RouterLink

class ItemMenu(route: RouterLink) : Div() {
  private var selected = false

  init {
    style.set("padding", "5px 10px")
    add(route)
  }

  var select = selected
    get() = selected
    set(value) {
      field = value
      updateSelect()
    }

  private fun updateSelect() {
    if (select) {
      style.set("backgroundColor", SolidColor.LIGHTBLUE.toString()).set("color", "white")
    }
    else style.clear()
  }
}
