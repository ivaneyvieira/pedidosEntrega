package br.com.astrosoft.pedido.model.beans

data class Relatorio(
  val loja: String,
  val pedido: String,
  val notaFiscal : String,
  val dataHoraNota: String,
  val rotaArea: String,
  val vendedor: String,
  val cliente: String,
  val endereco: String,
  val codigo: String,
  val descricao: String,
  val grade: String,
  val localizacao: String,
  val quantidade: Int,
                    )
