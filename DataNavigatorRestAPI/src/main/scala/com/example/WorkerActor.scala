package com.example

import akka.actor.{Actor, ActorLogging}

object WorkerActor {
  case class Ok(id: Int)
  case class Create(foo: Foo)
}

class WorkerActor extends Actor with ActorLogging {
  import WorkerActor._

  def receive = {
    case Create(foo) => {
      log.info(s"Create ${foo}")
      sender ! Ok(util.Random.nextInt(10000))
    }
  }
}

