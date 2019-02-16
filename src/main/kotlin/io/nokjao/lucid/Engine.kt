package io.nokjao.lucid

import java.util.*


class Engine {

    private val tag = "io.nokjao.lucid.Engine"
    private var thread: Thread? = null
    private var renderer: Renderer? = null

    var running = false

    inner class Loop(private val fps: Double = 60.0,
                     private val ups: Double = 30.0) : Runnable {

        private val tag = "io.nokjao.lucid.Loop"

        private val msPerUpdate = 1.0 / ups

        override fun run() {
            var previous = Date().time.toDouble()
            var lag = 0.0

            while (running) {
                val current = Date().time.toDouble()
                val elapsed = current - previous
                previous = current
                lag += elapsed

                handleInput()

                while (lag >= msPerUpdate) {
                    update()
                    lag -= msPerUpdate
                }

                renderer?.render(lag / msPerUpdate)
            }
        }


        fun update() {
            // World logic
            TODO()
        }

        fun handleInput() {
            TODO()
        }
    }

    fun start() {
        running = true
        thread = Thread(Loop(), tag)
        val os = System.getProperty("os.name")?.let {
            if (it.contains("Mac")) {
                thread?.run()
            } else {
                thread?.start()
            }
        }

        System.out.println("Starting engine for $os")
    }

    fun stop() {
        running = false
    }
}