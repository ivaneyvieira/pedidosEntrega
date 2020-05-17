package br.com.astrosoft.framework.view

import br.com.astrosoft.pedidoEntrega.model.beans.UserSaci
import br.com.astrosoft.pedidoEntrega.model.saci
import br.com.astrosoft.pedidoEntrega.view.LoginService
import br.com.astrosoft.framework.model.LoginInfo
import com.vaadin.flow.component.login.LoginI18n
import com.vaadin.flow.component.login.LoginI18n.Header
import com.vaadin.flow.component.login.LoginOverlay

class LoginFormApp(appName: String, version: String, val navigate: (UserSaci) -> Unit): LoginOverlay() {
  init {
    setI18n(loginI18n(appName, version))
    isError = false
    addLoginListener {loginEvent ->
      val user = saci.findUser(loginEvent.username)
      when {
        user == null                      -> logout()
        !user.ativo                       -> logout()
        user.senha == loginEvent.password -> login(user)
        else                              -> logout()
      }
    }
  }
  
  private fun logout() {
    LoginService.logout()
    isError = true
  }
  
  private fun login(user: UserSaci) {
    LoginService.login(LoginInfo(user.login ?: ""))
    navigate(user)
    close()
  }
  
  private fun loginI18n(appName: String, version: String) = LoginI18n.createDefault().apply {
    this.header = Header()
    this.header.title = appName
    this.header.description = version
    this.form.username = "Usuário"
    this.form.title = "Acesse a sua conta"
    this.form.submit = "Entrar"
    this.form.password = "Senha"
    this.form.forgotPassword = ""
    this.errorMessage.title = "Usuário/senha inválidos"
    this.errorMessage.message = "Confira seu usuário e senha e tente novamente."
    this.additionalInformation = ""
  }
}