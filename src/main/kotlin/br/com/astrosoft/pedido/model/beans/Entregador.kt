package br.com.astrosoft.pedido.model.beans

import br.com.astrosoft.pedido.model.saci
import java.time.LocalDate

data class Entregador(val funcaoName: String,
                      val nome: String,
                      val empno: Int,
                      val qtdEnt: Int,
                      val pisoCxs: Int,
                      val pisoPeso: Double,
                      val valor: Double,
                      val valorNota: Double,
                      val valorFrete: Double) {
  fun findEntregadoresNotas(dateI: LocalDate, dateF: LocalDate, ecommerce: Boolean) =
    saci.findEntregadoresNotas(dateI, dateF, empno, ecommerce)

  companion object {
    fun findEntregador(dateI: LocalDate, dateF: LocalDate, ecommerce: Boolean) =
      saci.findEntregadores(dateI, dateF, ecommerce)
  }
}
