package br.com.astrosoft.pedidoEntrega.model.beans

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
  var ativo
    get() = (bitAcesso and BIT_ATIVO) != 0 || admin
    set(value) {
      bitAcesso = if(value) bitAcesso or BIT_ATIVO
      else bitAcesso and BIT_ATIVO.inv()
    }
  var imprimir
    get() = (bitAcesso and BIT_IMPRIMIR) != 0 || admin
    set(value) {
      bitAcesso = if(value) bitAcesso or BIT_IMPRIMIR
      else bitAcesso and BIT_IMPRIMIR.inv()
    }
  var pendente
    get() = (bitAcesso and BIT_PENDENTE) != 0 || admin
    set(value) {
      bitAcesso = if(value) bitAcesso or BIT_PENDENTE
      else bitAcesso and BIT_PENDENTE.inv()
    }
  var impressoComNota
    get() = (bitAcesso and BIT_IMPRESSO_COM_NOTA) != 0 || admin
    set(value) {
      bitAcesso = if(value) bitAcesso or BIT_IMPRESSO_COM_NOTA
      else bitAcesso and BIT_IMPRESSO_COM_NOTA.inv()
    }
  var impressoSemNota
    get() = (bitAcesso and BIT_IMPRESSO_SEM_NOTA) != 0 || admin
    set(value) {
      bitAcesso = if(value) bitAcesso or BIT_IMPRESSO_SEM_NOTA
      else bitAcesso and BIT_IMPRESSO_SEM_NOTA.inv()
    }
  var entregador
    get() = (bitAcesso and BIT_ENTREGADOR) != 0 || admin
    set(value) {
      bitAcesso = if(value) bitAcesso or BIT_ENTREGADOR
      else bitAcesso and BIT_ENTREGADOR.inv()
    }
  val admin
    get() = login == "ADM"
  
  companion object {
    private val BIT_ATIVO = 2.pow(0)
    private val BIT_IMPRIMIR = 2.pow(1)
    private val BIT_PENDENTE = 2.pow(2)
    private val BIT_IMPRESSO_COM_NOTA = 2.pow(3)
    private val BIT_IMPRESSO_SEM_NOTA = 2.pow(4)
    private val BIT_ENTREGADOR = 2.pow(5)
    
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
}

fun Int.pow(e: Int): Int = this.toDouble()
  .pow(e)
  .toInt()