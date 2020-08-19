package br.com.astrosoft.pedidoEntrega.model.beans

data class ProdutoPedido(
  val codigo: String,
  val descricao: String,
  val grade: String,
  val refFab: String,
  val barcode: String,
  val qtd: Int,
  val vlUnit: Double,
  val vlTotal: Double,
  val localizacao: String
                        ){
  val descricaoReport
  get() = "$descricao\n$refFab\n$localizacao"
}