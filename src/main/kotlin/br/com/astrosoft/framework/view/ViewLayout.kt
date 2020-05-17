package br.com.astrosoft.framework.view

import br.com.astrosoft.pedidoEntrega.model.beans.UserSaci
import br.com.astrosoft.pedidoEntrega.model.saci
import br.com.astrosoft.pedidoEntrega.view.LoginService
import br.com.astrosoft.framework.model.RegistryUserInfo
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import com.github.mvysny.karibudsl.v10.KFormLayout
import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.addColumnFor
import com.github.mvysny.karibudsl.v10.em
import com.github.mvysny.karibudsl.v10.formLayout
import com.github.mvysny.karibudsl.v10.horizontalLayout
import com.github.mvysny.karibudsl.v10.isExpand
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.renderer.LocalDateRenderer
import com.vaadin.flow.data.renderer.NumberRenderer
import com.vaadin.flow.router.AfterNavigationEvent
import com.vaadin.flow.router.AfterNavigationObserver
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.BeforeLeaveEvent
import com.vaadin.flow.router.BeforeLeaveObserver
import org.claspina.confirmdialog.ButtonOption
import org.claspina.confirmdialog.ConfirmDialog
import java.text.DecimalFormat
import java.time.LocalDate
import kotlin.reflect.KProperty1
import com.vaadin.flow.component.grid.ColumnTextAlign.CENTER
import com.vaadin.flow.component.grid.ColumnTextAlign.END
import com.vaadin.flow.component.grid.ColumnTextAlign.START
import com.vaadin.flow.data.renderer.TextRenderer
import java.time.LocalTime

abstract class ViewLayout<VM: ViewModel<*>>: VerticalLayout(), IView, BeforeLeaveObserver,
                                             BeforeEnterObserver, AfterNavigationObserver {
  abstract val viewModel: VM
  
  init {
    width = "100%"
    height = "100%"
  }
  
  abstract fun isAccept(user: UserSaci): Boolean
  
  override fun showError(msg: String) {
    ConfirmDialog.createError()
      .withCaption("Erro do aplicativo")
      .withMessage(msg)
      .open()
  }
  
  override fun showWarning(msg: String) {
    ConfirmDialog.createWarning()
      .withCaption("Aviso")
      .withMessage(msg)
      .open()
  }
  
  override fun showInformation(msg: String) {
    ConfirmDialog.createInfo()
      .withCaption("Informação")
      .withMessage(msg)
      .open()
  }
  
  fun showQuestion(msg: String, execYes: () -> Unit) {
    showQuestion(msg, execYes, {})
  }
  
  private fun showQuestion(msg: String, execYes: () -> Unit, execNo: () -> Unit) {
    ConfirmDialog.createQuestion()
      .withCaption("Confirmação")
      .withMessage(msg)
      .withYesButton(Runnable {
        execYes()
      }, ButtonOption.caption("Sim"))
      .withNoButton(Runnable {execNo()}, ButtonOption.caption("Não"))
      .open()
  }
  
  override fun beforeLeave(event: BeforeLeaveEvent?) {
  }
  
  override fun beforeEnter(event: BeforeEnterEvent?) {
    if(!LoginService.isLogged())
      event?.forwardTo(LoginView::class.java)
    else {
      saci.findUser(RegistryUserInfo.usuario)
        ?.let {usuario ->
          if(!isAccept(usuario))
            event?.rerouteTo(AccessNotAllowed::class.java)
        }
    }
  }
  
  override fun afterNavigation(event: AfterNavigationEvent?) {
    //   loginForm.isOpened = LoginService.isLogged() == false
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
  
  fun VerticalLayout.toolbar(compnentes: HorizontalLayout.() -> Unit) {
    horizontalLayout {
      width = "100%"
      compnentes()
    }
  }
}

fun <T> (@VaadinDsl Grid<T>).addColumnString(
  property: KProperty1<T, String?>,
  block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}
                                            ): Grid.Column<T> {
  val column = this.addColumnFor(property)
  column.isAutoWidth = true
  column.left()
  column.block()
  return column
}

fun <T> (@VaadinDsl Grid<T>).addColumnBool(
  property: KProperty1<T, Boolean?>,
  block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}
                                          ): Grid.Column<T> {
  val column = this.addComponentColumn {bean ->
    val boleanValue = property.get(bean) ?: false
    if(boleanValue) VaadinIcon.CHECK_CIRCLE_O.create()
    else VaadinIcon.CIRCLE_THIN.create()
  }
  column.isAutoWidth = true
  column.center()
  column.block()
  return column
}

fun <T> (@VaadinDsl Grid<T>).addColumnDate(
  property: KProperty1<T, LocalDate?>,
  block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}
                                          ): Grid.Column<T> {
  val column = this.addColumnFor(property,
                                 renderer = LocalDateRenderer(property, "dd/MM/yyyy"))
  column.isAutoWidth = true
  column.left()
  column.block()
  return column
}

fun <T> (@VaadinDsl Grid<T>).addColumnTime(
  property: KProperty1<T, LocalTime?>,
  block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}
                                          ): Grid.Column<T> {
  val column = this.addColumnFor(property,
                                 TextRenderer{bean->
                                   val hora = property.get(bean)
                                    hora.format()
                                 })
  column.isAutoWidth = true
  column.left()
  column.block()
  return column
}

private val formatNumber = DecimalFormat("#,##0.00")

fun <T> (@VaadinDsl Grid<T>).addColumnDouble(
  property: KProperty1<T, Double?>,
  block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}
                                            ): Grid.Column<T> {
  val column = this.addColumnFor(property,
                                 renderer = NumberRenderer(property, formatNumber))
  column.isAutoWidth = true
  column.right()
  column.block()
  return column
}

fun <T> (@VaadinDsl Grid<T>).addColumnInt(
  property: KProperty1<T, Int?>,
  block: (@VaadinDsl Grid.Column<T>).() -> Unit = {}
                                         ): Grid.Column<T> {
  val column = this.addColumnFor(property)
  column.isAutoWidth = true
  column.right()
  column.block()
  return column
}

fun <T> (@VaadinDsl Grid.Column<T>).right() {
  this.textAlign = END
}

fun <T> (@VaadinDsl Grid.Column<T>).left() {
  this.textAlign = START
}

fun <T> (@VaadinDsl Grid.Column<T>).center() {
  this.textAlign = CENTER
}

