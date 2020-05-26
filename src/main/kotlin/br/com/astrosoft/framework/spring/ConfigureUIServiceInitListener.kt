package br.com.astrosoft.framework.spring

import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.server.ServiceInitEvent
import com.vaadin.flow.server.UIInitEvent
import com.vaadin.flow.server.VaadinServiceInitListener
import org.springframework.stereotype.Component

@Component
class ConfigureUIServiceInitListener: VaadinServiceInitListener {
  override fun serviceInit(event: ServiceInitEvent) {
    event.source
      .addUIInitListener {uiEvent: UIInitEvent ->
        val ui = uiEvent.ui
        ui.addBeforeEnterListener {event: BeforeEnterEvent ->
          authenticateNavigation(event)
        }
      }
  }
  
  private fun authenticateNavigation(event: BeforeEnterEvent) {
    if(canReroute(event))
      event.rerouteTo(LoginView::class.java)
  }
  
  private fun canReroute(event: BeforeEnterEvent) = LoginView::class.java != event.navigationTarget
                                                    && !SecurityUtils.isUserLoggedIn
}