package br.com.astrosoft.pedido.view.reports

import br.com.astrosoft.framework.util.format
import br.com.astrosoft.pedido.model.beans.Entregador
import br.com.astrosoft.pedido.model.beans.EntregadorNotas
import br.com.astrosoft.pedido.view.reports.Templates.columnStyle
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder
import net.sf.dynamicreports.report.builder.DynamicReports
import net.sf.dynamicreports.report.builder.DynamicReports.sbt
import net.sf.dynamicreports.report.builder.DynamicReports.col
import net.sf.dynamicreports.report.builder.DynamicReports.stl
import net.sf.dynamicreports.report.builder.DynamicReports.type
import net.sf.dynamicreports.report.builder.column.ColumnBuilder
import net.sf.dynamicreports.report.builder.component.ComponentBuilder
import net.sf.dynamicreports.report.builder.subtotal.SubtotalBuilder
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.CENTER
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.LEFT
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.RIGHT
import net.sf.dynamicreports.report.constant.Position
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.export.SimpleExporterInput
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.LocalTime

class RelatorioEntregadorPedido(val entregadores: List<EntregadorNotas>, val dataInicial: LocalDate, val dataFinal: LocalDate) {
  val entregadorNotasCarganoCol= col.column("Carga", EntregadorNotas::carganoCol.name, type.integerType())
    .apply {
      this.setHorizontalTextAlignment(LEFT)
      this.setPattern("0")
      this.setFixedWidth(50)
    }
  val entregadorNotasLojaCol= col.column("Loja", EntregadorNotas::lojaCol.name, type.integerType())
    .apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setPattern("0")
      this.setFixedWidth(30)
    }
  val entregadorNotasNumPedidoCol= col.column("Pedido", EntregadorNotas::numPedidoCol.name, type.integerType())
    .apply {
      this.setHorizontalTextAlignment(LEFT)
      this.setPattern("0")
      this.setFixedWidth(50)
    }
  val entregadorNotasDatePedidoCol= col.column("Data Ped", EntregadorNotas::datePedidoStr.name, type.stringType())
    .apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setFixedWidth(50)
    }
  val entregadorNotasNotaFatCol= col.column("Nota Fat", EntregadorNotas::notaFatCol.name, type.stringType())
    .apply {
      this.setHorizontalTextAlignment(LEFT)
      this.setFixedWidth(50)
    }
  val entregadorNotasDateFatCol= col.column("Data Fat", EntregadorNotas::dateFatStr.name, type.stringType())
    .apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setFixedWidth(50)
    }
  val entregadorNotasNotaEntCol= col.column("Nota Ent", EntregadorNotas::notaEntCol.name, type.stringType())
    .apply {
      this.setHorizontalTextAlignment(LEFT)
      this.setFixedWidth(50)
    }
  val entregadorNotasDateEntCol= col.column("Data Ent", EntregadorNotas::dateEntStr.name, type.stringType())
    .apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setFixedWidth(50)
    }
  val entregadorNotasPisoCxs= col.column("Piso Cxs", EntregadorNotas::pisoCxs.name, type.integerType())
    .apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setPattern("#,##0")
    }
  val entregadorNotasPisoPeso= col.column("Piso Peso", EntregadorNotas::pisoPeso.name, type.doubleType())
    .apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setPattern("#,##0.00")
      this.setFixedWidth(60)
    }
  val entregadorNotasValor= col.column("Valor", EntregadorNotas::valor.name, type.doubleType())
    .apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setPattern("#,##0.00")
      this.setFixedWidth(60)
    }
  
  private fun columnBuilder(): List<ColumnBuilder<*, *>> {
    return listOf(entregadorNotasCarganoCol,
                  entregadorNotasLojaCol,
                  entregadorNotasNumPedidoCol,
                  entregadorNotasDatePedidoCol,
                  entregadorNotasNotaFatCol,
                  entregadorNotasDateFatCol,
                  entregadorNotasNotaEntCol,
                  entregadorNotasDateEntCol,
                  entregadorNotasPisoCxs,
                  entregadorNotasPisoPeso,
                  entregadorNotasValor)
  }
  
  private fun titleBuider(): ComponentBuilder<*, *>? {
    val entregador = entregadores.firstOrNull()
    return verticalList {
      horizontalFlowList {
        text("ENGECOPI", LEFT)
        text("DESEMPENHO DE ENTREGADA - PEDIDOS", CENTER, 300)
        text("${
          LocalDate.now()
            .format()
        }-${
          LocalTime.now()
            .format()
        }", RIGHT)
      }
      horizontalFlowList {
        text("Perído: ${dataInicial.format()} - ${dataFinal.format()}")
        text("${entregador?.funcaoName} ${entregador?.empno}-${entregador?.nome}")
      }
    }
  }
  
  private fun pageFooterBuilder(): ComponentBuilder<*, *>? {
    return DynamicReports.cmp.verticalList()
  }
  
  private fun subtotalBuilder(): List<SubtotalBuilder<*, *>> {
    val style = stl.style(columnStyle)
      .setTopBorder(stl.pen1Point())
    return listOf(
      sbt.sum(entregadorNotasPisoCxs),
      sbt.sum(entregadorNotasPisoPeso),
      sbt.sum(entregadorNotasValor)
                 )
  }
  
  fun makeReport(): JasperReportBuilder? {
    val colunms = columnBuilder().toTypedArray()
    return DynamicReports.report()
      .title(titleBuider())
      .setTemplate(Templates.reportTemplate)
      .columns(* colunms)
      .columnGrid(* colunms)
      .setDataSource(entregadores)
      .summary(pageFooterBuilder())
      .subtotalsAtSummary(* subtotalBuilder().toTypedArray())
      .setSubtotalStyle(stl.style()
                          .setPadding(2)
                          .setTopBorder(stl.pen1Point()))
      .pageFooter(DynamicReports.cmp.pageNumber()
                    .setHorizontalTextAlignment(RIGHT)
                    .setStyle(stl.style()
                                .setFontSize(8)))
  }
  
  companion object {
    fun processaRelatorio(list: List<EntregadorNotas>, dataInicial: LocalDate, dataFinal: LocalDate): ByteArray {
      val report = RelatorioEntregadorPedido(list, dataInicial, dataFinal).makeReport()
      val jasperPrint = report?.toJasperPrint()
      val exporter = JRPdfExporter()
      val out = ByteArrayOutputStream()
      exporter.setExporterInput(SimpleExporterInput.getInstance(listOf(jasperPrint)))
      
      exporter.exporterOutput = SimpleOutputStreamExporterOutput(out);
      
      exporter.exportReport()
      return out.toByteArray()
    }
  }
}