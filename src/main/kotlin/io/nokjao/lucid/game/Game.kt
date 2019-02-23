package io.nokjao.lucid.game

import io.nokjao.lucid.core.graph.Mesh
import io.nokjao.lucid.core.Window
import io.nokjao.lucid.core.interfaces.*
import org.lwjgl.glfw.GLFW

class Game : IGameLogic {

    private var direction = 0
    private var color = 0.0f
    private val renderer = Renderer()
    private lateinit var mesh: Mesh

    @Throws(Exception::class)
    override fun init() {
        renderer.init()
        val positions = floatArrayOf(
            -0.5f,  0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
             0.5f, -0.5f, 0.0f,
             0.5f,  0.5f, 0.0f
        )
        val indices = intArrayOf(
            0, 1, 3, 3, 1, 2
        )
        val colors = floatArrayOf(
            0.5f, 0.0f, 0.0f,
            0.0f, 0.5f, 0.0f,
            0.0f, 0.0f, 0.5f,
            0.0f, 0.5f, 0.5f
        )
        mesh = Mesh(positions, colors, indices)
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
        window.setClearColor(color, color, color, 0.0f)
        renderer.render(window, mesh)
    }

    override fun cleanup() {
        renderer.cleanup()
    }
}



