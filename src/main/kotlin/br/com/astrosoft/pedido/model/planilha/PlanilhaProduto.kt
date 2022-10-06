package br.com.astrosoft.pedido.model.planilha

import br.com.astrosoft.framework.util.format
import br.com.astrosoft.pedido.model.beans.EntregadorNotas
import com.github.nwillc.poink.workbook
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment
import java.io.ByteArrayOutputStream

class PlanilhaProduto {
  private val campos: List<Campo<*, EntregadorNotas>> =
    listOf(CampoInt("Carga") { ent -> ent.carganoCol ?: 0 },
           CampoInt("Loja") { ent -> ent.lojaCol ?: 0 },
           CampoInt("Pedido") { ent -> ent.numPedidoCol ?: 0 },
           CampoString("Data Pedido") { ent -> ent.datePedidoCol.format() },
           CampoString("Nota Ent") { ent -> ent.notaEntCol ?: "" },
           CampoString("Data Ent") { ent -> ent.dateEntCol.format() },
           CampoString("Entrega") { ent -> ent.entregaCol.format() },

           CampoString("Código") { ent -> ent.prdnoCol ?: "" },
           CampoString("Descrição") { ent -> ent.descricao },
           CampoString("Grade") { ent -> ent.grade },

           CampoInt("Piso Cxs") { ent -> ent.pisoCxs ?: 0 },
           CampoNumber("Piso Peso") { ent -> ent.pisoPeso ?: 0.00 },
           CampoNumber("Valor") { ent -> ent.valor ?: 0.00 })

  fun grava(listaBean: List<EntregadorNotas>): ByteArray {
    val wb = workbook {
      val headerStyle = cellStyle("Header") {
        fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
        fillPattern = FillPatternType.SOLID_FOREGROUND
        this.verticalAlignment = VerticalAlignment.TOP
      }
      val rowStyle = cellStyle("Row") {
        this.verticalAlignment = VerticalAlignment.TOP
      }
      val sheet = sheet("Desempenho de entrega por pedido") {
        val headers = campos.map { it.header }
        row(headers, headerStyle)
        listaBean.forEach { bean ->
          val valores = campos.map { it.produceVakue(bean) }
          row(valores, rowStyle)
        }
      }

      campos.forEachIndexed { index, campo ->
        sheet.autoSizeColumn(index)
      }
    }
    val outBytes = ByteArrayOutputStream()
    wb.write(outBytes)
    return outBytes.toByteArray()
  }
}