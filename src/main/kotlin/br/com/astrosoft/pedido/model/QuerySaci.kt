package br.com.astrosoft.pedido.model

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.model.QueryDB
import br.com.astrosoft.framework.util.DB
import br.com.astrosoft.framework.util.parserDate
import br.com.astrosoft.framework.util.toSaciDate
import br.com.astrosoft.pedido.model.beans.*
import br.com.astrosoft.pedido.viewmodel.entrega.EZonaCarga
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class QuerySaci : QueryDB(driver, url, username, password) {
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
      addOptionalParameter("loja", user.storeno)
      addOptionalParameter("impressoraTermica", user.impressoraTermica)
    }
  }

  fun listaPedido(filtro: FiltroPedido): List<Pedido> {
    val sql = "/sqlSaci/listaPedido.sql"
    val storeno = AppConfig.userSaci?.storeno ?: 0
    val ec = if (filtro.ecommerce) "S" else "N"

    val dataInicial = filtro.dataInicial.toSaciDate()
    val dataFinal = filtro.dataFinal.toSaciDate()

    val pesquisa = filtro.pesquisa.trim()
    val filtroInt = pesquisa.toIntOrNull() ?: 0
    val filtroData = pesquisa.parserDate().toSaciDate()
    val filtroCD =
      if ((pesquisa.startsWith("CD", ignoreCase = true) || pesquisa.startsWith("EXP",
                                                                               ignoreCase = true)) && pesquisa.length in listOf(
                3,
                4)
      ) pesquisa.uppercase(Locale.getDefault())
      else ""
    val filtroStr = if (filtroInt == 0 && filtroData == 0 && filtroCD == "") pesquisa else ""
    val filtroLoja = filtroInt
    val filtroPedido = filtroInt
    val filtroArea = filtroStr
    val filtroRota = filtroStr
    val filtroFat = filtroInt
    val filtroPiso = filtroInt
    val filtroVend = filtroInt

    return query(sql, Pedido::class) {
      addOptionalParameter("tipo", filtro.tipo.sigla)
      addOptionalParameter("storeno", if (filtro.tipo == ETipoPedido.ENTREGA) 0 else storeno)
      addOptionalParameter("ecommerce", ec)
      addOptionalParameter("dataInicial", dataInicial)
      addOptionalParameter("dataFinal", dataFinal)
      addOptionalParameter("filtroPedido", filtroPedido)
      addOptionalParameter("filtroArea", filtroArea)
      addOptionalParameter("filtroRota", filtroRota)
      addOptionalParameter("filtroFat", filtroFat)
      addOptionalParameter("filtroData", filtroData)
      addOptionalParameter("filtroPiso", filtroPiso)
      addOptionalParameter("filtroVend", filtroVend)
      addOptionalParameter("filtroLoja", filtroLoja)
      addOptionalParameter("filtroCD", filtroCD)
    }
  }

  fun ativaMarca(storeno: Int, ordno: Int, marca: String) {
    val sql = "/sqlSaci/ativaMarca.sql"
    script(sql) {
      addOptionalParameter("storeno", storeno)
      addOptionalParameter("ordno", ordno)
      addOptionalParameter("marca", marca)
    }
  }

  fun marcaSeparado(storeno: Int, ordno: Int, marca: String) {
    val sql = "/sqlSaci/marcaSeparado.sql"
    script(sql) {
      addOptionalParameter("storeno", storeno)
      addOptionalParameter("ordno", ordno)
      addOptionalParameter("marca", marca)
    }
  }

  fun ativaDataHoraImpressao(storeno: Int, ordno: Int, data: LocalDate?, hora: LocalTime?) {
    val sql = "/sqlSaci/ativaDataHoraImpressao.sql"
    script(sql) {
      addParameter("storeno", storeno)
      addParameter("ordno", ordno)
      addParameter("data", data?.toSaciDate() ?: 0)
      addParameter("hora", hora ?: LocalTime.MIN)
    }
  }

  fun produtoPedido(storeno: Int, ordno: Int, tipo: String): List<ProdutoPedido> {
    val sql = "/sqlSaci/produtoPedido.sql"
    return query(sql, ProdutoPedido::class) {
      addOptionalParameter("storeno", storeno)
      addOptionalParameter("ordno", ordno)
      addOptionalParameter("tipo", tipo)
    }
  }

  fun findEntregadores(dateI: LocalDate, dateF: LocalDate, ecommerce: Boolean): List<Entregador> {
    val sql = "/sqlSaci/entregadores.sql"
    val ec = if (ecommerce) "S" else "N"
    return query(sql, Entregador::class) {
      addOptionalParameter("dateI", dateI.toSaciDate())
      addOptionalParameter("dateF", dateF.toSaciDate())
      addOptionalParameter("ecommerce", ec)
    }
  }

  fun findEntregadoresNotas(dateI: LocalDate, dateF: LocalDate, empno: Int, ecommerce: Boolean): List<EntregadorNotas> {
    val sql = "/sqlSaci/entregadoresNotas.sql"
    val ec = if (ecommerce) "S" else "N"
    return query(sql, EntregadorNotas::class) {
      addOptionalParameter("dateI", dateI.toSaciDate())
      addOptionalParameter("dateF", dateF.toSaciDate())
      addOptionalParameter("empno", empno)
      addOptionalParameter("ecommerce", ec)
    }
  }

  fun marcaCarga(storeno: Int, ordno: Int, carga: EZonaCarga) {
    val sql = "/sqlSaci/marcaCarga.sql"
    script(sql) {
      addOptionalParameter("storeno", storeno)
      addOptionalParameter("ordno", ordno)
      addOptionalParameter("marca", carga.codigo.toString())
    }
  }

  companion object {
    private val db = DB("saci")
    internal val driver = db.driver
    internal val url = db.url
    internal val username = db.username
    internal val password = db.password
    internal val test = db.test
    val ipServer = url.split("/").getOrNull(2)
  }
}

val saci = QuerySaci()