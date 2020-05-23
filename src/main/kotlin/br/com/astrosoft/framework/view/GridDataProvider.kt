package br.com.astrosoft.framework.view

import com.vaadin.flow.data.provider.CallbackDataProvider
import com.vaadin.flow.data.provider.CallbackDataProvider.CountCallback
import com.vaadin.flow.data.provider.CallbackDataProvider.FetchCallback
import com.vaadin.flow.data.provider.DataProvider
import com.vaadin.flow.data.provider.DataProviderListener
import com.vaadin.flow.data.provider.Query
import com.vaadin.flow.shared.Registration
import java.util.stream.*

class GridDataProvider<T>: DataProvider<T, Void> {
  val items = mutableListOf<T>()
  val fetchCallback = GridFetchCallback(items)
  val countCallback = GridCountCallback(items)
  private val backend: CallbackDataProvider<T, Void> = DataProvider.fromCallbacks(fetchCallback, countCallback)
  override fun fetch(query: Query<T, Void>?): Stream<T> {
    return backend.fetch(query)
  }
  
  override fun size(query: Query<T, Void>?): Int {
    return backend.size(query)
  }
  
  override fun addDataProviderListener(listener: DataProviderListener<T>?): Registration {
    return backend.addDataProviderListener(listener)
  }
  
  override fun refreshItem(item: T) {
    backend.refreshItem(item)
  }
  
  override fun refreshAll() {
    backend.refreshAll()
  }
  
  override fun isInMemory(): Boolean {
    return backend.isInMemory
  }
}

class GridFetchCallback<T>(private val list: List<T>): FetchCallback<T, Void> {
  override fun fetch(query: Query<T, Void>?): Stream<T> {
    query ?: return list.stream()
    val sortQuery = query.inMemorySorting ?: return list.stream()
    return list.sortedWith(sortQuery).stream()
  }
}

class GridCountCallback<T>(private val list: List<T>): CountCallback<T, Void> {
  override fun count(query: Query<T, Void>?): Int {
    return list.size
  }
}