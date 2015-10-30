def foo(): Unit = {
  ;
  var flag: Boolean = false

  // some stuff
  if (bar()) {
    var i: Int = 2 + 4 * 10 + 5
  } else {
    flag = true;
  }
};

def bar(): Boolean = {
  val i: Int = 42

  val snd: Int = (0, 0)._2

  val string: String = "hello" + "world"

  return true && false
}

def bar(foo: Foo): Boolean = {
  val b: Int = 2
  if (2 + b % 2 * 3 + 4 == 1 && true || false) {
    var foo: Int = 42;
    foo = 1
  }

  baz(new Foo(0, 0, (new Bar(), false)))
  return true
}

/*
multiline comment
*/

def power(base: Int, exponent: Int): Int = {
  var b: Int = base
  var e: Int = exponent
  var result: Int = 1
  while (b > 0) {
    if (b % 2 == 1) {
      result = result * b
      e = e - 1
    } else {
      b = b * b
      e = e / 2
    }
  }
  return result
};
;
;

case class Foo(x: Int, y: Int, tuple: (Bar, Boolean))

case class Bar()

def baz(foo: Foo): Unit = {
}

// last line comment