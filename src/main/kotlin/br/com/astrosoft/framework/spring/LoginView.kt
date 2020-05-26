package br.com.astrosoft.framework.spring

import br.com.astrosoft.AppConfig
import com.github.mvysny.karibudsl.v10.h1
import com.github.mvysny.karibudsl.v10.h2
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode.CENTER
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo

@Route("login")
@PageTitle("Login")
@Theme(value = Lumo::class, variant = Lumo.DARK)
class LoginView: VerticalLayout(), BeforeEnterObserver {
  private val loginFormApp = LoginFormApp()
  
  override fun beforeEnter(beforeEnterEvent: BeforeEnterEvent) {
    if(isError(beforeEnterEvent))
      loginFormApp.isError = true
  }
  
  private fun isError(beforeEnterEvent: BeforeEnterEvent): Boolean {
    return beforeEnterEvent.location
      .queryParameters
      .parameters
      .getOrDefault("error", emptyList())
      .isNotEmpty()
  }
  
  init {
    addClassName("login-view")
    setSizeFull()
    justifyContentMode = CENTER
    alignItems = Alignment.CENTER
    loginFormApp.action = "login"
    add(loginFormApp)
  }
}