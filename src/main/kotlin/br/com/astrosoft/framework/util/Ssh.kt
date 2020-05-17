package br.com.astrosoft.framework.util

import com.jcraft.jsch.Channel
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import java.util.*

class Ssh(private val host: String, val user: String, private val password: String, private val port: Int = 22) {
  private val config = Properties()
  
  fun shell(exec: Session.() -> Unit) {
    conectSession {session ->
      session.exec()
    }
  }
  
  private fun conectSession(exec: (Session) -> Unit) {
    config["StrictHostKeyChecking"] = "no"
    val jsch = JSch()
    jsch.getSession(user, host, port)
      ?.let {session ->
        session.setPassword(password)
        session.setConfig(config)
        session.connect()
        exec(session)
        session.disconnect()
      }
  }
}

fun Session.execCommand(command: String): String {
  val channelExec = this.openChannel("exec") as? ChannelExec
  return channelExec?.let {channel ->
    channel.setPty(true)
    channel.setCommand(command)
    channel.inputStream = null
    channel.setErrStream(System.err)
    channel.connect()
    val output: String = getOutput(channel)
    println(output)
    channel.disconnect()
    output
  } ?: ""
}

private fun getOutput(channel: Channel): String {
  val inputStream = channel.inputStream
  val tmp = ByteArray(1024)
  val stringBuild = StringBuilder()
  while(true) {
    while(inputStream.available() > 0) {
      val i: Int = inputStream.read(tmp, 0, 1024)
      if(i < 0) break
      stringBuild.append(tmp)
    }
    if(channel.isClosed) {
      println("exit-status: " + channel.exitStatus)
      break
    }
    try {
      Thread.sleep(1000)
    } catch(ee: Exception) {
    }
  }
  return stringBuild.toString()
}


// device for ressu4_bem: smb://ENGECOPI/192.168.4.56/ressu4_bem