package br.com.astrosoft.framework.view

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasSize
import com.vaadin.flow.component.Tag
import com.vaadin.flow.server.StreamResource

@Tag("object")
class PDFViewer private constructor() : Component(), HasSize {
  constructor(resource: StreamResource?) : this() {
    element.setAttribute("data", resource)
  }

  init {
    element.setAttribute("type", "application/pdf")
    setSizeFull()
  }
}