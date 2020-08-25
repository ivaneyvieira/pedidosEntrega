package br.com.astrosoft.pedidoEntrega.viewmodel

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.util.Ssh
import br.com.astrosoft.framework.util.execCommand
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.pedidoEntrega.model.QuerySaci
import br.com.astrosoft.pedidoEntrega.model.beans.PedidoEntrega
import java.time.LocalDate
import java.time.LocalDateTime

class PedidoEntregaViewModel(view: IPedidoEntregaView): ViewModel<IPedidoEntregaView>(view) {
  fun imprimirPedidoMinuta() = exec {
    val datetime = LocalDateTime.now()
    val pedidos =
      view.itensSelecionadoImprimir()
        .ifEmpty {fail("Não há pedido selecionado")}
    printPedidoMinutaPdf(pedidos)
    pedidos.forEach {pedido ->
      pedido.marcaDataHora(datetime)
    }
    updateGridImprimir()
  }
  
  fun imprimirPedidos(pedidos: List<PedidoEntrega>) = exec {
    printPedidoPdf(pedidos)
    updateGridImprimir()
  }
  
  
  private fun printPedido(pedido: PedidoEntrega, impressora: String): Boolean {
    val storeno = pedido.loja
    val ordno = pedido.pedido
    return try {
      if(!QuerySaci.test) {
        printSaci(storeno, ordno, impressora)
      }
      
      println("/u/saci/shells/printPedidos.sh $storeno $ordno $impressora")
      true
    } catch(e: Throwable) {
      e.printStackTrace()
      false
    }
  }
  
  private fun printPedidoPdf(pedidos: List<PedidoEntrega>) {
    view.showRelatorioPedido(pedidos)
  }
  
  private fun printPedidoMinutaPdf(pedidos: List<PedidoEntrega>) {
    view.showRelatorioPedidoMinuta(pedidos)
  }
  
  private fun printSaci(storeno: Int, ordno: Int, impressora: String) {
    Ssh("172.20.47.1", "ivaney", "ivaney").shell {
      execCommand("/u/saci/shells/printPedidos.sh $storeno $ordno $impressora")
    }
  }
  
  fun updateGridImprimir() {
    view.updateGridImprimir(listPedidosEntregaImprimir())
  }
  
  fun updateGridPendente() {
    view.updateGridPendente(listPedidosEntregaPendente())
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
  
  private fun listPedidosEntregaPendente(): List<PedidoEntrega> {
    val numPedido = view.pedidoPendente
    val data = view.dataPendente
    val area = view.areaPendente.trim()
    val rota = view.rotaPendente.trim()
    return PedidoEntrega.listaPedidoPendente()
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
  
  fun confirmaPrint() {
    val pedidos =
      view.itensSelecionadoImprimir()
        .ifEmpty {fail("Não há pedido selecionado")}
    
    pedidos.forEach {pedido ->
      if(pedido.dataHoraPrint != null)
        pedido.marcaImpresso()
    }
    
    updateGridImprimir()
  }
}

interface IPedidoEntregaView: IView {
  fun updateGridImprimir(itens: List<PedidoEntrega>)
  fun updateGridImpressoSemNota(itens: List<PedidoEntrega>)
  fun updateGridImpressoComNota(itens: List<PedidoEntrega>)
  fun updateGridPendente(itens: List<PedidoEntrega>)
  
  fun itensSelecionadoImprimir(): List<PedidoEntrega>
  fun itensSelecionadoImpressoComNota(): List<PedidoEntrega>
  fun itensSelecionadoImpressoSemNota(): List<PedidoEntrega>
  
  val pedidoImpressoSemNota: Int
  val pedidoImpressoComNota: Int
  
  //
  val pedidoImprimir: Int
  val dataImprimir: LocalDate?
  val areaImprimir: String
  val rotaImprimir: String
  
  //
  val pedidoPendente: Int
  val dataPendente: LocalDate?
  val areaPendente: String
  val rotaPendente: String
  
  //
  fun showRelatorioPedidoMinuta(pedidos: List<PedidoEntrega>)

  fun showRelatorioPedido(pedidos: List<PedidoEntrega>)
}