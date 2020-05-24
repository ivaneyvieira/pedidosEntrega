package br.com.astrosoft.framework.spring

import br.com.astrosoft.pedidoEntrega.model.saci
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class UserSaciDetailsService: UserDetailsService {
  override fun loadUserByUsername(username: String?): UserDetails {
    val userSaci = saci.findUser(username) ?: throw UsernameNotFoundException("Usuário inválido")
    return User(userSaci.login, userSaci.senha, emptyList())
  }
}