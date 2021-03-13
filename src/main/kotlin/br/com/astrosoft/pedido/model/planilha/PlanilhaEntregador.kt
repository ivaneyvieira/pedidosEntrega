package br.com.astrosoft.pedido.model.planilha

import br.com.astrosoft.pedido.model.beans.Entregador
import com.github.nwillc.poink.workbook
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment
import java.io.ByteArrayOutputStream

class PlanilhaEntregador {
  private val campos: List<Campo<*, Entregador>> =
    listOf(CampoString("Função") { ent -> ent.funcaoName },
      CampoInt("Número") { ent -> ent.empno },
      CampoString("Nome") { ent -> ent.nome },
      CampoInt("Qtd Ent") { ent -> ent.qtdEnt },
      CampoInt("Piso Cxs") { ent -> ent.pisoCxs },
      CampoNumber("Piso Peso") { ent -> ent.pisoPeso },
      CampoNumber("Valor") { ent -> ent.valorNota })

  fun grava(listaBean: List<Entregador>): ByteArray {
    val wb = workbook {
      val headerStyle = cellStyle("Header") {
        fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
        fillPattern = FillPatternType.SOLID_FOREGROUND
        this.verticalAlignment = VerticalAlignment.TOP
      }
      val rowStyle = cellStyle("Row") {
        this.verticalAlignment = VerticalAlignment.TOP
      }
      val sheet = sheet("Desempenho de entrega") {
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