package br.com.astrosoft.framework.spring

import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

internal class CustomRequestCache: HttpSessionRequestCache() {
  override fun saveRequest(request: HttpServletRequest,
                           response: HttpServletResponse) {
    if(!SecurityUtils.isFrameworkInternalRequest(request)) {
      super.saveRequest(request, response)
    }
  }
}