package br.com.astrosoft.framework.model

import br.com.astrosoft.framework.util.SystemUtils

object RegistryUserInfo {
  var loginInfoProvider: LoginInfoProvider? = null
  val version = SystemUtils.readFile("/versao.txt") ?: "1.0"
  const val appName = "Pedido para Entrega"
  const val commpany = "Engecopi"
  val usuario
    get() = loginInfoProvider?.loginInfo?.usuario
}