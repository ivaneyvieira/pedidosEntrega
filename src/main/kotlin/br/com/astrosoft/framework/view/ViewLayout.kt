package br.com.astrosoft.framework.view

import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.kaributools.tooltip
import com.vaadin.flow.component.*
import com.vaadin.flow.component.charts.model.style.SolidColor
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.datepicker.DatePicker.DatePickerI18n
import com.vaadin.flow.component.grid.ColumnTextAlign.*
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs.SelectedChangeEvent
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.renderer.LocalDateRenderer
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer
import com.vaadin.flow.data.renderer.NumberRenderer
import com.vaadin.flow.data.renderer.TextRenderer
import com.vaadin.flow.router.*
import com.vaadin.flow.shared.Registration
import org.claspina.confirmdialog.ButtonOption
import org.claspina.confirmdialog.ConfirmDialog
import org.vaadin.stefan.LazyDownloadButton
import java.io.ByteArrayInputStream
import java.sql.Time
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.reflect.KProperty1

abstract class ViewLayout<VM : ViewModel<*>> : VerticalLayout(), IView, BeforeLeaveObserver, BeforeEnterObserver,
        AfterNavigationObserver {
  abstract val viewModel: VM

  init {
    this.setSizeFull()
  }

  override fun showError(msg: String) {
    ConfirmDialog.createError().withCaption("Erro do aplicativo").withMessage(msg).open()
  }

  override fun showWarning(msg: String) {
    ConfirmDialog.createWarning().withCaption("Aviso").withMessage(msg).open()
  }

  override fun showInformation(msg: String) {
    ConfirmDialog.createInfo().withCaption("Informação").withMessage(msg).open()
  }

  override fun showConfirmation(msg: String, execYes: () -> Unit) {
    ConfirmDialog
      .createQuestion()
      .withCaption("Confirmação")
      .withMessage(msg)
      .withYesButton(execYes, ButtonOption.caption("Sim"))
      .withNoButton(ButtonOption.caption("Não"))
      .open()
  }

  override fun showReport(chave: String, report: ByteArray) {
    SubWindowPDF(chave, report).open()
  }

  fun showQuestion(msg: String, execYes: () -> Unit) {
    showQuestion(msg, execYes)
  }

  private fun showQuestion(msg: String, execYes: () -> Unit, execNo: () -> Unit) {
    ConfirmDialog
      .createQuestion()
      .withCaption("Confirmação")
      .withMessage(msg)
      .withYesButton({
                       execYes()
                     },
                     ButtonOption.caption("Sim"))
      .withNoButton({ execNo() }, ButtonOption.caption("Não"))
      .open()
  }

  override fun beforeLeave(event: BeforeLeaveEvent?) {
  }

  override fun beforeEnter(event: BeforeEnterEvent?) {/*
    if(!LoginService.isLogged())
      event?.forwardTo(LoginView::class.java)
    else {
      saci.findUser(RegistryUserInfo.usuario)
        ?.let {usuario ->
          if(!isAccept(usuario))
            event?.rerouteTo(AccessNotAllowed::class.java)
        }
    }*/
  }

  override fun afterNavigation(event: AfterNavigationEvent?) { //   loginForm.isOpened = LoginService.isLogged() == false
  }

  fun VerticalLayout.form(title: String, componentes: KFormLayout.() -> Unit = {}) {
    formLayout {
      isExpand = true

      em(title) {
        colspan = 2
      }
      componentes()
    }
  }

  fun HasComponents.toolbar(compnentes: HorizontalLayout.() -> Unit) {
    this.horizontalLayout {
      width = "100%"
      compnentes()
    }
  }
}

fun (@VaadinDsl TabSheet).selectedChange(onEvent: (event: SelectedChangeEvent) -> Unit) {
  addSelectedChangeListener { event -> onEvent(event) }
}

fun <T : Any> (@VaadinDsl Grid<T>).addColumnButton(iconButton: VaadinIcon,
                                                   tooltip: String? = null,
                                                   execButton: (T) -> Unit = {},
                                                   configIcon: (Icon, T) -> Unit = { _, _ -> },
                                                   block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}): Grid.Column<T> {
  return addComponentColumn { bean ->
    Icon(iconButton).apply {
      configIcon(this, bean)
      this.style.set("cursor", "pointer")
      if (tooltip != null) this.tooltip = tooltip
      onLeftClick {
        execButton(bean)
      }
    }
  }.apply {
    this.isAutoWidth = false
    this.width = "4em"
    this.center()
    this.block()
  }
}

fun <T : Any> (@VaadinDsl Grid<T>).addColumnButton(iconButton: VaadinIcon,
                                                   tooltip: String? = null,
                                                   header: String? = null,
                                                   configIcon: (Icon, T) -> Unit = { _, _ -> },
                                                   execButton: (T) -> Unit = {}): Grid.Column<T> {
  return addColumnButton(iconButton, tooltip, execButton, configIcon, block = {
    this.setHeader(header)
    this.isAutoWidth = false
    this.width = "3em"
  })
}

fun <T : Any> (@VaadinDsl Grid<T>).addColumnString(property: KProperty1<T, String?>,
                                                   block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}): Grid.Column<T> {
  return this.addColumnFor(property) {
    this.isAutoWidth = true
    if (this.key == null) this.key = property.name
    this.left()
    this.block()
  }
}

fun <T : Any> (@VaadinDsl Grid<T>).addColumnBool(property: KProperty1<T, Boolean?>,
                                                 block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}): Grid.Column<T> {
  val column = this.addComponentColumn { bean ->
    val boleanValue = property.get(bean) ?: false
    if (boleanValue) VaadinIcon.CHECK_CIRCLE_O.create()
    else VaadinIcon.CIRCLE_THIN.create()
  }
  column.isAutoWidth = true
  if (column.key == null) column.key = property.name
  column.center()
  column.block()
  return column
}

fun <T : Any> (@VaadinDsl Grid<T>).addColumnLocalDate(property: KProperty1<T, LocalDate?>,
                                                      block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}): Grid.Column<T> {
  return this.addColumnFor(property, renderer = LocalDateRenderer(property, "dd/MM/yyyy")) {
    this.isAutoWidth = true
    if (this.key == null) this.key = property.name
    this.left()
    this.setComparator { a, b ->
      val dataA = property.get(a) ?: LocalDate.of(1900, 1, 1)
      val dataB = property.get(b) ?: LocalDate.of(1900, 1, 1)
      dataA.compareTo(dataB)
    }
    this.block()
  }
}

fun <T : Any> (@VaadinDsl Grid<T>).addColumnDate(property: KProperty1<T, Date?>,
                                                 block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}): Grid.Column<T> {
  return this.addColumnFor(property, renderer = TextRenderer { bean ->
    val date = property.get(bean)
    date.format()
  }) {
    this.isAutoWidth = true
    if (this.key == null) this.key = property.name
    this.left()

    this.block()
  }
}

fun <T : Any> (@VaadinDsl Grid<T>).addColumnLocalTime(property: KProperty1<T, LocalTime?>,
                                                      block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}): Grid.Column<T> {
  return this.addColumnFor(property, TextRenderer { bean ->
    val hora = property.get(bean)
    hora.format()
  }) {
    this.isAutoWidth = true
    if (this.key == null) this.key = property.name
    this.left()
    this.block()
  }
}

fun <T : Any> (@VaadinDsl Grid<T>).addColumnTime(property: KProperty1<T, Time?>,
                                                 block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}): Grid.Column<T> {
  return this.addColumnFor(property, TextRenderer { bean ->
    val hora = property.get(bean)
    hora.format()
  }) {
    this.isAutoWidth = true
    if (this.key == null) this.key = property.name
    this.left()
    this.setComparator { a, b ->
      val dataA = property.get(a) ?: Time(0)
      val dataB = property.get(b) ?: Time(0)
      dataA.compareTo(dataB)
    }
    this.block()
  }
}

fun <T : Any> (@VaadinDsl Grid<T>).addColumnLocalDateTime(property: KProperty1<T, LocalDateTime?>,
                                                          block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}): Grid.Column<T> {
  return this.addColumnFor(property, renderer = LocalDateTimeRenderer(property, "dd/MM/yyyy hh:mm:ss")) {
    if (this.key == null) this.key = property.name
    this.isAutoWidth = true
    this.left()
    this.setComparator { a, b ->
      val dataA = property.get(a) ?: LocalDateTime.MIN
      val dataB = property.get(b) ?: LocalDateTime.MIN
      dataA.compareTo(dataB)
    }

    this.block()
  }
}

fun <T : Any> (@VaadinDsl Grid<T>).addColumnDouble(property: KProperty1<T, Double?>,
                                                   block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}): Grid.Column<T> {
  return this.addColumnFor(property, renderer = NumberRenderer(property, DecimalFormat("#,##0.00"))) {
    this.isAutoWidth = true
    this.setComparator { a, b ->
      val dataA = property.get(a) ?: Double.MIN_VALUE
      val dataB = property.get(b) ?: Double.MIN_VALUE
      dataA.compareTo(dataB)
    }
    if (this.key == null) this.key = property.name
    this.right()
    this.block()
  }
}

fun <T : Any> (@VaadinDsl Grid<T>).addColumnInt(property: KProperty1<T, Int?>,
                                                block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}): Grid.Column<T> {
  return this.addColumnFor(property) {
    if (this.key == null) this.key = property.name
    this.isAutoWidth = true
    this.setComparator { a, b ->
      val dataA = property.get(a) ?: Int.MIN_VALUE
      val dataB = property.get(b) ?: Int.MIN_VALUE
      dataA.compareTo(dataB)
    }
    this.right()
    this.block()
  }
}

fun <T : Any> (@VaadinDsl Grid.Column<T>).right() {
  this.textAlign = END
}

fun <T : Any> (@VaadinDsl Grid.Column<T>).left() {
  this.textAlign = START
}

fun <T : Any> (@VaadinDsl Grid.Column<T>).center() {
  this.textAlign = CENTER
}

fun Component.style(name: String, value: String) {
  this.element.style.set(name, value)
}

fun Component.background(color: SolidColor) {
  this.style("background", color.toString())
}

class TabClick(s: String?) : Tab(s) {
  @DomEvent("click")
  class ClickTabEvent(source: Tab?, fromClient: Boolean) : ComponentEvent<Tab?>(source, fromClient)

  fun addClickListener(listener: ComponentEventListener<ClickTabEvent?>?): Registration {
    return addListener(ClickTabEvent::class.java, listener)
  }
}

fun DatePicker.localePtBr() {
  this.locale = Locale("pt-br")
  this.i18n =
    DatePickerI18n()
      .setWeek("semana")
      .setCalendar("calendário")
      .setClear("apagar")
      .setToday("hoje")
      .setCancel("cancelar")
      .setFirstDayOfWeek(1)
      .setMonthNames(listOf("janeiro",
                            "fevereiro",
                            "março",
                            "abril",
                            "maio",
                            "junho",
                            "julho",
                            "agosto",
                            "setembro",
                            "outubro",
                            "novembro",
                            "dezembro"))
      .setWeekdays(listOf("domingo", "segunda", "terça", "quarta", "quinta", "sexta", "sábado"))
      .setWeekdaysShort(listOf("dom", "seg", "ter", "qua", "qui", "sex", "sab"))
}

fun <T> ListDataProvider<T>.updateItens(itens: List<T>) {
  this.items.clear() //  this.items.addAll(itens.sortedBy {it.hashCode()})
  this.items.addAll(itens)
  this.refreshAll()
}

@VaadinDsl
fun (@VaadinDsl HasComponents).buttonPlanilha(text: String,
                                              icon: Component,
                                              chave: String,
                                              blockByteArray: () -> ByteArray): LazyDownloadButton {
  val lazyDownloadButton = LazyDownloadButton(text, icon, { filename(chave) }) {
    ByteArrayInputStream(blockByteArray())
  }
  return init(lazyDownloadButton)
}

private fun filename(chave: String): String {
  val sdf = DateTimeFormatter.ofPattern("yyMMddHHmmss")
  val textTime = LocalDateTime.now().format(sdf)
  return "$chave$textTime.xlsx"
}