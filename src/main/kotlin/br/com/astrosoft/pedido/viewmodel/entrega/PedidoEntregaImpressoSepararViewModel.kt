package br.com.astrosoft.pedido.viewmodel.entrega

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.util.ECupsPrinter
import br.com.astrosoft.framework.viewmodel.exec
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.pedido.model.QuerySaci
import br.com.astrosoft.pedido.model.beans.ETipoPedido.ENTREGA
import br.com.astrosoft.pedido.model.beans.FiltroPedido
import br.com.astrosoft.pedido.model.beans.Pedido
import br.com.astrosoft.pedido.model.beans.PedidoGroup
import br.com.astrosoft.pedido.model.beans.groupBy

class PedidoEntregaImpressoSepararViewModel(val viewModel: PedidoEntregaViewModel) {
  private val subView
    get() = viewModel.view.tabEntregaImpressoSeparar

  private fun listPedidosEntregaImpressoSeparar(): List<PedidoGroup> {
    val pesquisa = ""
    return Pedido
      .listaPedidoImpressoSeparar(FiltroPedido(tipo = ENTREGA,
                                               pesquisa = pesquisa,
                                               ecommerce = false,
                                               dataInicial = null,
                                               dataFinal = null))
      .filter {
        it.entrega != null && (it.loc.startsWith("CD5A") && it.separado != "S")
      }
      .groupBy()
  }

  fun updateGridImpressoSeparar() {
    subView.updateGrid(listPedidosEntregaImpressoSeparar())
  }

  fun desmarcaSeparar() = exec(viewModel.view) {
    val pedidos = subView.itensSelecionadoPedido().ifEmpty { fail("Não há pedido selecionado") }
    pedidos.forEach { pedido ->
      pedido.desmarcaImpresso()
    }

    updateGridImpressoSeparar()
  }

  fun marcaSeparado() = exec(viewModel.view) {
    val pedidos = subView.itensSelecionadoPedido().ifEmpty { fail("Não há pedido selecionado") }
    subView.confirmaSeparado {
      pedidos.forEach { pedido ->
        pedido.marcaSeparado("S")
      }

      updateGridImpressoSeparar()
    }
  }

  fun imprimirPedidos(itensSelecionado: List<Pedido>) {
    viewModel.tabEntregaImprimirViewModel.imprimirPedidos(itensSelecionado)
  }

  fun removeCarga() {
    val pedidos = subView.itensSelecionadoPedido().ifEmpty { fail("Não há pedido selecionado") }
    subView.confirmaRemoveCarga {
      pedidos.forEach { pedido ->
        pedido.removeCarga()
      }

      updateGridImpressoSeparar()
    }
  }

  fun printRelatorio(pedido: Pedido) = viewModel.exec {
    val listaRelatorio = pedido.listaRelatorio().filter { relatorio ->
      relatorio.localizacao == pedido.loc
    }
    if (listaRelatorio.isEmpty()) {
      fail("Não produtos para imprimir com a localização ${pedido.loc}")
    }
    val impressora = AppConfig.userSaci?.impressoraTermica
    if ((impressora == "" || impressora == null) && !QuerySaci.test) {
      fail("Impressora termica não configurada")
    }
    else {
      try {
        RelatorioText().print(impressora ?: "teste", listaRelatorio)
      } catch (e: ECupsPrinter) {
        fail(e.message ?: "Erro de impressão")
      }
    }
  }

  fun printRelatorio(itensSelecionado: List<Pedido>) = viewModel.exec {
    if (itensSelecionado.isEmpty()) {
      fail("Nenhum pedido selecionado")
    }
    viewModel.view.showConfirmation("Imprime os pedidos?") {
      itensSelecionado.forEach { pedido ->
        printRelatorio(pedido)
        marcaSeparado()
      }
    }
  }
}

interface IPedidoEntregaImpressoSeparar {
  fun updateGrid(itens: List<PedidoGroup>)
  fun itensSelecionadoPedido(): List<Pedido>

  fun confirmaSeparado(exec: () -> Unit)
  fun confirmaRemoveCarga(exec: () -> Unit)
}