selftyped
=========

Non-singleton self-types for Scala

Example:

```scala
import ghik.selftyped.SelfTyped

trait Base extends SelfTyped {
  def same[T: Self]: T

  def twice[T: Self]: T
}

trait SimpleSame extends Base {
  // we can always safely return 'this' where self-type is expected
  def same[T: Self]: T = this
}

final case class A(num: Int) extends SimpleSame {
  // A is final, so we can safely return another instance where self-type is expected
  def twice[T: Self]: T = A(num * 2)

  override def same[T: Self]: T = A(num)
}

final case class B(text: String) extends SimpleSame {
  // B is final, so we can safely return another instance as self-type
  def twice[T: Self]: T = B(text * 2)
}

/*
class C extends SimpleSame {
  // ERROR: Got a value of type ghik.selftyped.Main.C where self-type was expected.
  // This is as intended, C is not a final class, so we can't safely return 'new C' where self type is expected.
  def twice[T: Self]: T = new C
}

class D extends SimpleSame {
  // ERROR: Got a value of type Int(42) where self-type was expected.
  // This is as intended, Int is completely unrelated to self-type.
  def twice[T: Self]: T = 42
}
*/

def twice[T <: Base] (t: T): T = t.twice

// everything typechecks, the self-type is properly preserved
println (A (42).same.num)
println (A (42).twice.num)
println (twice (A (42) ).num)
println (B ("fuu").same.text)
println (twice (B ("fuu") ).text)
println (B ("fuu").twice.text)

val a = A (42)
val a1: A = a.twice

// no problem

//ERROR: found A, required a.type - this is as intended, because 'twice' widens the type
//val a2: a.type = a.twice
```
