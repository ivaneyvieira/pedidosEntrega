package br.com.astrosoft.pedidoEntrega.view

import br.com.astrosoft.pedidoEntrega.model.beans.UserSaci
import br.com.astrosoft.pedidoEntrega.viewmodel.DefautlViewModel
import br.com.astrosoft.pedidoEntrega.viewmodel.IDefaultView
import br.com.astrosoft.framework.view.ViewLayout
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

//@Route(value = "", layout = AppPedidoLayout::class)
//@PageTitle("")
class DefaultView: ViewLayout<DefautlViewModel>(), IDefaultView {
  override val viewModel = DefautlViewModel(this)
  
  override fun beforeEnter(event: BeforeEnterEvent?) {
    event?.forwardTo(PedidoEntregaView::class.java)
    super.beforeEnter(event)
  }
  
  override fun isAccept(user: UserSaci): Boolean {
    return true
  }
}