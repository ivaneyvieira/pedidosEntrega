package br.com.astrosoft.framework.view

import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.provider.SortDirection.ASCENDING
import com.vaadin.flow.data.provider.SortDirection.DESCENDING
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.streams.toList

fun <T: Any> @VaadinDsl Grid<T>.shiftSelect() {
  var pedidoInicial: T? = null
  var pedidoFinal: T? = null
  this.addItemClickListener {evento ->
    val grade = evento.source
    if(evento.isShiftKey) {
      val pedido = evento.item
      if(pedidoInicial == null) {
        pedidoInicial = pedido
        grade.select(pedido)
      }
      else {
        if(pedidoFinal == null) {
          val itens = list(grade)
          pedidoFinal = pedido
          val p1 = itens.indexOf(pedidoInicial!!)
          val p2 = itens.indexOf(pedidoFinal!!) + 1
          val subList = itens.subList(p1.coerceAtMost(p2), p1.coerceAtLeast(p2))
          subList.forEach {
            grade.select(it)
          }
          pedidoFinal = null
          pedidoInicial = null
        }
        else {
          pedidoFinal = null
          pedidoInicial = null
        }
      }
    }
    else {
      pedidoFinal = null
      pedidoInicial = null
    }
  }
}

fun <T: Any> list(grade: Grid<T>): List<T> {
  val dataProvider = grade.dataProvider as ListDataProvider
  val filter = dataProvider.filter
  val queryOrdem = comparator(grade)
  return dataProvider.items.toList()
    .filter {
      filter?.test(it) ?: true
    }
    .let {list ->
      if(queryOrdem == null) list
      else list.sortedWith<T>(queryOrdem)
    }
}

fun <T: Any> comparator(grade: Grid<T>): Comparator<T>? {
  if(grade.sortOrder.isEmpty()) return null
  val sortOrder = grade.sortOrder
  val classGrid = grade.beanType.kotlin
  return comparator(sortOrder, classGrid)
}

fun <T: Any> comparator(sortOrder: List<GridSortOrder<T>>, classGrid: KClass<T>): Comparator<T>? {
  return sortOrder.flatMap {gridSort ->
    val sortOrdem =
      gridSort.sorted.getSortOrder(gridSort.direction)
        .toList()
    val propsBean = classGrid.members.toList()
      .filterIsInstance<KProperty1<T, Comparable<*>>>()
    val props = sortOrdem.mapNotNull {querySortOrder ->
      propsBean.firstOrNull {prop ->
        prop.name == querySortOrder.sorted
      }
    }
    props.map {prop ->
      when(gridSort.direction) {
        DESCENDING -> compareByDescending {
          prop.get(it)
        }
        null, ASCENDING -> compareBy<T> {
          prop.get(it)
        }
      }
    }
  }
    .reduce {acc, comparator ->
      acc.thenComparing(comparator)
    }
}