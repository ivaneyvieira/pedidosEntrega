package br.com.astrosoft.pedido.view

import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.pedido.model.beans.UserSaci
import br.com.astrosoft.pedido.viewmodel.IUsuarioView
import br.com.astrosoft.pedido.viewmodel.UsuarioViewModel
import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant.LUMO_ERROR
import com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant.LUMO_COMPACT
import com.vaadin.flow.component.icon.VaadinIcon.CHECK_CIRCLE_O
import com.vaadin.flow.component.icon.VaadinIcon.CIRCLE_THIN
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.END
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import org.claspina.confirmdialog.ConfirmDialog
import org.vaadin.crudui.crud.CrudOperation
import org.vaadin.crudui.crud.CrudOperation.*
import org.vaadin.crudui.crud.impl.GridCrud
import org.vaadin.crudui.form.AbstractCrudFormFactory
import org.vaadin.gatanaso.MultiselectComboBox

@Route(layout = PedidoEntregaLayout::class, value = "usuario")
@PageTitle("Usuário")
class UsuarioView : ViewLayout<UsuarioViewModel>(), IUsuarioView {
  override val viewModel = UsuarioViewModel(this)

  init {
    form("Editor de usuários")
    setSizeFull()
    val crud: GridCrud<UserSaci> = gridCrud() // layout configuration
    setSizeFull()
    this.add(crud) // logic configuration
    setOperationd(crud)
  }

  private fun gridCrud(): GridCrud<UserSaci> {
    val crud: GridCrud<UserSaci> = GridCrud<UserSaci>(UserSaci::class.java)
    crud.grid.setColumns(UserSaci::no.name, UserSaci::login.name, UserSaci::name.name, UserSaci::impressora.name)

    crud.grid.addThemeVariants(LUMO_COMPACT)

    crud.crudFormFactory = UserCrudFormFactory(viewModel)
    crud.setSizeFull()
    return crud
  }

  private fun setOperationd(crud: GridCrud<UserSaci>) {
    crud.setOperations({ viewModel.findAll() },
                       { user: UserSaci -> viewModel.add(user) },
                       { user: UserSaci? -> viewModel.update(user) },
                       { user: UserSaci? -> viewModel.delete(user) })
  }

  private fun Grid<UserSaci>.addColumnBool(caption: String, value: UserSaci.() -> Boolean) {
    val column = this.addComponentColumn { bean ->
      if (bean.value()) CHECK_CIRCLE_O.create()
      else CIRCLE_THIN.create()
    }
    column.setHeader(caption)
    column.textAlign = ColumnTextAlign.CENTER
  }
}

class UserCrudFormFactory(private val viewModel: UsuarioViewModel) : AbstractCrudFormFactory<UserSaci>() {
  private lateinit var comboAbreviacao: MultiselectComboBox<String>

  override fun buildNewForm(operation: CrudOperation?,
                            domainObject: UserSaci?,
                            readOnly: Boolean,
                            cancelButtonClickListener: ComponentEventListener<ClickEvent<Button>>?,
                            operationButtonClickListener: ComponentEventListener<ClickEvent<Button>>?): Component {
    val binder = Binder<UserSaci>(UserSaci::class.java)
    return VerticalLayout().apply {
      isSpacing = false
      isMargin = false
      formLayout {
        if (operation in listOf(READ, DELETE, UPDATE)) integerField("Número") {
          isReadOnly = true
          binder.bind(this, UserSaci::no.name)
        }
        if (operation in listOf(ADD, READ, DELETE, UPDATE)) textField("Login") {
          binder.bind(this, UserSaci::login.name)
        }
        if (operation in listOf(READ, DELETE, UPDATE)) textField("Nome") {
          isReadOnly = true
          binder.bind(this, UserSaci::name.name)
        }
        if (operation in listOf(READ, DELETE, UPDATE)) textField("Impressora") {
          isReadOnly = true
          binder.bind(this, UserSaci::impressora.name)
        }
        if (operation in listOf(READ, DELETE, UPDATE)) integerField("Número Loja") {
          isReadOnly = false
          binder.bind(this, UserSaci::storeno.name)
        }
        if (operation in listOf(ADD, READ, DELETE, UPDATE)) {
          checkBox("Entrega Imprimir") {
            binder.bind(this, UserSaci::entrega_imprimir.name)
          }
          checkBox("Entrega Impresso sem nota") {
            binder.bind(this, UserSaci::entrega_impressoSemNota.name)
          }
          checkBox("Entrega Pendente") {
            binder.bind(this, UserSaci::entrega_pendente.name)
          }
          checkBox("Entrega Editor de Nota") {
            binder.bind(this, UserSaci::entrega_impressoComNota.name)
          }
          checkBox("Desempenho Entrega") {
            binder.bind(this, UserSaci::entrega_entregador.name)
          }
          checkBox("Desempenho Rota") {
            binder.bind(this, UserSaci::entrega_rota.name)
          }
          checkBox("Carga") {
            binder.bind(this, UserSaci::entrega_carga.name)
          }
          checkBox("Separar") {
            binder.bind(this, UserSaci::entrega_separar.name)
          }
          checkBox("Separado") {
            binder.bind(this, UserSaci::entrega_separado.name)
          }
          checkBox("Remove carga") {
            binder.bind(this, UserSaci::entrega_removerCarga.name)
          }
          checkBox("Envia para separado") {
            binder.bind(this, UserSaci::entrega_enviarSeparado.name)
          }
          checkBox("Volta para separar") {
            binder.bind(this, UserSaci::entrega_voltaSeparar.name)
          }

          checkBox("Retira Imprimir") {
            binder.bind(this, UserSaci::retira_imprimir.name)
          }
          checkBox("Retira Impresso sem nota") {
            binder.bind(this, UserSaci::retira_impressoSemNota.name)
          }
          checkBox("Retira Pendente") {
            binder.bind(this, UserSaci::retira_pendente.name)
          }
          checkBox("Retira Editor de Nota") {
            binder.bind(this, UserSaci::retira_impressoComNota.name)
          }

          checkBox("E-Commerce Entrega Imprimir") {
            binder.bind(this, UserSaci::ecommerceE_imprimir.name)
          }
          checkBox("E-Commerce Entrega Impresso sem nota") {
            binder.bind(this, UserSaci::ecommerceE_impressoSemNota.name)
          }
          checkBox("E-Commerce Entrega Editor de Nota") {
            binder.bind(this, UserSaci::ecommerceE_impressoComNota.name)
          }
          checkBox("E-Commerce Desempenho Entrega") {
            binder.bind(this, UserSaci::ecommerceE_entregador.name)
          }

          checkBox("E-Commerce Retira Imprimir") {
            binder.bind(this, UserSaci::ecommerceR_imprimir.name)
          }
          checkBox("E-Commerce Retira Impresso sem nota") {
            binder.bind(this, UserSaci::ecommerceR_impressoSemNota.name)
          }
          checkBox("E-Commerce Retira Editor de Nota") {
            binder.bind(this, UserSaci::ecommerceR_impressoComNota.name)
          }
        }
      }
      hr()
      horizontalLayout {
        this.setWidthFull()
        this.justifyContentMode = JustifyContentMode.END
        button("Confirmar") {
          alignSelf = END
          addThemeVariants(LUMO_PRIMARY)
          addClickListener {
            binder.writeBean(domainObject)
            operationButtonClickListener?.onComponentEvent(it)
          }
        }
        button("Cancelar") {
          alignSelf = END
          addThemeVariants(LUMO_ERROR)
          addClickListener(cancelButtonClickListener)
        }
      }

      binder.readBean(domainObject)
    }
  }

  override fun buildCaption(operation: CrudOperation?, domainObject: UserSaci?): String {
    return operation?.let { crudOperation ->
      when (crudOperation) {
        READ   -> "Consulta"
        ADD    -> "Adiciona"
        UPDATE -> "Atualiza"
        DELETE -> "Remove"
      }
    } ?: "Erro"
  }

  override fun showError(operation: CrudOperation?, e: Exception?) {
    ConfirmDialog.createError().withCaption("Erro do aplicativo").withMessage(e?.message ?: "Erro desconhecido").open()
  }
}