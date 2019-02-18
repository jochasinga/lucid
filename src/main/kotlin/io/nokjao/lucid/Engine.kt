package io.nokjao.lucid

import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.glClearColor
import java.util.*

class Engine {

    private val tag = "io.nokjao.lucid.Engine"

    private lateinit var window: Window
    private lateinit var thread: Thread
    private lateinit var renderer: Renderer
    private lateinit var world: World
    private lateinit var inputHandler: InputHandler

    private var running = false

    inner class Loop(
        private val fps: Int = 75,
        private val ups: Int = 30
    ) : Runnable {

        private val msPerUpdate = 1.0 / ups

        override fun run() {

            System.out.println("Hello Lucid!")

            init()
            loop()

            window.destroy()
        }

        private fun init() {
            window = Window("Hello World", 300, 300, true)
            window.init()
            renderer = Renderer()
        }


        private fun loop() {
            GL.createCapabilities()
            glClearColor(1.0f, 0.0f, 0.0f, 0.0f)

            window.let {

                var previous = Date().time.toDouble()
                var lag = 0.0

                while (!it.shouldClose()) {

                    val current = Date().time.toDouble()
                    val elapsed = current - previous
                    previous = current
                    lag += elapsed

                    // handleInput()
                    System.out.println("handling output")

                    while (lag >= msPerUpdate) {
                        // update()
                        System.out.println("catching up")
                        lag -= msPerUpdate
                    }

                    renderer.render(lag / msPerUpdate)

                    it.swapBuffers()
                    it.pollEvents()
                }
            }
        }
    }

    fun start() {

        running = true
        thread = Thread(Loop(), tag)

        val os = System.getProperty("os.name")?.also {
            if (it.contains("Mac")) {
                thread.run()
            } else {
                thread.start()
            }
        }

        System.out.println("Starting engine for $os")
    }
}
