package com.example.foobartory.workstation

import com.example.foobartory.Product

object RobotShop : SimpleWorkstation {

    override val name = "Robot shop"
    override val finishedProduct = Product.ROBOT
    override val averageProcessingTime = 0.0
    override val averageIngredientAmount = mapOf(
        Product.EURO to 3.0,
        Product.FOO to 6.0
    )
    override val averageOutputAmount = 1.0
}