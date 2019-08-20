package chapter4

import chapter3.listings.Cons
import chapter3.listings.List
import chapter3.listings.Nil
import chapter4.Listing_4_4.Try
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

fun <A, B> traverse(xa: List<A>, f: (A) -> Option<B>): Option<List<B>> =
        when (xa) {
            is Nil -> Some(Nil)
            is Cons ->
                map2(f(xa.head), traverse(xa.tail, f)) { b, xb -> Cons(b, xb) }
        }

class SolutionSpec_4_5 : WordSpec({

    fun <A> sequence(xs: List<Option<A>>): Option<List<A>> = traverse(xs) { it }

    "traverse" should {
        "sequence the successful results of map" {
            val xa = List.of(1, 2, 3, 4, 5)
            traverse(xa) { a: Int -> Try { a.toString() } } shouldBe Some(List.of("1", "2", "3", "4", "5"))
        }

        "sequence results of map with a failure" {
            val xa = List.of("1", "2", "x", "4")
            traverse(xa) { a -> Try { a.toInt() } } shouldBe None
        }
    }

    "sequence" should {
        "turn a list of options into an option of list" {
            val lo = List.of(Some(10), Some(20), Some(30))
            sequence(lo) shouldBe Some(List.of(10, 20, 30))
        }

        "turn a list of options containing a none into a none" {
            val lo = List.of(Some(10), None, Some(30))
            sequence(lo) shouldBe None
        }

        "turn a list of none into a none" {
            sequence(List.of(None)) shouldBe None
        }
    }
})

