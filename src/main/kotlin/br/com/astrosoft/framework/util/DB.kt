package br.com.astrosoft.framework.util

import java.io.File
import java.io.FileReader
import java.util.*

class DB(banco: String) {
  private val prop = properties()
  val driver = prop?.getProperty("datasource.$banco.databaseDriver") ?: ""
  val url = prop?.getProperty("datasource.$banco.databaseUrl") ?: ""
  val username = prop?.getProperty("datasource.$banco.username") ?: ""
  val password = prop?.getProperty("datasource.$banco.password") ?: ""
  val test = prop?.getProperty("test") == "true"

  companion object {
    private val propertieFile = System.getProperty("ebean.props.file")

    private fun properties(): Properties? {
      val properties = Properties()
      val file = File(propertieFile)

      properties.load(FileReader(file))
      return properties
    }
  }
}