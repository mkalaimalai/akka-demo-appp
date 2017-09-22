package com.mkalaimalai

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.routing.FromConfig
import com.mkalaimalai.actors._
import com.typesafe.config.ConfigFactory


/**
  * Created by kalaimam on 11/20/16.
  */
object Main extends App {
  val system: ActorSystem = ActorSystem.create("akka-demo", ConfigFactory.load.getConfig("akka-demo"))



}
