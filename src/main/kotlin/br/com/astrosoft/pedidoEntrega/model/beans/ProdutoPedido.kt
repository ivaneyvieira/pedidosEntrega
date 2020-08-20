package br.com.astrosoft.pedidoEntrega.model.beans

import br.com.astrosoft.framework.util.format

data class ProdutoPedido(
  val codigo: String,
  val descricao: String,
  val grade: String,
  val refFab: String,
  val barcode: String,
  val qtd: Int,
  val peso: Double,
  val vlUnit: Double,
  val vlTotal: Double,
  val localizacao: String
                        ) {
  val descricaoReport
    get() = "$descricao\n| ${peso.format()}   | $localizacao   | $refFab "
}