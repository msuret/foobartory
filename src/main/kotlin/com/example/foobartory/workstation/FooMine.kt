package com.example.foobartory.workstation

import com.example.foobartory.Product

object FooMine : SimpleWorkstation {
    override val name = "Foo Mine"
    override val finishedProduct = Product.FOO
    override val averageProcessingTime = 1.0
    override val averageIngredientAmount = emptyMap<Product, Double>()
    override val averageOutputAmount: Double = 1.0
}