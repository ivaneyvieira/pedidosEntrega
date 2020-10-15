package br.com.astrosoft.pedido.view.reports

import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.util.lpad
import br.com.astrosoft.pedido.model.beans.Pedido
import br.com.astrosoft.pedido.model.beans.ProdutoPedido
import br.com.astrosoft.pedido.view.reports.Templates.columnStyle
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder
import net.sf.dynamicreports.report.builder.DynamicReports.cmp
import net.sf.dynamicreports.report.builder.DynamicReports.col
import net.sf.dynamicreports.report.builder.DynamicReports.report
import net.sf.dynamicreports.report.builder.DynamicReports.sbt
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

class RelatorioPedido(val pedido: Pedido) {
  val colCodigo =
    col.column("Código", ProdutoPedido::codigo.name, type.stringType())
      .apply {
        this.setHorizontalTextAlignment(LEFT)
        this.setFixedWidth(40)
      }
  val colDescricao =
    col.column("Descrição", ProdutoPedido::descricaoReport.name, type.stringType())
      .apply {
      }
  val colGrade =
    col.column("Grade", ProdutoPedido::grade.name, type.stringType())
      .apply {
        this.setHorizontalTextAlignment(LEFT)
        this.setFixedWidth(50)
      }
  val colCodBarras =
    col.column("Cod Barras", ProdutoPedido::barcode.name, type.stringType())
      .apply {
        this.setHorizontalTextAlignment(LEFT)
        this.setFixedWidth(80)
      }
  val colQtd =
    col.column("Qtd", ProdutoPedido::qtd.name, type.integerType())
      .apply {
        this.setPattern("#,##0.####")
        this.setFixedWidth(40)
      }
  val colVlUnit =
    col.column("R$ Unit", ProdutoPedido::vlUnit.name, type.doubleType())
      .apply {
        this.setPattern("#,##0.00")
        this.setFixedWidth(80)
      }
  val vlTotal =
    col.column("R$ Total", ProdutoPedido::vlTotal.name, type.doubleType())
      .apply {
        this.setPattern("#,##0.00")
        this.setFixedWidth(80)
      }
  
  fun makeReportPedido(): JasperReportBuilder? {
    val colunms = columnBuilder().toTypedArray()
    return report().title(titleBuider())
      .setTemplate(Templates.reportTemplate)
      .columns(* colunms)
      .columnGrid(* colunms)
      .subtotalsAtSummary(* subtotalBuilder().toTypedArray())
      .setDataSource(dataSource())
      .summary(pageFooterBuilder())
      .setSubtotalStyle(stl.style()
                          .setPadding(2)
                          .setTopBorder(stl.pen1Point()))
    /*
    .pageFooter(cmp.pageNumber()
                  .setHorizontalTextAlignment(RIGHT)
                  .setStyle(stl.style()
                              .setFontSize(8)))
                              
     */
  }
  
  fun makeReportMinuta(): JasperReportBuilder? {
    return report().title(titleBuiderMinutaCompacta())
      .setTemplate(Templates.reportTemplate)
      .setDataSource(listOf(pedido))
  }
  
  fun titleBuiderReportDuplo(): ComponentBuilder<*, *>? {
    return verticalList {
      this.add(cmp.subreport(makeReportPedido()))
      this.add(cmp.subreport(makeReportMinuta()))
    }
  }
  
  fun makeReportDuplo(): JasperReportBuilder? {
    return report().title(titleBuiderReportDuplo())
  }
  
  private fun titleBuiderMinuta(): ComponentBuilder<*, *>? {
    return verticalList {
      horizontalFlowList {
        text("ENGECOPI ${pedido.siglaLoja}", LEFT, 100)
        text("MINUTA DE ENTREGA - PEDIDO ${pedido.pedido}", CENTER)
        text("${pedido.data.format()}-${pedido.hora.format()}", RIGHT, 100)
      }
      breakLine()
      horizontalFlowList {
        text("Vendedor: ${pedido.vendedor}", LEFT)
        text("Valor do Pedido: ${pedido.valorComFrete.format()}", RIGHT)
      }
      breakLine()
      horizontalFlowList {
        text("Cliente: ${pedido.cliente}", LEFT)
        text("Fone: ${pedido.foneCliente}", RIGHT)
      }
      horizontalFlowList {
        text("Bairro: ${pedido.bairro}", LEFT)
        text("Cidade: ${pedido.cidade} - ${pedido.estado}", RIGHT)
      }
      breakLine()
      text("Entrega: ${pedido.enderecoEntrega}", LEFT)
      horizontalFlowList {
        text("Bairro: ${pedido.bairroEntrega}", LEFT)
        text("Área: ${pedido.area}", CENTER)
        text("Rota: ${pedido.rota}", RIGHT)
      }
      breakLine()
      text("Observação Vendedor")
      horizontalFlowList {
        verticalList {
          text(pedido.obs1)
          text(pedido.obs2)
          text(pedido.obs3)
          text(pedido.obs4)
          text(pedido.obs5)
          text(pedido.obs6)
          text(pedido.obs7)
        }
      }
      breakLine()
      val coluna1 = 150
      val coluna2 = 150
      horizontalFlowList {
        text("", LEFT, coluna1)
        text("Início:", RIGHT)
        text("_______/_______/_______", CENTER)
        text("______:______", CENTER, coluna2)
      }
      horizontalFlowList {
        text("", LEFT, coluna1)
        text("", LEFT)
        text("Data", CENTER)
        text("Hora", CENTER, coluna2)
      }
      breakLine()
      horizontalFlowList {
        text("_______________________", CENTER, coluna1)
        text("Concluído:", RIGHT)
        text("_______/_______/_______", CENTER)
        text("______:______", CENTER, coluna2)
      }
      horizontalFlowList {
        text("Separador", CENTER, coluna1)
        text("", LEFT)
        text("Data", CENTER)
        text("Hora", CENTER, coluna2)
      }
      breakLine()
      
      horizontalFlowList {
        text("Observações motorista", LEFT)
        text("Hora Chegada:  ___________________", RIGHT)
      }
      breakLine()
      breakLine()
      breakLine()
      breakLine()
      horizontalFlowList {
        text("Hora Saida:    ___________________", RIGHT)
      }
      breakLine()
      breakLine()
      breakLine()
      breakLine()
      horizontalFlowList {
        text("Ocorrências de Faltas", LEFT)
        text("Quilometragem:  ___________________", RIGHT)
      }
      breakLine()
      breakLine()
      breakLine()
      breakLine()
      horizontalFlowList {
        text("Motorista:    ___________________", RIGHT)
      }
      breakLine()
      breakLine()
      breakLine()
      breakLine()
      horizontalFlowList {
        text("Data Recebimento", LEFT, 150)
        text("Identificação e Assinatura ao Receber", CENTER)
        text("Nota Fiscal:   ___________________", RIGHT, 200)
      }
      breakLine()
      horizontalFlowList {
        text("______/______/______", LEFT, 150)
        text("________________________________________________", CENTER)
        text("Reserva:   ___________________", RIGHT, 200)
      }
    }
  }
  
  private fun titleBuiderMinutaCompacta(): ComponentBuilder<*, *>? {
    return verticalList {
      breakLine()
      horizontalFlowList {
        text("MINUTA DE ENTREGA", CENTER)
      }
      
      text("END DE ENTREGA: ${pedido.enderecoEntrega} - ${pedido.bairroEntrega}    ${pedido.area}    ${pedido.rota}")
      
      text("Observação Vendedor:")
      val obsList = listOf(pedido.obs1, pedido.obs2, pedido.obs3, pedido.obs4,
                           pedido.obs5, pedido.obs6, pedido.obs7).flatMap {obs ->
        val obsFormat = obs.lpad(80, " ")
        val part1 = obsFormat.substring(1, 40)
        val part2 = obsFormat.substring(41, 80)
        listOf(part1, part2)
      }
                      .mapNotNull {obs ->
                        val obsTrim = obs.trim()
                        if(obsTrim == "" || obsTrim == ".") null else obs
                      } + ""
      /*
      obsList.windowed(2, 2)
        .map {Pair(it[0], it[1])}
        .forEach {par ->
          horizontalFlowList {
            text(par.first + par.second, LEFT)
          }
        }
        
       */
      
      if(pedido.obs1.trim() != "")
        text(pedido.obs1.trim())
      if(pedido.obs2.trim() != "")
        text(pedido.obs2.trim())
      if(pedido.obs3.trim() != "")
        text(pedido.obs3.trim())
      if(pedido.obs4.trim() != "")
        text(pedido.obs4.trim())
      if(pedido.obs5.trim() != "")
        text(pedido.obs5.trim())
      if(pedido.obs6.trim() != "")
        text(pedido.obs6.trim())
      if(pedido.obs7.trim() != "")
        text(pedido.obs7.trim())
      
      breakLine()
      horizontalFlowList {
        text("Separador:  __________________________________", LEFT)
        text("Motorista: __________________________________", RIGHT)
      }
      horizontalFlowList {
        text("Hora Saída: ___________________", LEFT)
        text("Hora Chegada: ___________________", CENTER)
        text("Quilometragem: ___________________", RIGHT)
      }
      breakLine()
      horizontalFlowList {
        text("Data Recebimento\n_____/_____/_____\n\nObservação Motorista:", CENTER, 150)
        text("\n_____________________________________________________\nIdentificação e Assinatura do(a) Recebedor(a)", CENTER)
      }
    }
  }
  
  private fun pageFooterBuilder(): ComponentBuilder<*, *>? {
    return cmp.verticalList()
  }
  
  private fun titleBuider(): ComponentBuilder<*, *>? {
    return verticalList {
      horizontalFlowList {
        text("ENGECOPI ${pedido.siglaLoja}", LEFT)
        text("ROMANEIO DE SEPARAÇÃO PEDIDO DE ENTREGA: ${pedido.pedido}", CENTER, 300)
        text("${pedido.data?.format()}-${pedido.hora.format()}", RIGHT)
      }
      horizontalFlowList {
        val nfText = "NF: ${pedido.nfFat}"
        val dataText = "DATA: ${pedido.dataFat.format()}-${pedido.horaFat.format()}"
        val valorText = "VALOR R$: ${pedido.valorComFrete.format()}"
        val pdvText = "PDV: ${pedido.pdvnoVenda}"
        val metodo = "MET PGTO: ${pedido.metodo}"
        text("$nfText $dataText $valorText      $pdvText      $metodo")
      }
      horizontalFlowList {
        text("VENDEDOR: ${pedido.vendedor.replace(" +".toRegex(), " ")}", LEFT)
      }
      horizontalFlowList {
        text("CLIENTE: ${pedido.cliente}       ${pedido.endereco}   ${pedido.bairro}", LEFT)
      }
    }
  }
  
  private fun dataSource(): List<ProdutoPedido> {
    return pedido.produtos()
      .sortedBy {it.descricao + it.grade}
  }
  
  private fun subtotalBuilder(): List<SubtotalBuilder<*, *>> {
    val style = stl.style(columnStyle)
      .setTopBorder(stl.pen1Point())
    return listOf(
      sbt.text("", colCodigo),
      sbt.text("", colDescricao),
      sbt.text("", colGrade),
      sbt.text("", colCodBarras),
      sbt.text("", colQtd),
      sbt.text("", colVlUnit),
      sbt.sum(vlTotal)
        .setLabel("Total R$")
        .setLabelStyle(style)
        .setLabelPosition(Position.LEFT)
        .setStyle(style))
  }
  
  private fun columnBuilder(): List<ColumnBuilder<*, *>> {
    return listOf(colCodigo, colDescricao, colGrade, colCodBarras, colQtd, colVlUnit, vlTotal)
  }
  
  companion object {
    fun processaPedidosMinuta(list: List<Pedido>): ByteArray {
      val reports = list.flatMap {pedido ->
        val report = RelatorioPedido(pedido)
        listOf(report.makeReportPedido(), report.makeReportMinuta())
      }
        .filterNotNull()
      val jasperPrints = reports.map {jasperReportBuild ->
        jasperReportBuild.toJasperPrint()
      }
      val exporter = JRPdfExporter()
      val out = ByteArrayOutputStream()
      exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrints))
      
      exporter.exporterOutput = SimpleOutputStreamExporterOutput(out);
      
      exporter.exportReport()
      return out.toByteArray()
    }
    
    fun processaPedidosMinutaCompacta(list: List<Pedido>): ByteArray {
      val reports = list.flatMap {pedido ->
        val report = RelatorioPedido(pedido)
        listOf(report.makeReportDuplo())
      }
        .filterNotNull()
      val jasperPrints = reports.map {jasperReportBuild ->
        jasperReportBuild.toJasperPrint()
      }
      val exporter = JRPdfExporter()
      val out = ByteArrayOutputStream()
      exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrints))
      
      exporter.exporterOutput = SimpleOutputStreamExporterOutput(out);
      
      exporter.exportReport()
      return out.toByteArray()
    }
    
    fun processaPedidos(list: List<Pedido>): ByteArray {
      val reports = list.flatMap {pedido ->
        val report = RelatorioPedido(pedido)
        listOf(report.makeReportPedido())
      }
        .filterNotNull()
      val jasperPrints = reports.map {jasperReportBuild ->
        jasperReportBuild.toJasperPrint()
      }
      val exporter = JRPdfExporter()
      val out = ByteArrayOutputStream()
      exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrints))
      
      exporter.exporterOutput = SimpleOutputStreamExporterOutput(out);
      
      exporter.exportReport()
      return out.toByteArray()
    }
  }
}
