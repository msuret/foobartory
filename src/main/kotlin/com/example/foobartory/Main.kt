package com.example.foobartory

import com.example.foobartory.runner.Runner
import kotlinx.html.div
import kotlinx.html.h1
import space.kscience.plotly.Plotly
import space.kscience.plotly.layout
import space.kscience.plotly.plot
import space.kscience.plotly.server.close
import space.kscience.plotly.server.serve
import space.kscience.plotly.server.show
import kotlin.random.Random

fun main() {
    val metrics = Runner(30).apply { run() }.context.metrics

    val serverPort = Random.nextInt(49152, 65535)
    val server = Plotly.serve(port = serverPort) {

        val pageTitle = "Foobartory metrics"

        page(title = pageTitle) {

            h1 { text(pageTitle) }

            div {
                plot {
                    traces(
                        metrics.totalRobotCount.asTrace(),
                        metrics.workingRobotCount.asTrace(),
                        metrics.movingRobotCount.asTrace(),
                        metrics.waitingRobotCount.asTrace()
                    )
                    layout {
                        title = "Robot repartition per activity"
                        xaxis { title = "time" }
                        yaxis { title = "Robot count" }
                    }
                }
            }

            div {
                plot {
                    traces(*metrics.robotsPerWorkstation.values.map { it.asTrace() }.toTypedArray())
                    layout {
                        title = "Robot repartition per workstation"
                        xaxis { title = "time" }
                        yaxis { title = "Robot count" }
                    }
                }
            }

            div {
                plot {
                    traces(*metrics.stocks.values.map { it.asTrace() }.toTypedArray())
                    layout {
                        title = "Product stocks"
                        xaxis { title = "time" }
                        yaxis { title = "stock" }
                    }
                }
            }

            div {
                plot {
                    traces(metrics.foobarProductionOutcome.asTrace())
                    layout {
                        title = "Foobar production outcome"
                    }
                }
            }
        }

    }

    server.show()

    println("\nYou can have look on some statistics at http://localhost:$serverPort\nPress Enter to exit...")
    readLine()
    println("Shutting down..")
    server.close()
}

