package br.com.astrosoft.pedidoEntrega.model

import br.com.astrosoft.pedidoEntrega.model.beans.UserSaci
import br.com.astrosoft.framework.model.QueryDB
import br.com.astrosoft.framework.util.DB
import br.com.astrosoft.pedidoEntrega.model.beans.PedidoEntrega

class QuerySaci: QueryDB(driver, url, username, password) {
  fun findUser(login: String?): UserSaci? {
    login ?: return null
    val sql = "/sqlSaci/userSenha.sql"
    return query(sql, UserSaci::class) {
      addParameter("login", login)
    }.firstOrNull()
      ?.initVars()
  }
  
  fun findAllUser(): List<UserSaci> {
    val sql = "/sqlSaci/userSenha.sql"
    return query(sql, UserSaci::class) {
      addParameter("login", "TODOS")
    }.map {user ->
      user.initVars()
    }
  }
  
  fun updateUser(user: UserSaci) {
    val sql = "/sqlSaci/updateUser.sql"
    script(sql) {
      addOptionalParameter("login", user.login)
      addOptionalParameter("bitAcesso", user.bitAcesso())
    }
  }
  
  fun listaPedido(): List<PedidoEntrega> {
    val sql = "/sqlSaci/listaPedido.sql"
    return query(sql, PedidoEntrega::class)
  }
  
  fun ativaMarca(storeno : Int, ordno : Int, marca : String) {
    val sql = "/sqlSaci/ativaMarca.sql"
    script(sql) {
      addOptionalParameter("storeno", storeno)
      addOptionalParameter("ordno", ordno)
      addOptionalParameter("marca", marca)
    }
  }
  
  
  companion object {
    private val db = DB("saci")
    internal val driver = db.driver
    internal val url = db.url
    internal val username = db.username
    internal val password = db.password
    internal val test = db.test
    val ipServer =
      url.split("/")
        .getOrNull(2)
  }
}

val saci = QuerySaci()