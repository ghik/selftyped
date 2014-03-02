package ghik.selftyped

import scala.reflect.macros.{blackbox, whitebox}

object Macros {
  def self_impl[S: c.WeakTypeTag, T](c: whitebox.Context {type PrefixType <: SelfTyped with Singleton}) = {
    import c.universe._

    val ts = weakTypeOf[S]
    q"null: $ts#Self[${ts.widen}]"
  }

  def asSelf_impl[T: c.WeakTypeTag](c: blackbox.Context {type PrefixType <: SelfTyped with Singleton})(any: c.Expr[Any])(s: c.Expr[c.PrefixType#Self[T]]): c.Expr[T] = {
    import c.universe._

    val ptpe = c.prefix.actualType
    val atpe = any.actualType
    val ttpe = weakTypeOf[T]

    if (atpe =:= ptpe || (ptpe.typeSymbol.isFinal && atpe <:< ptpe.widen))
      c.Expr[T](q"$any.asInstanceOf[$ttpe]")
    else {
      c.error(any.tree.pos, s"Got a value of type $atpe where self-type was expected.")
      null
    }
  }
}
