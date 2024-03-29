package br.com.astrosoft.pedido.viewmodel

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.pedido.model.beans.Impressora
import br.com.astrosoft.pedido.model.beans.UserSaci

class UsuarioViewModel(view: IUsuarioView) : ViewModel<IUsuarioView>(view) {
  fun findAll(): List<UserSaci>? {
    return UserSaci.findAll()
  }

  fun add(user: UserSaci): UserSaci? {
    exec {
      user.ativo = true
      validaUser(user)
      UserSaci.updateUser(user)
    }
    return user
  }

  private fun validaUser(user: UserSaci?): UserSaci {
    UserSaci.findUser(user?.login) ?: fail("Usuário não encontrado no saci")
    return user ?: fail("Usuário não selecionado")
  }

  fun update(user: UserSaci?): UserSaci? {
    exec {
      UserSaci.updateUser(validaUser(user))
    }
    return user
  }

  fun delete(user: UserSaci?) {
    exec {
      val userValid = validaUser(user)
      userValid.ativo = false
      UserSaci.updateUser(userValid)
    }
  }

  fun findImpressoras() = Impressora.findAll()
}

interface IUsuarioView : IView