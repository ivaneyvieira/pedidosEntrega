package br.com.astrosoft.pedido.model.beans

import br.com.astrosoft.pedido.model.saci

class Impressora(
  val no: Int,
  val name: String,
  val local: Int,
                ) {
  companion object {
    fun findAll() = saci.findImpressoras()
  }
}