package br.com.astrosoft.framework.view

import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.init
import com.vaadin.flow.component.HasComponents
import org.vaadin.gatanaso.MultiselectComboBox

@VaadinDsl
fun <T> (@VaadinDsl HasComponents).multiselectComboBox(block: (@VaadinDsl MultiselectComboBox<T>).() -> Unit = {}) =
  init(MultiselectComboBox(), block)