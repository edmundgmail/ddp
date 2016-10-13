package com.ddp.rest

import java.io.OutputStream

import akka.actor.{Actor, ActorRef, ActorSelection}
import akka.util.ByteString
import spray.can.websocket.frame.{BinaryFrame, TextFrame}

/**
  * Created by cloudera on 9/27/16.
  */
case class AkkaActorOutputstream (val actor: ActorSelection) extends OutputStream {
  var buffer : Array[Byte] = new Array[Byte] (4096)
  var index  = 0

  override def write(b:Int): Unit ={
    buffer(index) = b.toByte
    index += 1
    if(index == 4096)
      flush()
  }

  override def flush(): Unit = {
    if(index > 0){
      actor ! PushText(TextFrame(ByteString.fromArray(buffer, 0, index).toString()))
      index = 0
    }
  }

  override def write(b: Array[Byte]): Unit = {
    write(b, 0, b.length)
  }

  override def write(b: Array[Byte], off: Int, len: Int): Unit = {

    flush()

    actor ! PushText( TextFrame(ByteString.fromArray(b, off, len)))

  }

}
