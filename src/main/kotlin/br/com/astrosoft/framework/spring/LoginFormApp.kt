package br.com.astrosoft.framework.spring

import br.com.astrosoft.AppConfig
import com.vaadin.flow.component.login.LoginForm
import com.vaadin.flow.component.login.LoginI18n
import com.vaadin.flow.component.login.LoginI18n.Header

class LoginFormApp: LoginForm() {
  init {
    setI18n()
    isError = false
  }
  
  private fun setI18n() {
    setI18n(loginI18n())
  }
  
  private fun loginI18n() = LoginI18n.createDefault()
    .apply {
      this.header = Header()
      this.header.title = AppConfig.title
      this.header.description = AppConfig.version
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

