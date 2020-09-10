package br.com.astrosoft.pedido.viewmodel

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class DefautlViewModel(view: IDefaultView): ViewModel<IDefaultView>(view)

interface IDefaultView: IView
