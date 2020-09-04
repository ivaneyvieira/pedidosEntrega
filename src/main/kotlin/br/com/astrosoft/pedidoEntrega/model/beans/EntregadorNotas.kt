package br.com.astrosoft.pedidoEntrega.model.beans

import java.time.LocalDate

data class EntregadorNotas(
  val funcaoName: String,
  val nome: String,
  val date: LocalDate,
  val empno: Int,
  val loja: Int,
  val nota: String,
  val prdno: String,
  val grade: String,
  val descricao: String,
  val pisoCxs: Int,
  val pisoPeso: Double,
  val valor: Double
                          ): Any()