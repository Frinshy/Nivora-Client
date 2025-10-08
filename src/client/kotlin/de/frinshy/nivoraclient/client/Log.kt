package de.frinshy.nivoraclient.client

object Log {
    /** Toggle detailed debug output (false by default in production) */
    var DEBUG = false

    fun d(msg: String) {
        if (DEBUG) System.err.println(msg)
    }

    fun e(msg: String) {
        System.err.println(msg)
    }
}
