package br.com.astrosoft.framework.util

import org.imgscalr.Scalr
import org.imgscalr.Scalr.Method
import org.imgscalr.Scalr.Mode
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.security.MessageDigest
import javax.imageio.ImageIO

object SystemUtils {
  private val enviroment = System.getenv()
  fun variable(variable: String, def: String): String {
    val envResult = enviroment[variable]
    return if(envResult == null || envResult.trim {it <= ' '} == "") {
      def
    }
    else envResult
  }

  fun resize(imagem: ByteArray?, width: Int, height: Int): ByteArray? {
    return try {
      if(imagem == null) return null
      val bImagemIn = toBufferedImage(imagem) ?: return null
      val bimage = Scalr.resize(bImagemIn, Method.QUALITY, Mode.FIT_TO_WIDTH, width, height)
      toByteArray(bimage)
    } catch(e: IOException) {
      ByteArray(0)
    }
  }

  @Throws(IOException::class)
  private fun toBufferedImage(imagem: ByteArray?): BufferedImage? {
    if(imagem == null) return null
    val inputStream = ByteArrayInputStream(imagem)
    return ImageIO.read(inputStream)
  }

  @Throws(IOException::class)
  private fun toByteArray(image: BufferedImage): ByteArray? {
    val baos = ByteArrayOutputStream()
    ImageIO.write(image, "jpg", baos)
    baos.flush()
    val imageInByte = baos.toByteArray()
    baos.close()
    return imageInByte
  }

  fun getResourceAsStream(name: String?): InputStream? {
    var nameRet = name
    nameRet = resolveName(nameRet)
    val cl = SystemUtils::class.java.classLoader ?: return ClassLoader.getSystemResourceAsStream(nameRet)
    return cl.getResourceAsStream(nameRet)
  }

  private fun resolveName(name: String?): String? {
    var nameRet = name
    if(nameRet == null) {
      return nameRet
    }
    if(!nameRet.startsWith("/")) {
      var c: Class<*> = SystemUtils::class.java
      while(c.isArray) {
        c = c.componentType
      }
      val baseName = c.name
      val index = baseName.lastIndexOf('.')
      if(index != -1) {
        nameRet = baseName.substring(0, index).replace('.', '/') + "/" + nameRet
      }
    }
    else {
      nameRet = nameRet.substring(1)
    }
    return nameRet
  }

  fun readFile(file: String): String? {
    return readFile(file, Charset.defaultCharset())
  }

  @Throws(IOException::class)
  fun readFile(filename: String, encoding: Charset): String {
    val resource = SystemUtils::class.java.getResource(filename)
    val path = Paths.get(resource.toURI())
    val encoded = Files.readAllBytes(path)
    return String(encoded, encoding)
  }

  private fun hashString(type: String, input: String): String {
    val hexChar = "0123456789ABCDEF"
    val bytes = MessageDigest.getInstance(type).digest(input.toByteArray())
    val result = StringBuilder(bytes.size * 2)

    bytes.forEach {
      val i = it.toInt()
      result.append(hexChar[i shr 4 and 0x0f])
      result.append(hexChar[i and 0x0f])
    }

    return result.toString()
  }

  fun md5(text: String): String {
    return hashString("MD5", text)
  }
}


