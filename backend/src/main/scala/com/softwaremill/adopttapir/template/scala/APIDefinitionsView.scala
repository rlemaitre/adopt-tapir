package com.softwaremill.adopttapir.template.scala

import com.softwaremill.adopttapir.starter.{ServerEffect, StarterDetails}

object APIDefinitionsView {

  def getHelloServerEndpoint(starterDetails: StarterDetails): PlainLogicWithImports = starterDetails.serverEffect match {
    case ServerEffect.FutureEffect => HelloServerEndpoint.future
    case ServerEffect.IOEffect     => HelloServerEndpoint.io
    case ServerEffect.ZIOEffect    => HelloServerEndpoint.zio
  }

  private object HelloServerEndpoint {

    val future: PlainLogicWithImports = PlainLogicWithImports(
      """  val helloServerEndpoint: Full[Unit, Unit, User, Unit, String, Any, Future] = helloEndpoint.serverLogic(user =>
        |    Future.successful {
        |      s"Hello ${user.name}".asRight[Unit]
        |    }
        |  )""".stripMargin,
      List(
        Import("scala.concurrent.ExecutionContext.Implicits.global"),
        Import("scala.concurrent.Future"),
        Import("cats.implicits.catsSyntaxEitherId")
      )
    )

    val io: PlainLogicWithImports = PlainLogicWithImports(
      """  val helloServerEndpoint: Full[Unit, Unit, User, Unit, String, Any, IO] = helloEndpoint.serverLogic(user =>
        |    IO.pure {
        |      s"Hello ${user.name}".asRight[Unit]
        |    }
        |  )
        |""".stripMargin,
      List(
        Import("cats.effect.IO"),
        Import("cats.implicits.catsSyntaxEitherId")
      )
    )

    val zio: PlainLogicWithImports = PlainLogicWithImports(
      """  val helloServerEndpoint: ZServerEndpoint[Any, Any] = helloEndpoint.zServerLogic(user =>
        |    ZIO.succeed {
        |      s"Hello ${user.name}"
        |    }
        |  )
        |""".stripMargin,
      List(
        Import("sttp.tapir.ztapir._"),
        Import("zio.ZIO")
      )
    )
  }
}
