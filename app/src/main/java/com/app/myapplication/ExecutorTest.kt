package com.app.myapplication

import okhttp3.internal.threadFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

fun getExecutorService(): ExecutorService {
    return ThreadPoolExecutor(
        0, Int.MAX_VALUE, 60, TimeUnit.SECONDS,
        PriorityBlockingQueue<Runnable>(
            11, compareByDescending {
                (it as? PriorityRunnerTest)?.priority
            }), threadFactory("Custom Dispatcher", false)
    )
}

fun main() {
    val executorService = getExecutorService()

    for (i in 0..10) {
        executorService.execute(PriorityRunnerTest("Runner ${i + 1}", 1))
    }
    for (i in 0..10) {
        executorService.execute { println("run: Runner ${i + 1} is running with no priority") }
    }
    executorService.execute(PriorityRunnerTest("Runner21}", 2))
    executorService.execute(PriorityRunnerTest("Runner22", 3))
    executorService.execute(PriorityRunnerTest("Runner23", 2))
    executorService.execute(PriorityRunnerTest("Runner24", 1))
    executorService.execute(PriorityRunnerTest("Runner25", 5))
    executorService.execute(PriorityRunnerTest("Runner26", 10))
    executorService.execute(PriorityRunnerTest("Runner27", 10))
    executorService.execute(PriorityRunnerTest("Runner28", 10))
    executorService.execute(PriorityRunnerTest("Runner29", 1))
    executorService.execute(PriorityRunnerTest("Runner30", 3))
    executorService.shutdown()
}

class PriorityRunnerTest(private val runnerName: String, val priority: Int) : Runnable {

    override fun run() {
        Thread.sleep(200)
        println("run: $runnerName is running with priority $priority")
    }
}