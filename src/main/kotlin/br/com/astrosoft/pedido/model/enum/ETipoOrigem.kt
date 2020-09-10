package br.com.astrosoft.pedido.model.enum

enum class ETipoOrigem(val descricao: String, val sigla: String) {
  DUPLICADO("Duplicado", "D"),
  SEPARADO("Separado", "S"),
  LOJA("Separado Loja", "L");
  
  companion object {
    fun value(sigla: String) = values().toList().firstOrNull {it.sigla == sigla}
  }
}