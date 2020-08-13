package br.com.astrosoft.pedidoEntrega.model.beans

import br.com.astrosoft.framework.spring.SecurityUtils
import br.com.astrosoft.pedidoEntrega.model.saci
import kotlin.math.pow

class UserSaci {
  var no: Int = 0
  var name: String = ""
  var login: String = ""
  var senha: String = ""
  var bitAcesso: Int = 0
  var prntno: Int = 0
  var impressora: String = ""
  
  //Otiros campos
  var ativo: Boolean = true
  val admin
    get() = login == "ADM"
  
  fun initVars(): UserSaci {
    val bits = bitAcesso
    ativo = (bits and 2.pow(7)) != 0 || admin
    return this
  }
  
  fun bitAcesso(): Int {
    val ativoSum = if(ativo) 2.pow(7) else 0
    val bits = saci.findUser(login)?.bitAcesso ?: 0
    return ativoSum or bits
  }
  
  companion object {
    fun findAll(): List<UserSaci>? {
      return saci.findAllUser()
        .filter {it.ativo}
    }
    
    fun updateUser(user: UserSaci) {
      saci.updateUser(user)
    }
    
    fun findUser(login: String?): UserSaci? {
      return saci.findUser(login)
    }
  }
  
  fun Int.pow(e: Int): Int = this.toDouble()
    .pow(e)
    .toInt()
}


