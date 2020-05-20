package br.com.astrosoft.pedidoEntrega.viewmodel

import br.com.astrosoft.framework.util.Ssh
import br.com.astrosoft.framework.util.execCommand
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.pedidoEntrega.model.beans.PedidoEntrega
import br.com.astrosoft.pedidoEntrega.model.beans.UserSaci
import br.com.astrosoft.pedidoEntrega.model.beans.UserSaci.Companion
import java.time.LocalDate

class PedidoEntregaViewModel(view: IPedidoEntregaView): ViewModel<IPedidoEntregaView>(view) {
  fun imprimir() = exec {
    val pedidos =
      view.itensSelecionado()
        .ifEmpty {fail("Não há pedido selecionado")}
    val impressora = UserSaci.userAtual?.impressora ?: fail("O usuário não possui impresseora")
    pedidos.forEach {pedido ->
      printPedido(pedido.loja, pedido.pedido, impressora)
    }
  }
  
  private fun printPedido(storeno: Int, ordno: Int, impressora: String) {
    Ssh("172.20.47.1", "ivaney", "ivaney").shell {
      execCommand("/u/saci/shells/printPedidos.sh $storeno $ordno $impressora")
    }
  }
  
  fun updateGridImprimir() {
    view.updateGridImprimir(listPedidosEntregaImprimir())
  }
  
  fun updateGridImpressoComNota() {
    view.updateGridImpressoComNota(listPedidosEntregaImpressoComNota())
  }
  
  fun updateGridImpressoSemNota() {
    view.updateGridImpressoSemNota(listPedidosEntregaImpressoSemNota())
  }
  
  private fun listPedidosEntregaImprimir(): List<PedidoEntrega> {
    val numPedido = view.pedidoImprimir
    val data = view.dataImprimir
    val area = view.areaImprimir.trim()
    val rota = view.rotaImprimir.trim()
    return PedidoEntrega.listaPedidoImprimir()
      .filter {pedido ->
        (pedido.pedido == numPedido || numPedido == 0) &&
        (pedido.data == data || data == null) &&
        (pedido.rota.contains(rota) || rota == "") &&
        (pedido.area.contains(area) || area == "")
      }
  }
  
  private fun listPedidosEntregaImpressoSemNota(): List<PedidoEntrega> {
    val numPedido = view.pedidoImpressoSemNota
    return PedidoEntrega.listaPedidoImpressoSemNota()
      .filter {pedido ->
        pedido.pedido == numPedido || numPedido == 0
      }
  }
  
  private fun listPedidosEntregaImpressoComNota(): List<PedidoEntrega> {
    val numPedido = view.pedidoImpressoComNota
    return PedidoEntrega.listaPedidoImpressoComNota()
      .filter {pedido ->
        pedido.pedido == numPedido || numPedido == 0
      }
  }
  
  private fun listAreas() = PedidoEntrega.listaPedido()
    .map {it.area}
    .distinct()
    .sorted()
  private fun listRotas() = PedidoEntrega.listaPedido()
    .map {it.rota}
    .distinct()
    .sorted()
}

interface IPedidoEntregaView: IView {
  fun updateGridImprimir(itens: List<PedidoEntrega>)
  fun updateGridImpressoSemNota(itens: List<PedidoEntrega>)
  fun updateGridImpressoComNota(itens: List<PedidoEntrega>)
  fun itensSelecionado(): List<PedidoEntrega>
  val pedidoImprimir: Int
  val pedidoImpressoSemNota: Int
  val pedidoImpressoComNota: Int
  val dataImprimir: LocalDate?
  val areaImprimir: String
  val rotaImprimir: String
  fun updateComboAreaImprimir(itens: List<String>)
  fun updateComboRotaImprimir(itens: List<String>)
}