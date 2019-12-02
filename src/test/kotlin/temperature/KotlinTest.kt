package temperature

fun main() {
    fun printDouble(d: Double) {
        print(d)
    }

    val i = 1
    val d = 1.1
    val f = 1.1f

    doStuff(d)


    val numbers = listOf(1, 2, 3, 4)  // LinkedHashSet is the default implementation
    val numbersBackwards = listOf(4, 3, 2, 1)

    println(numbers.first() == numbersBackwards.first())
    println(numbers.first() == numbersBackwards.last())

    println(numbers.average())

    println(numbers.map { i * 3 })
    println(numbers.mapIndexed { idx, value -> value * idx })


    val colors = listOf("red", "brown", "grey")
    val animals = listOf("fox", "bear", "wolf")
    println(colors zip animals)

    val twoAnimals = listOf("fox", "bear")
    println(colors.zip(twoAnimals))

    println(colors.zip(animals) { color, animal -> "The ${animal.capitalize()} is $color" })

    colors.zip(animals) { a, b -> }

//    printDouble(i) // Error: Type mismatch
//    printDouble(f) // Error: Type mismatch


    val numbers1 = listOf("one", "two", "three", "four")
    println(numbers1.associateWith { it.length })


}


fun doStuff(d: Double) {
//    val d = "ada"

    val f = d + d

    println(f)

}


open class Person {
    constructor(firstName: String) { /*...*/
    }
}


class Pope(firstname: String) : Person(firstname) {


}