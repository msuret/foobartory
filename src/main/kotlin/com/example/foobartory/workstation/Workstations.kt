package com.example.foobartory.workstation

val workstations: List<Workstation<out Any>> = listOf(FooMine, BarMine, FoobarFactory, FoobarPointOfSale, RobotShop)

/**
 * Average amount of its finished product each workstation must produce for one robot to be built
 */
private val averageProductionAmount: Map<Workstation<out Any>, Double> = run {
    val result: MutableMap<Workstation<out Any>, Double> = mutableMapOf(RobotShop to 1.0)
    val remainingWorkstations = workstations.toMutableList().apply { remove(RobotShop) }

    while (remainingWorkstations.size > 0) {
        // find a workstation for which all workstations consuming the finished product have a known average production
        val workstation = remainingWorkstations.find { ws1 ->
            remainingWorkstations.none { ws2 ->
                ws2.averageIngredientAmount.containsKey(
                    ws1.finishedProduct
                )
            }
        } ?: throw IllegalStateException("workstations don't form a DAG")
        //sum all needs for this workstation production
        val averageQuantity = result.entries.sumOf { (dependingWs, dependingWsNeeds) ->
            dependingWs.averageIngredientAmount.getOrDefault(
                workstation.finishedProduct,
                0.0
            ) * dependingWsNeeds / dependingWs.averageOutputAmount
        }
        result[workstation] = averageQuantity
        remainingWorkstations.remove(workstation)
    }
    result.toMap()
}

/**
 * Average robot-second time each workstation needs to build one robot
 */
private val averageProductionTime: Map<Workstation<out Any>, Double> =
    averageProductionAmount.mapValues { (workstation, qty) -> qty * workstation.averageProcessingTime / workstation.averageOutputAmount }

/**
 * Weighting of each workstation in the build process
 */
val workstationWeights: Map<Workstation<out Any>, Double> = run {
    val totalTime = averageProductionTime.values.sum()
    averageProductionTime.mapValues { it.value / totalTime }
}