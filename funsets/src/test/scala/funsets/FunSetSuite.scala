package funsets

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  test("string take") {
    val message = "hello, world"
    assert(message.take(5) == "hello")
  }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  test("adding ints") {
    assert(1 + 2 === 3)
  }

  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  test("it should not contain 100") {

    assert(!contains({ x: Int => x > 0 }, -100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
    val s4 = singletonSet(1)
    
    val setPositiveNumbers = union(singletonSet(1), singletonSet(200))
    val setNegativeNumbers = union(singletonSet(-10), singletonSet(-55))
    val setPositiveAndNegativeNumbers = union(setPositiveNumbers, setNegativeNumbers)
    val setEvenNumbers = union(singletonSet(4), singletonSet(6))
    val setOddNumbers = union(singletonSet(3), singletonSet(9))
    val setEvenAndOddNumbers = union(setEvenNumbers, setOddNumbers)
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
    }
  }

  test("union contains all elements") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("Intersection Function") {
    new TestSets {
      val s_ = intersect(s1, s2)
      assert(!contains(s_, 1), "Intersect not 1")
      val s = intersect(s1, s4)
      assert(contains(s, 1), "Intersect  contain 1")

    }
  }

  test("Diff Function") {
    new TestSets {
      val s = diff(s1, s2)
      assert(!contains(s, 2), "Diff not contain 1")
      val s1_ = diff(s1, s4)
      assert(!contains(s1_, 1), "Diff  not contain 1")
      val s2_ = diff(s1, s3)
      assert(contains(s2_, 1), "Diff contain 1")

    }
  }

  test("Filter Function") {
    new TestSets {
      val fs1 = filter(s1, { elem: Int => elem < 2 })
      assert(contains(fs1, 1))

      val fs2 = filter(s3, { elem: Int => elem > 5 })
      assert(!contains(fs1, 3))
    }
  }

  test("forall function") {
    new TestSets {
      assert(forall(setPositiveNumbers, { elem: Int => elem > 0 }))
      assert(forall(setNegativeNumbers, { elem: Int => elem < 0 }))
      assert(!forall(setPositiveAndNegativeNumbers, { elem: Int => elem < 0 }))
      assert(forall(setEvenNumbers, { elem: Int => (elem % 2) == 0 }))
      assert(forall(setOddNumbers, { elem: Int => (elem % 2) != 0 }))
      assert(!forall(setEvenAndOddNumbers, { elem: Int => (elem % 2) == 0 }))
    }
  }

  test("exists function") {
    new TestSets {
      assert(exists(setPositiveAndNegativeNumbers, { elem: Int => elem > 0 }))
      assert(exists(setEvenAndOddNumbers, { elem: Int => (elem % 2) == 0 }))
      assert(exists(setPositiveNumbers, { elem: Int => elem > 0 }))
      assert(exists(setNegativeNumbers, { elem: Int => elem < 0 }))
      assert(exists(setEvenNumbers, { elem: Int => (elem % 2) == 0 }))
      assert(exists(setOddNumbers, { elem: Int => (elem % 2) != 0 }))
     }
  }

  test("map function") {
    new TestSets {
      val mapEvenSetToOdd = map(setEvenNumbers, { elem: Int => elem + 1 })
      printSet(mapEvenSetToOdd)
      assert(contains(mapEvenSetToOdd, 5) && contains(mapEvenSetToOdd, 7))
      val mapOddSetToEven = map(setOddNumbers, { elem: Int => elem * 2 })
      assert(forall(mapOddSetToEven, { elem: Int => (elem % 2) == 0 }))

    }

  }

}
