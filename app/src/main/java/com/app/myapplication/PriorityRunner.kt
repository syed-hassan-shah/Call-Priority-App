package com.app.myapplication

import android.util.Log
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PriorityRunner(private val runner: Runnable, val priority: Int) : Runnable {

    var logger: Logger = LoggerFactory.getLogger(PriorityRunner::class.java)
    override fun run() {
        if (logger.isDebugEnabled){
            logger.debug("logger.isDebugEnabled {}", logger.isDebugEnabled)
        }else if (logger.isTraceEnabled){
            println()
            Log.d("TAG", "run: ")
            logger.debug("logger.isTraceEnabled {}", logger.isTraceEnabled)
        }else{
            println()
        }
        Log.d("TAG", "run: ")
        logger.debug("This is a test for proguard {}{}{}", 1, false, 5.9)
        runner.run()
    }
}
