package io.nokjao.lucid

import org.lwjgl.opengl.GL11.*

class Renderer {
    @Throws(Exception::class)
    fun init() {}

    fun clear() = glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

    fun render(framePos: Double) {}
}

