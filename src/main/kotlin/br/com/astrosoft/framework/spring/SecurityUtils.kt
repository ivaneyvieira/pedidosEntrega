package br.com.astrosoft.framework.spring

import com.vaadin.flow.server.HandlerHelper.RequestType
import com.vaadin.flow.shared.ApplicationConstants
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import javax.servlet.http.HttpServletRequest

object SecurityUtils {
  fun isFrameworkInternalRequest(request: HttpServletRequest?): Boolean {
    val parameterValue = request?.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER) ?: return false
    return RequestType.values()
      .any {it.identifier == parameterValue}
  }
  
  val isUserLoggedIn: Boolean
    get() {
      val authentication = SecurityContextHolder.getContext().authentication ?: return false
      return authentication.isAuthenticated
    }
  
  val userDetails : UserSaciDetails ?
  get(){
    val authentication = SecurityContextHolder.getContext().authentication ?: return null
    return authentication.principal as? UserSaciDetails
  }
}