package br.com.astrosoft.pedidoEntrega.view

import br.com.astrosoft.AppConfig
import com.github.mvysny.karibudsl.v10.anchor
import com.github.mvysny.karibudsl.v10.br
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.div
import com.github.mvysny.karibudsl.v10.drawer
import com.github.mvysny.karibudsl.v10.drawerToggle
import com.github.mvysny.karibudsl.v10.h3
import com.github.mvysny.karibudsl.v10.horizontalLayout
import com.github.mvysny.karibudsl.v10.hr
import com.github.mvysny.karibudsl.v10.icon
import com.github.mvysny.karibudsl.v10.isExpand
import com.github.mvysny.karibudsl.v10.item
import com.github.mvysny.karibudsl.v10.label
import com.github.mvysny.karibudsl.v10.listBox
import com.github.mvysny.karibudsl.v10.menuBar
import com.github.mvysny.karibudsl.v10.navbar
import com.github.mvysny.karibudsl.v10.routerLink
import com.github.mvysny.karibudsl.v10.tab
import com.github.mvysny.karibudsl.v10.tabs
import com.github.mvysny.karibudsl.v10.verticalLayout
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY
import com.vaadin.flow.component.button.ButtonVariant.LUMO_SMALL
import com.vaadin.flow.component.button.ButtonVariant.LUMO_TERTIARY
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode.END
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.server.PWA
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo

@Theme(value = Lumo::class, variant = Lumo.DARK)
@Push
@PWA(name = AppConfig.title,
     shortName = AppConfig.shortName,
     iconPath = AppConfig.iconPath,
     enableInstallPrompt = false)
class PedidoEntregaLayout: AppLayout() {
  init {
    isDrawerOpened = true
    navbar {
      drawerToggle()
      h3(AppConfig.title)
      horizontalLayout {
        isExpand = true
      }
      anchor("logout", "Sair")
    }
    drawer {
      verticalLayout {
        label("Versão ${AppConfig.version}")
        label(AppConfig.userSaci?.login)
      }
      hr()
      /*
      tabs {
        orientation = Tabs.Orientation.VERTICAL
        tabPedido = tab {
          this.icon(VaadinIcon.FORM)
          routerLink(text = "Pedido", viewType = PedidoEntregaView::class)
        }
      }
      tabs {
        orientation = Tabs.Orientation.VERTICAL
        tabUser = tab {
          this.isEnabled = AppConfig.userSaci?.admin ?: false
          this.icon(VaadinIcon.USER)
          routerLink(text = "Usuário", viewType = UsuarioView::class)
        }
      }
       */
      verticalLayout {
        width = "100%"
        isSpacing = false
        button("Pedidos") {
          width = "100%"
          icon = VaadinIcon.FORM.create()
          this.addThemeVariants(LUMO_PRIMARY , LUMO_SMALL)
        }
        button("Usuários") {
          width = "100%"
          icon = VaadinIcon.USER.create()
          this.addThemeVariants(LUMO_TERTIARY, LUMO_SMALL)
        }
        button("Sair") {
          width = "100%"
          icon = VaadinIcon.CLOSE_CIRCLE.create()
          this.addThemeVariants(LUMO_TERTIARY, LUMO_SMALL)
        }
        listBox<String> {
          width = "100%"
          this.add("Menu 0")
          this.setItems("Menu 0", "Menu 1", "Menu 0")
        }
      }
    }
  }
}