package br.com.astrosoft.pedido.view.entrega

import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnButton
import br.com.astrosoft.framework.view.addColumnSeq
import br.com.astrosoft.framework.view.shiftSelect
import br.com.astrosoft.pedido.model.beans.Pedido
import br.com.astrosoft.pedido.model.beans.PedidoGroup
import br.com.astrosoft.pedido.view.entrega.columns.*
import br.com.astrosoft.pedido.viewmodel.entrega.IPedidoEntregaImpressoSeparar
import br.com.astrosoft.pedido.viewmodel.entrega.PedidoEntregaImpressoSepararViewModel
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import org.claspina.confirmdialog.ButtonOption
import org.claspina.confirmdialog.ConfirmDialog

class TabEntregaImpressoSeparar(val viewModel: PedidoEntregaImpressoSepararViewModel) : TabPanelGrid<PedidoGroup>(),
        IPedidoEntregaImpressoSeparar {
  private var dialog: DlgPedidoSeparar? = null

  override val label = "Separar"

  override fun updateComponent() {
    viewModel.updateGridImpressoSeparar()
  }

  override fun itensSelecionadoPedido(): List<Pedido> {
    return dialog?.gridDetail?.selectedItems.orEmpty().toList()
  }

  override fun confirmaSeparado(exec: () -> Unit) {
    ConfirmDialog
      .createQuestion()
      .withCaption("Confirmação")
      .withMessage("Marca como separado?")
      .withYesButton({
                       exec()
                     },
                     ButtonOption.caption("Sim"))
      .withNoButton({ }, ButtonOption.caption("Não"))
      .open()
  }

  override fun confirmaRemoveCarga(exec: () -> Unit) {
    ConfirmDialog
      .createQuestion()
      .withCaption("Confirmação")
      .withMessage("Remove a carga?")
      .withYesButton({
                       exec()
                     },
                     ButtonOption.caption("Sim"))
      .withNoButton({ }, ButtonOption.caption("Não"))
      .open()
  }

  override fun classPanel() = PedidoGroup::class
  override fun HorizontalLayout.toolBarConfig() {
  }

  override fun Grid<PedidoGroup>.gridPanel() {
    setSelectionMode(Grid.SelectionMode.MULTI)

    addColumnSeq("Seq")
    addColumnButton(iconButton = VaadinIcon.TABLE, tooltip = "Pedidos", header = "Pedido") { grupo ->
      dialog = DlgPedidoSeparar(viewModel, grupo)
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
}