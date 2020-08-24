package br.com.astrosoft.pedidoEntrega.view.reports

import net.sf.dynamicreports.report.builder.DynamicReports
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.CENTER
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.LEFT
import net.sf.dynamicreports.report.constant.VerticalTextAlignment.MIDDLE
import java.awt.Color
import java.util.*

object Templates {
  val rootStyle = DynamicReports.stl.style()
    .setPadding(2)
  val boldStyle = DynamicReports.stl.style(rootStyle)
    .bold()
  val italicStyle = DynamicReports.stl.style(rootStyle)
    .italic()
  val boldCenteredStyle = DynamicReports.stl.style(boldStyle)
    .setTextAlignment(CENTER, MIDDLE)
  val bold9CenteredStyle = DynamicReports.stl.style(boldCenteredStyle)
    .setFontSize(9)
  val columnStyle = DynamicReports.stl.style(rootStyle)
    .setFontSize(8)
  val columnTitleStyle = DynamicReports.stl.style(columnStyle)
    .setBorder(DynamicReports.stl.pen1Point())
    .setHorizontalTextAlignment(CENTER)
    .setBackgroundColor(Color.LIGHT_GRAY)
    .bold()
  val groupStyle =
    DynamicReports.stl.style(boldStyle)
      .setHorizontalTextAlignment(LEFT)
  val subtotalStyle =
    DynamicReports.stl.style(boldStyle)
  val reportTemplate =
    DynamicReports.template()
      .setLocale(Locale.UK)
      .setColumnStyle(columnStyle)
      .setColumnTitleStyle(columnTitleStyle)
      .setGroupStyle(groupStyle)
      .setGroupTitleStyle(groupStyle)
      .setSubtotalStyle(subtotalStyle)
      .setDetailStyle(DynamicReports.stl.style(rootStyle)
                        .setFontSize(8))
}