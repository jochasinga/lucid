package io.nokjao.lucid

import org.lwjgl.opengl.GL11.*

class Renderer {
    fun render(framePos: Double) = glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
}

