package br.com.astrosoft.pedido.viewmodel.entrega

import br.com.astrosoft.framework.util.lpad
import br.com.astrosoft.pedido.model.beans.*

class PedidoEntregaRotaViewModel(val viewModel: PedidoEntregaViewModel) {
  private val subView
    get() = viewModel.view.tabEntregaRota

  private fun listPedidosEntregaRota(): List<Rota> {
    val filtro = subView.filtro()
    return Pedido.listaPedidoImpressoComNota(filtro).groupRoot()
  }

  fun updateGrid() {
    subView.updateGrid(listPedidosEntregaRota())
  }

  fun relatorioSimplificado() {
    val filtro = subView.filtro()
    val rotaAberta =  subView.rotaAberta()
    val listRotasLoja =
            Pedido.listaPedidoImpressoComNota(filtro)
              .groupRotaLoja().filter {
                rotaAberta ?: return@filter true
                it.nomeRota == rotaAberta.nomeRota
              }
              .sortedBy { it.nomeRota + it.loja.toString().lpad(2, "0") }

    subView.showRotaLoja(listRotasLoja)
  }
}

interface IPedidoEntregaRota {
  fun updateGrid(itens: List<Rota>)

  fun filtro(): FiltroPedido
  fun showRotaLoja(listRotasLoja: List<Rota>)
  fun rotaAberta(): Rota?
}