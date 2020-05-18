package br.com.astrosoft.pedidoEntrega.model.beans

import java.time.LocalDate
import java.time.LocalTime

data class PedidoEntrega(
  val loja: Int,
  val pedido: Int,
  val marca: String,
  val data: LocalDate?,
  val hora: LocalTime?,
  val nfnoFat: String,
  val nfseFat: String,
  val dataFat: LocalDate?,
  val horaFat: LocalTime?,
  val nfnoEnt: String,
  val nfseEnt: String,
  val dataEnt: LocalDate?,
  val horaEnt: LocalTime?,
  val vendno: Int,
  val custno: Int,
  val frete: Double,
  val valor: Double,
  val status: String,
  val area: String,
  val rota: String,
  val obs: String,
  val codArea: Int,
  val userno: Int,
  val username: String
                        ) {
  val nfFat: String
    get() = "$nfnoFat/$nfseFat"
  val nfEnt: String
    get() = "$nfnoEnt/$nfseEnt"
}