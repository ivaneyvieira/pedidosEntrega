package br.com.astrosoft.framework.view

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import javax.servlet.annotation.WebListener

@WebListener
class Bootstrap : ServletContextListener {
  override fun contextDestroyed(sce: ServletContextEvent?) {
    log?.info("Shutting down")
    log?.info("Destroying VaadinOnKotlin")
    log?.info("Shutdown complete")
  }

  override fun contextInitialized(sce: ServletContextEvent?) {
    log?.info("Starting up")
    Locale.setDefault(Locale("pt", "BR"))
    val home = System.getenv("HOME")
    val fileName = System.getenv("EBEAN_PROPS") ?: "$home/ebean.properties"
    System.setProperty("ebean.props.file", fileName)
    println("##################### $fileName")
  }
}

val log: Logger? = LoggerFactory.getLogger(Bootstrap::class.java)