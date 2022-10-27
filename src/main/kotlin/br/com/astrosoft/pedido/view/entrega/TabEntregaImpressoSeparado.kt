package br.com.astrosoft.pedido.view.entrega

import br.com.astrosoft.framework.view.*
import br.com.astrosoft.pedido.model.beans.Pedido
import br.com.astrosoft.pedido.model.beans.PedidoGroup
import br.com.astrosoft.pedido.view.*
import br.com.astrosoft.pedido.view.entrega.columns.*
import br.com.astrosoft.pedido.viewmodel.entrega.IPedidoEntregaImpressoSeparado
import br.com.astrosoft.pedido.viewmodel.entrega.PedidoEntregaImpressoSeparadoViewModel
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import org.claspina.confirmdialog.ButtonOption
import org.claspina.confirmdialog.ConfirmDialog

class TabEntregaImpressoSeparado(val viewModel: PedidoEntregaImpressoSeparadoViewModel) : TabPanelGrid<PedidoGroup>(),
        IPedidoEntregaImpressoSeparado {

  private var dialog: DlgPedidoSeparado? = null
  override val label = "Separado"

  override fun updateComponent() {
    viewModel.updateGridImpressoSeparado()
  }

  override fun classPanel() = PedidoGroup::class

  override fun HorizontalLayout.toolBarConfig() {
  }

  override fun Grid<PedidoGroup>.gridPanel() {
    setSelectionMode(Grid.SelectionMode.MULTI)

    addColumnSeq("Seq")
    addColumnButton(iconButton = VaadinIcon.TABLE, tooltip = "Pedidos", header = "Pedido"){grupo ->
      dialog = DlgPedidoSeparado(viewModel, grupo)
      dialog?.showDialog()
    }
    pedidoData()
    pedidoCarga()
    pedidoLoc()
    pedidoPiso()
    pedidoTotal()
    pedidoQuant()

    shiftSelect()
  }

  override fun itensSelecionadoPedido(): List<Pedido> {
    return dialog?.gridDetail?.selectedItems.orEmpty().toList()
  }

  override fun confirmaVolta(exec: () -> Unit) {
    ConfirmDialog
      .createQuestion()
      .withCaption("Confirmação")
      .withMessage("Volta para a separação?")
      .withYesButton({
                       exec()
                     },
                     ButtonOption.caption("Sim"))
      .withNoButton({ }, ButtonOption.caption("Não"))
      .open()
  }
}