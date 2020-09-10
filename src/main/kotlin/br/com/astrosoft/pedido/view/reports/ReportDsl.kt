package br.com.astrosoft.pedido.view.reports

import net.sf.dynamicreports.report.builder.DynamicReports.cmp
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.LEFT

fun horizontalFlowList(block: (HorizontalListBuilder) -> HorizontalListBuilder): HorizontalListBuilder {
  val hrizontal = cmp.horizontalFlowList()
  block(hrizontal)
  return hrizontal
}

fun verticalList(block: VerticalListBuilder.() -> VerticalListBuilder): VerticalListBuilder {
  val vertical = cmp.verticalList()
  block(vertical)
  return vertical
}

fun VerticalListBuilder.horizontalFlowList(block: HorizontalListBuilder.() -> Unit): VerticalListBuilder {
  val horizontal = cmp.horizontalList()
  block(horizontal)
  return this.add(horizontal)
}

fun HorizontalListBuilder.verticalList(block: (VerticalListBuilder) -> Unit): HorizontalListBuilder {
  val vertical = cmp.verticalList()
  block(vertical)
  return this.add(vertical)
}

fun VerticalListBuilder.breakLine(): VerticalListBuilder {
  return this.add(cmp.text(""))
}

fun HorizontalListBuilder.text(text: String, horizontalTextAlignment: HorizontalTextAlignment = LEFT, width: Int = 0,
                               block: TextFieldBuilder<String>.() -> Unit = {}): HorizontalListBuilder? {
  val textString = cmp.text(text)
    .setHorizontalTextAlignment(horizontalTextAlignment)
  if(width > 0)
    textString.setFixedWidth(width)
  textString.block()
  return this.add(textString)
}

fun VerticalListBuilder.text(text: String, horizontalTextAlignment: HorizontalTextAlignment = LEFT, width: Int = 0,
                             block: TextFieldBuilder<String>.() -> Unit = {}): VerticalListBuilder? {
  val textString = cmp.text(text)
    .setHorizontalTextAlignment(horizontalTextAlignment)
  if(width > 0)
    textString.setFixedWidth(width)
  textString.block()
  return this.add(textString)
}