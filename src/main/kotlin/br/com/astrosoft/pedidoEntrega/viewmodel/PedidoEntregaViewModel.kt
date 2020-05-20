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
import java.time.LocalDateTime
import kotlin.concurrent.thread

class PedidoEntregaViewModel(view: IPedidoEntregaView): ViewModel<IPedidoEntregaView>(view) {
  fun imprimir() = exec {
    val pedidos =
      view.itensSelecionadoImprimir()
        .ifEmpty {fail("Não há pedido selecionado")}
    val impressora = UserSaci.userAtual?.impressora ?: fail("O usuário não possui impresseora")
    pedidos.forEach {pedido ->
      if(printPedido(pedido.loja, pedido.pedido, impressora))
        pedido.marcaImpresso()
    }
    view.showInformation("Impressão finalizada")
    updateGridImprimir()
  }
  
  private fun printPedido(storeno: Int, ordno: Int, impressora: String): Boolean {
    return try {
      Ssh("172.20.47.1", "ivaney", "ivaney").shell {
        execCommand("/u/saci/shells/printPedidos.sh $storeno $ordno $impressora")
      }
      true
    } catch(e: Throwable) {
      false
    }
  }
  
  fun updateGridImprimir() {
    updatePedidos()
    view.updateGridImprimir(listPedidosEntregaImprimir())
  }
  
  fun updateGridImpressoComNota() {
    updatePedidos()
    view.updateGridImpressoComNota(listPedidosEntregaImpressoComNota())
  }
  
  fun updateGridImpressoSemNota() {
    updatePedidos()
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
  
  fun desmarcaSemNota() = exec {
    val pedidos =
      view.itensSelecionadoImpressoSemNota()
        .ifEmpty {fail("Não há pedido selecionado")}
    
    pedidos.forEach {pedido ->
      pedido.desmarcaImpresso()
    }
    
    updateGridImpressoSemNota()
  }
  
  fun desmarcaComNota() = exec {
    val pedidos =
      view.itensSelecionadoImpressoComNota()
        .ifEmpty {fail("Não há pedido selecionado")}
    
    pedidos.forEach {pedido ->
      pedido.desmarcaImpresso()
    }
    
    updateGridImpressoComNota()
  }
  
  private fun updatePedidos() {
    PedidoEntrega.update()
  }
}

interface IPedidoEntregaView: IView {
  fun updateGridImprimir(itens: List<PedidoEntrega>)
  fun updateGridImpressoSemNota(itens: List<PedidoEntrega>)
  fun updateGridImpressoComNota(itens: List<PedidoEntrega>)
  fun itensSelecionadoImprimir(): List<PedidoEntrega>
  fun itensSelecionadoImpressoComNota(): List<PedidoEntrega>
  fun itensSelecionadoImpressoSemNota(): List<PedidoEntrega>
  val pedidoImprimir: Int
  val pedidoImpressoSemNota: Int
  val pedidoImpressoComNota: Int
  val dataImprimir: LocalDate?
  val areaImprimir: String
  val rotaImprimir: String
  fun updateComboAreaImprimir(itens: List<String>)
  fun updateComboRotaImprimir(itens: List<String>)
}