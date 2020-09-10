package br.com.astrosoft.pedido.model

import br.com.astrosoft.pedido.model.beans.UserSaci
import br.com.astrosoft.framework.model.QueryDB
import br.com.astrosoft.framework.util.DB
import br.com.astrosoft.framework.util.toSaciDate
import br.com.astrosoft.pedido.model.beans.ETipoPedido
import br.com.astrosoft.pedido.model.beans.Entregador
import br.com.astrosoft.pedido.model.beans.EntregadorNotas
import br.com.astrosoft.pedido.model.beans.Pedido
import br.com.astrosoft.pedido.model.beans.ProdutoPedido
import java.time.LocalDate
import java.time.LocalTime

class QuerySaci: QueryDB(driver, url, username, password) {
  fun findUser(login: String?): UserSaci? {
    login ?: return null
    val sql = "/sqlSaci/userSenha.sql"
    return query(sql, UserSaci::class) {
      addParameter("login", login)
    }.firstOrNull()
  }
  
  fun findAllUser(): List<UserSaci> {
    val sql = "/sqlSaci/userSenha.sql"
    return query(sql, UserSaci::class) {
      addParameter("login", "TODOS")
    }
  }
  
  fun updateUser(user: UserSaci) {
    val sql = "/sqlSaci/updateUser.sql"
    script(sql) {
      addOptionalParameter("login", user.login)
      addOptionalParameter("bitAcesso", user.bitAcesso)
    }
  }
  
  fun listaPedido(tipo : ETipoPedido): List<Pedido> {
    val sql = "/sqlSaci/listaPedido.sql"
    return query(sql, Pedido::class){
      addOptionalParameter("tipo", tipo.sigla)
    }
  }
  
  fun ativaMarca(storeno : Int, ordno : Int, marca : String) {
    val sql = "/sqlSaci/ativaMarca.sql"
    script(sql) {
      addOptionalParameter("storeno", storeno)
      addOptionalParameter("ordno", ordno)
      addOptionalParameter("marca", marca)
    }
  }
  
  fun ativaDataHoraImpressao(storeno : Int, ordno : Int, data : LocalDate?, hora : LocalTime?) {
    val sql = "/sqlSaci/ativaDataHoraImpressao.sql"
    script(sql) {
      addParameter("storeno", storeno)
      addParameter("ordno", ordno)
      addParameter("data", data?.toSaciDate() ?: 0)
      addParameter("hora", hora ?: LocalTime.MIN)
    }
  }
  
  fun produtoPedido(storeno : Int, ordno : Int, tipo : String) : List<ProdutoPedido>{
    val sql = "/sqlSaci/produtoPedido.sql"
    return query(sql, ProdutoPedido::class) {
      addOptionalParameter("storeno", storeno)
      addOptionalParameter("ordno", ordno)
      addOptionalParameter("tipo", tipo)
    }
  }
  
  fun findEntregadores(dateI : LocalDate, dateF : LocalDate) : List<Entregador>{
    val sql = "/sqlSaci/entregadores.sql"
    return query(sql, Entregador::class) {
      addOptionalParameter("dateI", dateI.toSaciDate())
      addOptionalParameter("dateF", dateF.toSaciDate())
    }
  }
  
  fun findEntregadoresNotas(dateI : LocalDate, dateF : LocalDate, empno : Int) : List<EntregadorNotas>{
    val sql = "/sqlSaci/entregadoresNotas.sql"
    return query(sql, EntregadorNotas::class) {
      addOptionalParameter("dateI", dateI.toSaciDate())
      addOptionalParameter("dateF", dateF.toSaciDate())
      addOptionalParameter("empno", empno)
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