package br.com.astrosoft.pedido.viewmodel.ecommerce

import br.com.astrosoft.framework.viewmodel.exec
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.pedido.model.beans.ETipoPedido.ENTREGA
import br.com.astrosoft.pedido.model.beans.Pedido
import java.time.LocalDate
import java.time.LocalDateTime

class PedidoEcommerceImprimirViewModel(val viewModel : PedidoEcommerceViewModel) {
  private val subView
    get() = viewModel.view.tabEcommerceImprimir
  private fun listPedidosEcommerceImprimir(): List<Pedido> {
    val numPedido = subView.pedidoImprimir
    val data = subView.dataImprimir
    val area = subView.areaImprimir.trim()
    val rota = subView.rotaImprimir.trim()
    return Pedido.listaPedidoImprimir(ENTREGA, ecommerce = true)
      .filter {pedido ->
        (pedido.pedido == numPedido || numPedido == 0) &&
        (pedido.data == data || data == null) &&
        (pedido.rota.contains(rota) || rota == "") &&
        (pedido.area.contains(area) || area == "")
      }
  }
  
  fun updateGridImprimir() {
    subView.updateGrid(listPedidosEcommerceImprimir())
  }
  
  fun imprimirPedidos(pedidos: List<Pedido>) = exec(viewModel.view) {
    printPedidoPdf(pedidos)
    updateGridImprimir()
  }
  
  fun confirmaPrint() {
    val pedidos =
      viewModel.view.tabEcommerceImprimir.itensSelecionado()
        .ifEmpty {fail("Não há pedido selecionado")}
    
    pedidos.forEach {pedido ->
      if(pedido.dataHoraPrint != null)
        pedido.marcaImpresso()
    }
    
    updateGridImprimir()
  }
  
  private fun printPedidoPdf(pedidos: List<Pedido>) {
    viewModel.view.showRelatorioPedido(pedidos)
  }
  
  fun imprimirPedidoMinuta() = exec(viewModel.view) {
    val datetime = LocalDateTime.now()
    val pedidos =
      viewModel.view.tabEcommerceImprimir.itensSelecionado()
        .ifEmpty {fail("Não há pedido selecionado")}
    printPedidoMinutaPdf(pedidos)
    pedidos.forEach {pedido ->
      pedido.marcaDataHora(datetime)
    }
    updateGridImprimir()
  }
  
  private fun printPedidoMinutaPdf(pedidos: List<Pedido>) {
    viewModel.view.showRelatorioPedidoMinuta(pedidos)
  }
}

interface IPedidoEcommerceImprimir {
  fun updateGrid(itens: List<Pedido>)
  fun itensSelecionado(): List<Pedido>
  val pedidoImprimir: Int
  val dataImprimir: LocalDate?
  val areaImprimir: String
  val rotaImprimir: String
}