package br.com.astrosoft.pedido.viewmodel.entrega

import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.util.lpad
import br.com.astrosoft.framework.viewmodel.PrintText
import br.com.astrosoft.pedido.model.beans.Relatorio
import br.com.astrosoft.pedido.model.beans.Rota
import java.time.LocalDate
import java.time.LocalTime

class RelatorioText : PrintText<Relatorio>() {
  init {
    columText("Cod", 6) { codigo }
    columText("Descricao", 30) { descricao }
    columText("Grade", 8) { grade }
    columText("Local", 8) { localizacao }
    columText("Quant", 8) { quantidade.toString().lpad(8, " ") }
  }

  override fun sumaryLine(): List<String> {
    val dataHora = "${LocalDate.now().format()}-${LocalTime.now().format()}"
    return listOf(""""
    
DOCUMENTO NAO FISCAL                            $dataHora
 
    """.trimIndent().negrito())
  }

  override fun titleLines(bean: Relatorio): List<String> {
    val rota = "ROTA: ${bean.rota}".lpad(21, " ")
    return listOf(
      "ENGECOPI ${bean.loja}       ROMANEIO DE SEPARACAO $rota".negrito(),
      "PEDIDO DE ENTREGA: ${bean.pedido} ${bean.dataPedido} ${bean.notaFiscal} ${bean.dataHoraNota}".negrito(),
      "VENDEDOR: ${bean.vendedor}".negrito(),
      "CLIENTE: ${bean.cliente}".negrito(),
      ""
                 )
  }
}