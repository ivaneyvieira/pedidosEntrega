package br.com.astrosoft.pedidoEntrega.view.reports

import br.com.astrosoft.framework.util.format
import br.com.astrosoft.pedidoEntrega.model.beans.PedidoEntrega
import br.com.astrosoft.pedidoEntrega.model.beans.ProdutoPedido
import net.sf.dynamicreports.report.builder.DynamicReports.cmp
import net.sf.dynamicreports.report.builder.DynamicReports.col
import net.sf.dynamicreports.report.builder.DynamicReports.report
import net.sf.dynamicreports.report.builder.DynamicReports.sbt
import net.sf.dynamicreports.report.builder.DynamicReports.stl
import net.sf.dynamicreports.report.builder.DynamicReports.type
import net.sf.dynamicreports.report.builder.column.ColumnBuilder
import net.sf.dynamicreports.report.builder.component.ComponentBuilder
import net.sf.dynamicreports.report.builder.subtotal.SubtotalBuilder
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.LEFT
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.RIGHT
import net.sf.dynamicreports.report.constant.Position
import net.sf.dynamicreports.report.exception.DRException
import java.io.ByteArrayOutputStream

class RelatorioPedido(val pedido: PedidoEntrega) {
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
  val colRefFab =
    col.column("Ref Fab", ProdutoPedido::refFab.name, type.stringType())
      .apply {
        this.setHorizontalTextAlignment(LEFT)
        this.setFixedWidth(60)
      }
  val colCodBarras =
    col.column("Cod Barras", ProdutoPedido::barcode.name, type.stringType())
      .apply {
        this.setHorizontalTextAlignment(LEFT)
        this.setFixedWidth(70)
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
  val colLocalizacao =
    col.column("Localização", ProdutoPedido::localizacao.name, type.stringType())
      .apply {
        this.setHorizontalTextAlignment(LEFT)
      }
  
  fun build(): ByteArray {
    return try {
      val outputStream = ByteArrayOutputStream()
      val colunms = columnBuilder().toTypedArray()
      report().title(titleBuider())
        .setTemplate(Templates.reportTemplate)
        .columns(* colunms)
        .columnGrid(* colunms)
        .subtotalsAtSummary(* subtotalBuilder().toTypedArray())
        .setDataSource(dataSource())
        .summary(pageFooterBuilder())
        .setSubtotalStyle(stl.style()
                            .setPadding(2)
                            .setTopBorder(stl.pen1Point()))
        .pageFooter(cmp.pageNumber()
                      .setHorizontalTextAlignment(RIGHT)
                      .setStyle(stl.style().setFontSize(8)))
        .toPdf(outputStream)
      outputStream.toByteArray()
    } catch(e: DRException) {
      e.printStackTrace()
      ByteArray(0)
    }
  }
  
  private fun pageFooterBuilder(): ComponentBuilder<*, *>? {
    return cmp.verticalList()
  }
  
  private fun titleBuider(): ComponentBuilder<*, *>? {
    return cmp.verticalList()
      .add(
        cmp.horizontalList()
          .add(
            cmp.text("ENGECOPI ${pedido.sigla()} - ROMANEIO DE SEPARAÇÃO PEDIDO DE ENTREGA: ${pedido.pedido} - ${
              pedido
                .data?.format()
            } -${pedido.hora.format()}")
              .setStyle(Templates.boldStyle)
              ),
        cmp.horizontalList()
          .add(
            cmp.text("LJ ${pedido.loja} ${pedido.pdv()} NF ${pedido.nfFat}  DATA ${pedido.dataFat.format()}  HORA ${
              pedido.horaFat
                .format()
            }  VALOR ${pedido.valor.format()}  VENDEDOR ${pedido.vendedor}")
              .setStyle(Templates.boldStyle)
              ),
        cmp.horizontalList()
          .add(
            cmp.text("CLIENTE ${pedido.cliente} ")
              .setStyle(Templates.boldStyle)
              ),
          cmp.horizontalList()
          .add(
            cmp.text("ENDEREÇO ${pedido.endereco}  ${pedido.bairro}  ${pedido.area}  ${pedido.rota}")
              .setStyle(Templates.boldStyle)
              )
          )
  }
  
  private fun dataSource(): List<ProdutoPedido> {
    return pedido.produtos()
  }
  
  private fun subtotalBuilder(): List<SubtotalBuilder<*, *>> {
    listOf(
      sbt.text("", colCodigo),
      sbt.text("", colDescricao),
      sbt.text("", colGrade),
      sbt.text("", colRefFab),
      sbt.text("", colCodBarras),
      sbt.text("", colQtd),
      sbt.text("", colVlUnit),
      sbt.sum(vlTotal)
        .setLabel("Total R$")
        .setLabelStyle(stl.style()
                         .setTopBorder(stl.pen1Point()))
        .setLabelPosition(Position.LEFT),
      sbt.text("", colLocalizacao))
    return emptyList()
  }
  
  private fun columnBuilder(): List<ColumnBuilder<*, *>> {
    return listOf(colCodigo, colDescricao, colGrade, colCodBarras, colQtd, colVlUnit, vlTotal)
  }
}
