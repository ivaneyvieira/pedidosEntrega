package br.com.astrosoft.framework.spring

import br.com.astrosoft.pedidoEntrega.model.saci
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class UserSaciDetailsService: UserDetailsService {
  private var userDetails: User? = null
  
  override fun loadUserByUsername(username: String?): UserDetails {
    userDetails = null
    val userSaci = saci.findUser(username) ?: throw UsernameNotFoundException("Usuário inválido")
    userDetails = User(userSaci.login, userSaci.senha, emptyList())
    return userDetails!!
  }
}