package br.com.astrosoft.framework.view

import br.com.astrosoft.pedidoEntrega.view.PedidoEntregaLayout
import com.github.mvysny.karibudsl.v10.h1
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route

@Route(layout = PedidoEntregaLayout::class)
class AccessNotAllowed : VerticalLayout() {
  init {
    h1("Acesso não permitido")
  }
}