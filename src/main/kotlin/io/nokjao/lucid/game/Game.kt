package io.nokjao.lucid.game

import io.nokjao.lucid.core.Window
import io.nokjao.lucid.core.interfaces.*
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11.glViewport

class Game : IGameLogic {

    private var direction = 0
    private var color = 0.0f
    private val renderer = Renderer()

    @Throws(Exception::class)
    override fun init() {
        renderer.init()
    }

    override fun input(window: Window) {
        if (window.isKeyPressed(GLFW.GLFW_KEY_UP)) {
            direction = 1
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
            direction = -1
        } else {
            direction = 0
        }
    }

    override fun update(interval: Float) {
        color += direction * 0.01f
        if (color > 1) {
            color = 1.0f
        } else if (color < 0) {
            color = 0.0f
        }
    }

    override fun render(window: Window) {
        if (window.isResized()) {
            glViewport(0, 0, window.width, window.height)
            window.resized = false
        }
        window.setClearColor(color, 0.0f, 0.0f, 0.0f)
        renderer.clear()
    }
}



