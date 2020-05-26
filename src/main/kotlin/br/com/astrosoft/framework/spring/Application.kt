package br.com.astrosoft.framework.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

@SpringBootApplication(exclude = [ErrorMvcAutoConfiguration::class])
class Application: SpringBootServletInitializer()

fun main(args: Array<String>) {
  runApplication<Application>(*args)
}

