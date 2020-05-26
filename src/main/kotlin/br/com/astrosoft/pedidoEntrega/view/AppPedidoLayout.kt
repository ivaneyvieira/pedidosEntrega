package br.com.astrosoft.pedidoEntrega.view
/*
class AppPedidoLayout: AppLayoutRouterLayout() {
  init {
    init(atualizaMenu())
  }
  
  private fun atualizaMenu(): AppLayout? {
    val appMenu = appMenu()
    return appLayout(AppConfig.title, appMenu)
  }
  
  private fun appLayout(title: String, appMenu: Component): AppLayout {
    return AppLayoutBuilder.get(LEFT_RESPONSIVE)
      .withTitle(title)
      .withAppBar(AppBarBuilder.get().add(TopClickableItem(null, CLOSE_CIRCLE.create()) {
        //LoginService.logout()
        navigateToView(ViewEmpty::class)
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
      val appMenu = headerMenu(AppConfig.commpany, "Versão ${AppConfig.version}")
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
 */