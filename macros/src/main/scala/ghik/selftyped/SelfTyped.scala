package ghik.selftyped

import scala.language.experimental.macros

import SelfTyped._

trait SelfTyped {

  type Self[T] = Widen[this.type, T]

  implicit def asSelf[T](any: Any)(implicit s: Self[T]): T = macro Macros.asSelf_impl[T]
}

object SelfTyped {

  trait Widen[S <: SelfTyped with Singleton, T]

  object Widen {
    implicit def self[S <: SelfTyped with Singleton, T]: Widen[S, T] = macro Macros.self_impl[S, T]
  }

}
