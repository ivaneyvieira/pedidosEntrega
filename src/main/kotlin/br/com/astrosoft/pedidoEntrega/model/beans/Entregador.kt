package br.com.astrosoft.pedidoEntrega.model.beans

import br.com.astrosoft.pedidoEntrega.model.saci
import java.time.LocalDate

data class Entregador(
  val funcaoName: String,
  val nome: String,
  val qtdEnt: Int,
  val pisoCxs: Int,
  val pisoPeso: Double,
  val valor: Double
                     ){
  
  companion object {
    fun findEntregador(dateI : LocalDate, dateF : LocalDate) = saci.findEntregadores(dateI, dateF)
  }
}