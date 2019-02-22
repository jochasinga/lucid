package io.nokjao.lucid.core

import io.nokjao.lucid.core.interfaces.IGameLogic

class Engine(private val game: IGameLogic) {

    companion object {
        @JvmStatic val TARGET_FPS = 75
        @JvmStatic val TARGET_UPS = 30
    }

    @Volatile @JvmField var running = false
    private lateinit var thread: Thread
    private val window = Window(
        "Hello World",
        300, 300, true
    )

    inner class Loop : Runnable {

        private val msPerUpdate = 1.0 / TARGET_UPS
        private var previous = getCurrentTime()
        private var lag = 0.0

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
                while (running && !it.shouldClose()) {
                    val current = getCurrentTime()
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

                    if (!window.isvSync())
                        sync()
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

        private fun sync() {
            val loopSlot = 1f / TARGET_FPS
            val endTime = previous + loopSlot
            while (getCurrentTime() < endTime) {
                try {
                    Thread.sleep(1)
                } catch (ie: InterruptedException) {}
            }
        }

        private fun getCurrentTime(): Double = System.nanoTime() / 1000_000_000.0

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

    fun stop() {
        running = false
    }
}
