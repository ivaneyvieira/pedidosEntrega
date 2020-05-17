package br.com.astrosoft.pedidoEntrega.view

import br.com.astrosoft.framework.model.LoginInfo
import br.com.astrosoft.framework.model.LoginInfoProvider
import br.com.astrosoft.framework.model.RegistryUserInfo
import com.vaadin.flow.component.UI
import com.vaadin.flow.server.VaadinSession

object LoginService {
  fun login(loginInfo: LoginInfo) {
    SessionUitl.loginInfo = loginInfo
    RegistryUserInfo.loginInfoProvider = SessionLoginInfoProvider()
  }
  
  fun logout() {
    SessionUitl.loginInfo = null
    RegistryUserInfo.loginInfoProvider = null
  }
  
  fun isLogged(): Boolean {
    return SessionUitl.loginInfo != null
  }
}

class SessionLoginInfoProvider: LoginInfoProvider {
  override val loginInfo: LoginInfo?
    get() {
      return SessionUitl.loginInfo
    }
}

private object SessionUitl {
  private val ATTRIBUTE_NAME get() = "SESSION_ABASTECIMENTO${UI.getCurrent().csrfToken}"
  var loginInfo: LoginInfo?
    get() = VaadinSession.getCurrent().getAttribute(ATTRIBUTE_NAME) as? LoginInfo
    set(value) {
      VaadinSession.getCurrent()
        .setAttribute(ATTRIBUTE_NAME, value)
      UI.getCurrent()
        ?.page
    }
}