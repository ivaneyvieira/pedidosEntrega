package br.com.astrosoft.pedido.view

import br.com.astrosoft.pedido.view.entrega.PedidoEntregaView
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.Route

@Route("")
class ViewEmpty : VerticalLayout(), BeforeEnterObserver {
  override fun beforeEnter(event: BeforeEnterEvent?) {
    if (event?.navigationTarget == ViewEmpty::class.java) event.forwardTo(PedidoEntregaView::class.java)
  }
}
  