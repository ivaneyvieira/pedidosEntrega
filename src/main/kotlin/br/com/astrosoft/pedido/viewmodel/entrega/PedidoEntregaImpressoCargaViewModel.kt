package br.com.astrosoft.pedido.viewmodel.entrega

import br.com.astrosoft.framework.viewmodel.exec
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.pedido.model.beans.ETipoPedido.ENTREGA
import br.com.astrosoft.pedido.model.beans.FiltroPedido
import br.com.astrosoft.pedido.model.beans.Pedido
import java.time.LocalDate

class PedidoEntregaImpressoCargaViewModel(val viewModel: PedidoEntregaViewModel) {
  private val subView
    get() = viewModel.view.tabEntregaImpressoCarga

  private fun listPedidosEntregaImpressoCarga(): List<Pedido> {
    val pesquisa = subView.pedidoPesquisa
    return Pedido
      .listaPedidoImpressoCarga(FiltroPedido(tipo = ENTREGA,
                                             pesquisa = pesquisa,
                                             ecommerce = false,
                                             dataInicial = null,
                                             dataFinal = null))
      .filter {
        it.zonaCarga == "" && it.data?.isAfter(LocalDate.of(2022, 10, 6)) == true
      }
  }

  fun updateGridImpressoCarga() {
    subView.updateGrid(listPedidosEntregaImpressoCarga())
  }

  fun desmarcaCarga() = exec(viewModel.view) {
    val pedidos = subView.itensSelecionado().ifEmpty { fail("Não há pedido selecionado") }
    pedidos.forEach { pedido ->
      pedido.desmarcaImpresso()
    }

    updateGridImpressoCarga()
  }

  fun marcaCarga() = exec(viewModel.view) {
    val pedidos = subView.itensSelecionado().ifEmpty { fail("Não há pedido selecionado") }
    subView.selecionaCarga { carga, entrega ->
      pedidos.forEach { pedido ->
        pedido.marcaCarga(carga, entrega)
      }
      updateGridImpressoCarga()
    }
  }

  fun marcaSeparado() = exec(viewModel.view) {
    val pedidos = subView.itensSelecionado().ifEmpty { fail("Não há pedido selecionado") }
    pedidos.forEach { pedido ->
      pedido.marcaSeparado("S")
    }

    updateGridImpressoCarga()
  }

  fun imprimirPedidos(itensSelecionado: List<Pedido>) {
    viewModel.tabEntregaImprimirViewModel.imprimirPedidos(itensSelecionado)
  }
}

interface IPedidoEntregaImpressoCarga {
  fun updateGrid(itens: List<Pedido>)
  fun itensSelecionado(): List<Pedido>
  val pedidoPesquisa: String
  fun selecionaCarga(exec: (EZonaCarga, LocalDate?) -> Unit)
}

enum class EZonaCarga(val codigo: Char, val descricao: String) {
  Leste1('A', "Leste 1"),
  Leste2('B', "Leste 2"),
  Leste3('I', "Leste 3"),
  Norte1('C', "Norte 1"),
  Norte2('D', "Norte 2"),
  Norte3('J', "Norte 3"),
  Sul1('E', "Sul 1"),
  Sul2('F', "Sul 2"),
  Sul3('G', "Sul 3"),
  Motoboy('K', "Motoboy"),
  Timon('H', "Timon"),
  SemZona(' ', "")
}