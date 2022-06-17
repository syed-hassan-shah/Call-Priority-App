package com.app.myapplication

import java.util.concurrent.ExecutorService

class ExecutorServiceDecorator(private val executorService: ExecutorService) : ExecutorService by executorService {
    override fun execute(command: Runnable?) {
        command?.let { executorService.execute(PriorityRunner(it, 1)) }
    }
}