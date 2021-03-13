package br.com.astrosoft.pedido.view.reports

import br.com.astrosoft.framework.util.format
import br.com.astrosoft.pedido.model.beans.Entregador
import br.com.astrosoft.pedido.view.reports.Templates.columnStyle
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder
import net.sf.dynamicreports.report.builder.DynamicReports
import net.sf.dynamicreports.report.builder.DynamicReports.*
import net.sf.dynamicreports.report.builder.column.ColumnBuilder
import net.sf.dynamicreports.report.builder.component.ComponentBuilder
import net.sf.dynamicreports.report.builder.subtotal.SubtotalBuilder
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.*
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.export.SimpleExporterInput
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.LocalTime

class RelatorioEntregador(
  val entregadores: List<Entregador>,
  val dataInicial: LocalDate,
  val dataFinal: LocalDate
                         ) {
  val entregadorFuncaoName =
    col.column("Função", Entregador::funcaoName.name, type.stringType()).apply {
        this.setHorizontalTextAlignment(LEFT)
        this.setFixedWidth(100)
      }
  val entregadorEmpno = col.column("Código", Entregador::empno.name, type.integerType()).apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setPattern("0")
      this.setFixedWidth(40)
    }
  val entregadorNome = col.column("Entregador", Entregador::nome.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(LEFT)
    }
  val entregadorQtdEnt = col.column("Qtd Ent", Entregador::qtdEnt.name, type.integerType()).apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setFixedWidth(40)
    }
  val entregadorPisoCxs =
    col.column("Piso Cxs", Entregador::pisoCxs.name, type.integerType()).apply {
        this.setHorizontalTextAlignment(RIGHT)
        this.setFixedWidth(40)
      }
  val entregadorPisoPeso =
    col.column("Piso Peso", Entregador::pisoPeso.name, type.doubleType()).apply {
        this.setHorizontalTextAlignment(RIGHT)
        this.setPattern("#,##0.00")
        this.setFixedWidth(80)
      }
  val entregadorValorNota =
    col.column("Valor Nota", Entregador::valorNota.name, type.doubleType()).apply {
        this.setHorizontalTextAlignment(RIGHT)
        this.setPattern("#,##0.00")
        this.setFixedWidth(80)
      }

  private fun columnBuilder(): List<ColumnBuilder<*, *>> {
    return listOf(
      entregadorFuncaoName,
      entregadorEmpno,
      entregadorNome,
      entregadorQtdEnt,
      entregadorPisoCxs,
      entregadorPisoPeso,
      entregadorValorNota
                 )
  }

  private fun titleBuider(): ComponentBuilder<*, *>? {
    return verticalList {
      horizontalFlowList {
        text("ENGECOPI", LEFT)
        text("DESEMPENHO DE ENTREGADA - ENTREGADORES", CENTER, 300)
        text(
          "${
            LocalDate.now().format()
          }-${
            LocalTime.now().format()
          }", RIGHT
            )
      }
      horizontalFlowList {
        text("Perído: ${dataInicial.format()} - ${dataFinal.format()}")
      }
    }
  }

  private fun pageFooterBuilder(): ComponentBuilder<*, *>? {
    return DynamicReports.cmp.verticalList()
  }

  private fun subtotalBuilder(): List<SubtotalBuilder<*, *>> {
    val style = stl.style(columnStyle).setTopBorder(stl.pen1Point())
    return listOf(
      sbt.sum(entregadorQtdEnt),
      sbt.sum(entregadorPisoCxs),
      sbt.sum(entregadorPisoPeso),
      sbt.sum(entregadorValorNota)
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
      .setSubtotalStyle(
        stl.style().setPadding(2).setTopBorder(stl.pen1Point())
                       )
      .pageFooter(
        DynamicReports.cmp.pageNumber().setHorizontalTextAlignment(RIGHT).setStyle(
            stl.style().setFontSize(8)
                                                                                  )
                 )
  }

  companion object {
    fun processaEntregadores(
      list: List<Entregador>,
      dataInicial: LocalDate,
      dataFinal: LocalDate
                            ): ByteArray {
      val report = RelatorioEntregador(list, dataInicial, dataFinal).makeReport()
      val jasperPrint = report?.toJasperPrint()
      val exporter = JRPdfExporter()
      val out = ByteArrayOutputStream()
      exporter.setExporterInput(SimpleExporterInput.getInstance(listOf(jasperPrint)))

      exporter.exporterOutput = SimpleOutputStreamExporterOutput(out)

      exporter.exportReport()
      return out.toByteArray()
    }
  }
}