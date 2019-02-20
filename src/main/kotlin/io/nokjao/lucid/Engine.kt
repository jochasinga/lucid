package io.nokjao.lucid

import io.nokjao.lucid.interfaces.IGameLogic
import java.util.*

class Engine(private val game: IGameLogic) {

    companion object {
        @JvmStatic val TARGET_FPS = 75
        @JvmStatic val TARGET_UPS = 30
    }

    @JvmField var running = false
    private lateinit var thread: Thread
    private val window = Window("Hello World",
                            300, 300, true)

    inner class Loop : Runnable {

        private val msPerUpdate = 1.0 / TARGET_UPS

        override fun run() {
            println("Hello Lucid!")
            try {
                init()
                loop()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                destroy()
            }
        }

        @Throws(Exception::class)
        private fun init() {
            window.init()
            game.init()
        }

        private fun loop() {
            window.let {
                var previous = Date().time.toDouble()
                var lag = 0.0
                while (!it.shouldClose()) {
                    val current = Date().time.toDouble()
                    val elapsed = current - previous
                    previous = current
                    lag += elapsed

                    input()

                    while (lag >= msPerUpdate) {
                        update((lag / msPerUpdate).toFloat())
                        println("catching up")
                        lag -= msPerUpdate
                    }

                    render()
                }
            }
        }

        private fun input() {
            game.input(window)
        }

        private fun update(interval: Float) {
            game.update(interval)
        }

        private fun render() {
            game.render(window)
            window.update()
        }

        private fun destroy() {
            window.destroy()
        }
    }

    fun start() {
        running = true
        thread = Thread(Loop(), "Loop")

        System.getProperty("os.name")?.also {

            println("Starting engine for $it")

            when(it.contains("Mac")) {
                true -> thread.run()
                false -> thread.start()
            }
        }
    }
}
