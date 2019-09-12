package ru.victorpomidor.sitemapgenerator.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class Log {
    val log: Logger = LoggerFactory.getLogger(javaClass)
}

fun Log.timed(block: () -> Unit): TimedResult {
    val start = System.currentTimeMillis()
    block.invoke()
    val end = System.currentTimeMillis()
    return TimedResult(end - start, log)
}

class TimedResult(private val time: Long, private val log: Logger) {
    fun log(message: String) = log.info(message, time)
}
