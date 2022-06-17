package com.app.myapplication

class PriorityRunner(private val runner: Runnable, val priority: Int) : Runnable {

    override fun run() {
        runner.run()
    }
}
