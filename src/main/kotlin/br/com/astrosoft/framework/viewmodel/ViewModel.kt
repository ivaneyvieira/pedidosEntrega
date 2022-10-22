package br.com.astrosoft.framework.viewmodel

import br.com.astrosoft.framework.view.log

abstract class ViewModel<V : IView>(val view: V) {
  fun exec(block: () -> Unit) = exec(view, block)

  fun showError(msg: String) = view.showError(msg)
  fun showWarning(msg: String) = view.showWarning(msg)
  fun showInformation(msg: String) = view.showInformation(msg)
  fun showReport(chave: String, report: ByteArray) = view.showReport(chave, report)
}

fun exec(view: IView, block: () -> Unit) {
  try {
    block()
  } catch (e: EViewModelFail) {
    view.showError(e.message ?: "Erro generico")
    log?.error(e.toString())
    throw e
  }
}

interface IViewModelUpdate {
  fun updateView()
}

fun fail(message: String): Nothing {
  throw EViewModelFail(message)
}

interface IView {
  fun showError(msg: String)
  fun showWarning(msg: String)
  fun showInformation(msg: String)
  fun showConfirmation(msg: String, execYes: () -> Unit)
  fun showReport(chave: String, report: ByteArray)
}