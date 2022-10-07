package br.com.astrosoft.pedido.model.beans

import br.com.astrosoft.pedido.model.saci
import kotlin.math.pow

class UserSaci {
  var no: Int = 0
  var name: String = ""
  var login: String = ""
  var senha: String = ""
  var storeno: Int = 0
  var bitAcesso: Int = 0
  var prntno: Int = 0
  var impressora: String = ""

  //Otiros campos
  var ativo
    get() = (bitAcesso and BIT_ATIVO) != 0 || admin
    set(value) {
      bitAcesso = if (value) bitAcesso or BIT_ATIVO
      else bitAcesso and BIT_ATIVO.inv()
    }
  var entrega_imprimir
    get() = (bitAcesso and BIT_ENTREGA_IMPRIMIR) != 0 || admin
    set(value) {
      bitAcesso = if (value) bitAcesso or BIT_ENTREGA_IMPRIMIR
      else bitAcesso and BIT_ENTREGA_IMPRIMIR.inv()
    }
  var entrega_pendente
    get() = (bitAcesso and BIT_ENTREGA_PENDENTE) != 0 || admin
    set(value) {
      bitAcesso = if (value) bitAcesso or BIT_ENTREGA_PENDENTE
      else bitAcesso and BIT_ENTREGA_PENDENTE.inv()
    }
  var entrega_impressoComNota
    get() = (bitAcesso and BIT_ENTREGA_IMPRESSO_COM_NOTA) != 0 || admin
    set(value) {
      bitAcesso = if (value) bitAcesso or BIT_ENTREGA_IMPRESSO_COM_NOTA
      else bitAcesso and BIT_ENTREGA_IMPRESSO_COM_NOTA.inv()
    }
  var entrega_impressoSemNota
    get() = (bitAcesso and BIT_ENTREGA_IMPRESSO_SEM_NOTA) != 0 || admin
    set(value) {
      bitAcesso = if (value) bitAcesso or BIT_ENTREGA_IMPRESSO_SEM_NOTA
      else bitAcesso and BIT_ENTREGA_IMPRESSO_SEM_NOTA.inv()
    }
  var entrega_entregador
    get() = (bitAcesso and BIT_ENTREGA_ENTREGADOR) != 0 || admin
    set(value) {
      bitAcesso = if (value) bitAcesso or BIT_ENTREGA_ENTREGADOR
      else bitAcesso and BIT_ENTREGA_ENTREGADOR.inv()
    }
  var retira_imprimir
    get() = (bitAcesso and BIT_RETIRA_IMPRIMIR) != 0 || admin
    set(value) {
      bitAcesso = if (value) bitAcesso or BIT_RETIRA_IMPRIMIR
      else bitAcesso and BIT_RETIRA_IMPRIMIR.inv()
    }
  var retira_pendente
    get() = (bitAcesso and BIT_RETIRA_PENDENTE) != 0 || admin
    set(value) {
      bitAcesso = if (value) bitAcesso or BIT_RETIRA_PENDENTE
      else bitAcesso and BIT_RETIRA_PENDENTE.inv()
    }
  var retira_impressoComNota
    get() = (bitAcesso and BIT_RETIRA_IMPRESSO_COM_NOTA) != 0 || admin
    set(value) {
      bitAcesso = if (value) bitAcesso or BIT_RETIRA_IMPRESSO_COM_NOTA
      else bitAcesso and BIT_RETIRA_IMPRESSO_COM_NOTA.inv()
    }
  var retira_impressoSemNota
    get() = (bitAcesso and BIT_RETIRA_IMPRESSO_SEM_NOTA) != 0 || admin
    set(value) {
      bitAcesso = if (value) bitAcesso or BIT_RETIRA_IMPRESSO_SEM_NOTA
      else bitAcesso and BIT_RETIRA_IMPRESSO_SEM_NOTA.inv()
    }

  //
  var ecommerceE_imprimir
    get() = (bitAcesso and BIT_ECOMMERCEE_IMPRIMIR) != 0 || admin
    set(value) {
      bitAcesso = if (value) bitAcesso or BIT_ECOMMERCEE_IMPRIMIR
      else bitAcesso and BIT_ECOMMERCEE_IMPRIMIR.inv()
    }
  var ecommerceE_impressoComNota
    get() = (bitAcesso and BIT_ECOMMERCEE_IMPRESSO_COM_NOTA) != 0 || admin
    set(value) {
      bitAcesso = if (value) bitAcesso or BIT_ECOMMERCEE_IMPRESSO_COM_NOTA
      else bitAcesso and BIT_ECOMMERCEE_IMPRESSO_COM_NOTA.inv()
    }
  var ecommerceE_impressoSemNota
    get() = (bitAcesso and BIT_ECOMMERCEE_IMPRESSO_SEM_NOTA) != 0 || admin
    set(value) {
      bitAcesso = if (value) bitAcesso or BIT_ECOMMERCEE_IMPRESSO_SEM_NOTA
      else bitAcesso and BIT_ECOMMERCEE_IMPRESSO_SEM_NOTA.inv()
    }
  var ecommerceE_entregador
    get() = (bitAcesso and BIT_ECOMMERCEE_ENTREGADOR) != 0 || admin
    set(value) {
      bitAcesso = if (value) bitAcesso or BIT_ECOMMERCEE_ENTREGADOR
      else bitAcesso and BIT_ECOMMERCEE_ENTREGADOR.inv()
    }
  var ecommerceR_imprimir
    get() = (bitAcesso and BIT_ECOMMERCER_IMPRIMIR) != 0 || admin
    set(value) {
      bitAcesso = if (value) bitAcesso or BIT_ECOMMERCER_IMPRIMIR
      else bitAcesso and BIT_ECOMMERCER_IMPRIMIR.inv()
    }
  var ecommerceR_impressoComNota
    get() = (bitAcesso and BIT_ECOMMERCER_IMPRESSO_COM_NOTA) != 0 || admin
    set(value) {
      bitAcesso = if (value) bitAcesso or BIT_ECOMMERCER_IMPRESSO_COM_NOTA
      else bitAcesso and BIT_ECOMMERCER_IMPRESSO_COM_NOTA.inv()
    }
  var ecommerceR_impressoSemNota
    get() = (bitAcesso and BIT_ECOMMERCER_IMPRESSO_SEM_NOTA) != 0 || admin
    set(value) {
      bitAcesso = if (value) bitAcesso or BIT_ECOMMERCER_IMPRESSO_SEM_NOTA
      else bitAcesso and BIT_ECOMMERCER_IMPRESSO_SEM_NOTA.inv()
    }
  var entrega_separar
    get() = (bitAcesso and BIT_ENTRADA_IMPRESSO_SEPARAR) != 0 || admin
    set(value) {
      bitAcesso = if (value) bitAcesso or BIT_ENTRADA_IMPRESSO_SEPARAR
      else bitAcesso and BIT_ENTRADA_IMPRESSO_SEPARAR.inv()
    }
  var entrega_carga
    get() = (bitAcesso and BIT_ENTRADA_IMPRESSO_CARGA) != 0 || admin
    set(value) {
      bitAcesso = if (value) bitAcesso or BIT_ENTRADA_IMPRESSO_CARGA
      else bitAcesso and BIT_ENTRADA_IMPRESSO_CARGA.inv()
    }
  var entrega_separado
    get() = (bitAcesso and BIT_ENTRADA_IMPRESSO_SEPARADO) != 0 || admin
    set(value) {
      bitAcesso = if (value) bitAcesso or BIT_ENTRADA_IMPRESSO_SEPARADO
      else bitAcesso and BIT_ENTRADA_IMPRESSO_SEPARADO.inv()
    }
  var entrega_removerCarga
    get() = (bitAcesso and BIT_ENTRADA_IMPRESSO_REMOVER_CARGA) != 0 || admin
    set(value) {
      bitAcesso = if (value) bitAcesso or BIT_ENTRADA_IMPRESSO_REMOVER_CARGA
      else bitAcesso and BIT_ENTRADA_IMPRESSO_REMOVER_CARGA.inv()
    }
  var entrega_enviarSeparado
    get() = (bitAcesso and BIT_ENTRADA_IMPRESSO_ENVIA_SEPARADO) != 0 || admin
    set(value) {
      bitAcesso = if (value) bitAcesso or BIT_ENTRADA_IMPRESSO_ENVIA_SEPARADO
      else bitAcesso and BIT_ENTRADA_IMPRESSO_ENVIA_SEPARADO.inv()
    }
  var entrega_voltaSeparar
    get() = (bitAcesso and BIT_ENTRADA_IMPRESSO_VOLTA_SEPARAR) != 0 || admin
    set(value) {
      bitAcesso = if (value) bitAcesso or BIT_ENTRADA_IMPRESSO_VOLTA_SEPARAR
      else bitAcesso and BIT_ENTRADA_IMPRESSO_VOLTA_SEPARAR.inv()
    }

  val admin
    get() = login == "ADM"

  companion object {
    private val BIT_ATIVO = 2.pow(0)
    private val BIT_ENTREGA_IMPRIMIR = 2.pow(1)
    private val BIT_ENTREGA_PENDENTE = 2.pow(2)
    private val BIT_ENTREGA_IMPRESSO_COM_NOTA = 2.pow(3)
    private val BIT_ENTREGA_IMPRESSO_SEM_NOTA = 2.pow(4)
    private val BIT_ENTREGA_ENTREGADOR = 2.pow(5)
    private val BIT_RETIRA_IMPRIMIR = 2.pow(6)
    private val BIT_RETIRA_PENDENTE = 2.pow(7)
    private val BIT_RETIRA_IMPRESSO_COM_NOTA = 2.pow(8)
    private val BIT_RETIRA_IMPRESSO_SEM_NOTA = 2.pow(9)
    private val BIT_ECOMMERCEE_IMPRIMIR = 2.pow(10)
    private val BIT_ECOMMERCEE_IMPRESSO_COM_NOTA = 2.pow(11)
    private val BIT_ECOMMERCEE_IMPRESSO_SEM_NOTA = 2.pow(12)
    private val BIT_ECOMMERCEE_ENTREGADOR = 2.pow(13)
    private val BIT_ECOMMERCER_IMPRIMIR = 2.pow(14)
    private val BIT_ECOMMERCER_IMPRESSO_COM_NOTA = 2.pow(15)
    private val BIT_ECOMMERCER_IMPRESSO_SEM_NOTA = 2.pow(16)
    private val BIT_ENTRADA_IMPRESSO_SEPARAR = 2.pow(17)
    private val BIT_ENTRADA_IMPRESSO_SEPARADO = 2.pow(18)
    private val BIT_ENTRADA_IMPRESSO_CARGA = 2.pow(19)
    private val BIT_ENTRADA_IMPRESSO_REMOVER_CARGA = 2.pow(20)
    private val BIT_ENTRADA_IMPRESSO_ENVIA_SEPARADO = 2.pow(21)
    private val BIT_ENTRADA_IMPRESSO_VOLTA_SEPARAR = 2.pow(22)

    fun findAll(): List<UserSaci>? {
      return saci.findAllUser().filter { it.ativo }
    }

    fun updateUser(user: UserSaci) {
      saci.updateUser(user)
    }

    fun findUser(login: String?): UserSaci? {
      return saci.findUser(login)
    }
  }
}

fun Int.pow(e: Int): Int = this.toDouble().pow(e).toInt()