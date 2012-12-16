package com.github.philcali

import sys.process._
import util.parsing.json.{ JSON, JSONArray, JSONObject }
import tools.jline.console.ConsoleReader
import dispatch._

object Github {
  val api = "https://api.github.com/"

  val reader = new ConsoleReader()

  val response = as.String andThen JSON.parseRaw

  class Command(option: String) {
    val capture = """\{\s*%s\s*\}"""
    val Argument = capture.format("""([^}]+)""").r

    lazy val args = Argument.findAllIn(option).map { arg =>
      Argument.findFirstMatchIn(arg).map(_.group(1)).get
    }.toSeq

    def execute(obj: Map[String, Any])(success: Any => String) = {
      (option /: args) ({
        case (in, key) =>
          val replacement = if (obj contains key) success(obj(key)) else ""
          in.replaceFirst(capture format key, replacement)
      }) !!
    }
  }

  case class Config(
    username: Option[String] = None,
    command: Boolean = false,
    path: Option[String] = None
  )

  val parser = new scopt.immutable.OptionParser[Config]("gpf", "1.0") {
    def options = Seq(
      opt("u", "username", "Github username") { (u, c) => c.copy(username = Some(u)) },
      flag("c", "command", "Execute an OS command") { _.copy(command = true) },
      argOpt("<path>", "Github API path") { (a, c) => c.copy(path = Some(a)) }
    )
  }

  def flatten(js: Any, command: Option[Command], depth: Int = 0): String = js match {
    case JSONArray(elements) => ("" /: elements) (_ + flatten(_, command, depth))
    case JSONObject(obj) =>
      command.map(_.execute(obj)(flatten(_, command, depth + 2))).getOrElse(
        obj.foldLeft("") ({
          case (s, (key, value)) => s + "\n" +
            (0 to depth).map(_ => " ").mkString + key + ": " +
            flatten(value, command, depth + 2)
        })
      )
    case null => ""
    case _ => js.toString()
  }

  def main(args: Array[String]) {
    parser.parse(args, Config()) map { config =>
      val username = config.username.getOrElse(
        reader.readLine("Github Username > ")
      )

      val password = reader.readLine("Github Password > ", '*')

      val path = config.path.map(rest => api + rest.stripPrefix("/")).getOrElse(api)

      Http(url(path).secure.as (username, password) > response).apply() match {
        case Some(json) =>
          println(flatten(json, config.command match {
            case true => Some(new Command(reader.readLine("Execute command > ")))
            case false => None
          }))
        case _ => println("Did not receive JSON response...")
      }
    }
  }
}
