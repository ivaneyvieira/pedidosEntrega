package br.com.astrosoft

import br.com.astrosoft.framework.view.ViewUtil

class AppConfig {
  private val versao = ViewUtil.versao
  private val title = "Boleto Crediário"
  private val shortName = "Boleto"
  private val iconPath = "icons/logoPintos.png"
  
  fun loginInfo() = LoginInfo(title, shortName, versao, iconPath)
  // fun loginFormApp() = LoginFormApp()
  
  companion object {
    private val instance = AppConfig()
    val loginInfo = instance.loginInfo()
    //val loginFormApp get() = instance.loginFormApp()
  }
}

open class LoginInfo(val appName: String, val shortName: String, val version: String, val iconPath: String)