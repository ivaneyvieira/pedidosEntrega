package br.com.astrosoft.framework.spring

import com.vaadin.flow.server.ServletHelper.RequestType
import com.vaadin.flow.shared.ApplicationConstants
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import java.util.stream.*
import javax.servlet.http.HttpServletRequest

object SecurityUtils {
  fun isFrameworkInternalRequest(request: HttpServletRequest?): Boolean {
    request ?: return false
    val parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER)
    return parameterValue != null &&
           Stream.of(*RequestType.values())
             .anyMatch {r: RequestType -> r.identifier == parameterValue}
  }
  
  val isUserLoggedIn: Boolean
    get() {
      val authentication =
        SecurityContextHolder.getContext()
          .authentication
      return authentication != null &&
             authentication !is AnonymousAuthenticationToken &&
             authentication.isAuthenticated
    }
}