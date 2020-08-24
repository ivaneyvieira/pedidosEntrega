package br.com.astrosoft.framework.view

import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.horizontalLayout
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.verticalLayout
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.server.InputStreamFactory
import com.vaadin.flow.server.StreamResource
import java.io.ByteArrayInputStream
import java.io.InputStream

class SubWindowPDF(chave: String, bytesBoletos: ByteArray): Dialog() {
  init {
    width = "100%"
    height = "100%"
    val timeNumber = System.currentTimeMillis()
    val resourcePDF = StreamResource("${chave}_${timeNumber}.pdf", ConverteByte(bytesBoletos))
    //val buttonWrapper = FileDownloadWrapper(resourcePDF)
    verticalLayout {
      isPadding = false
      horizontalLayout {
        add(Anchor(resourcePDF, "Download"))
        button("Fechar") {
          icon = VaadinIcon.CLOSE.create()
          onLeftClick {
            close()
          }
        }
      }
      
      addAndExpand(PDFViewer(resourcePDF))
    }
    isCloseOnEsc = true
  }
}

class ConverteByte(val bytesBoletos: ByteArray): InputStreamFactory {
  override fun createInputStream(): InputStream {
    return ByteArrayInputStream(bytesBoletos)
  }
}