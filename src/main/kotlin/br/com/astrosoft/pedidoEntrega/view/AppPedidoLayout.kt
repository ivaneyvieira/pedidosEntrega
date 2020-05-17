package br.com.astrosoft.pedidoEntrega.view

import br.com.astrosoft.framework.model.RegistryUserInfo
import com.github.appreciated.app.layout.behaviour.AppLayout
import com.github.appreciated.app.layout.behaviour.Behaviour.LEFT_RESPONSIVE
import com.github.appreciated.app.layout.builder.AppLayoutBuilder
import com.github.appreciated.app.layout.component.appbar.AppBarBuilder
import com.github.appreciated.app.layout.component.menu.left.builder.LeftAppMenuBuilder
import com.github.appreciated.app.layout.component.menu.left.items.LeftHeaderItem
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem
import com.github.appreciated.app.layout.component.menu.top.item.TopClickableItem
import com.github.appreciated.app.layout.entity.Section
import com.github.appreciated.app.layout.router.AppLayoutRouterLayout
import com.github.mvysny.karibudsl.v10.navigateToView
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.html.Hr
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.icon.VaadinIcon.CLOSE_CIRCLE
import com.vaadin.flow.component.icon.VaadinIcon.FORM
import com.vaadin.flow.component.icon.VaadinIcon.USER
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.server.PWA
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo
import kotlin.reflect.KClass

@Theme(value = Lumo::class, variant = Lumo.DARK)
@Push
@PWA(name = "Pedidos de venda",
     shortName = "Pedidos",
     iconPath = "icons/logo.png")
class AppPedidoLayout: AppLayoutRouterLayout() {
  init {
    init(atualizaMenu())
  }
  
  private fun atualizaMenu(): AppLayout? {
    val appMenu = appMenu()
    return appLayout(RegistryUserInfo.appName, appMenu)
  }
  
  private fun appLayout(title: String, appMenu: Component): AppLayout {
    return AppLayoutBuilder.get(LEFT_RESPONSIVE)
      .withTitle(title)
      .withAppBar(AppBarBuilder.get().add(TopClickableItem(null, CLOSE_CIRCLE.create()) {
        LoginService.logout()
        navigateToView(DefaultView::class)
      }).build())
      .withAppMenu(appMenu)
      .build()
  }
  
  companion object {
    private var menuComponent: Component? = null
    private var menuUsuario: LeftNavigationItem? = null
    private var menuEditar: LeftNavigationItem? = null
  
    fun updateLayout() {
      menuComponent?.children?.forEach {component ->
        (component as? LeftNavigationItem)?.let {lItem ->
          println(lItem)
        }
        println("OK")
      }
    }
  
    private fun Component.localeMenu(caption: String): Component? {
      println(this)
      this.children.forEach {
        it.localeMenu(caption)
      }
      return null
    }
    
    fun appMenu(): Component {
      val appMenu = headerMenu(RegistryUserInfo.commpany, "Versão ${RegistryUserInfo.version}")
      menuEditar = addMenu("Pedidos", FORM, PedidoEntregaView::class)
      menuUsuario = addMenu("Usuários", USER, UsuarioView::class)
      appMenu.add(menuEditar)
      appMenu.add(menuUsuario)
      menuComponent = appMenu.build()
      return menuComponent!!
    }
    
    private fun headerMenu(company: String, version: String) = LeftAppMenuBuilder.get()
      .addToSection(LeftHeaderItem(company, version, null), Section.HEADER).add(Hr())
    
    private fun addMenu(caption: String, icon: VaadinIcon, className: KClass<out Component>): LeftNavigationItem {
      return LeftNavigationItem(caption, icon, className.java)
    }
  }
}