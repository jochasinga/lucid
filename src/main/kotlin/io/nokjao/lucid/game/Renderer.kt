package io.nokjao.lucid.game

import io.nokjao.lucid.core.Utils
import io.nokjao.lucid.core.Window
import io.nokjao.lucid.core.graph.ShaderProgram
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer


class Renderer {

    private var vaoId: Int? = null

    private var vboId: Int? = null

    private lateinit var shaderProgram: ShaderProgram

    @Throws(Exception::class)
    fun init() {

        shaderProgram = ShaderProgram()
        shaderProgram.apply {
            createVertexShader(Utils.loadResource("/vertex.vs"))
            createFragmentShader(Utils.loadResource("/fragment.fs"))
            link()
        }

        val vertices = floatArrayOf(
            0.0f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f
        )

        var verticesBuffer: FloatBuffer? = null

        try {
            verticesBuffer = MemoryUtil.memAllocFloat(vertices.size)
            verticesBuffer.put(vertices).flip()

            // Create VAO and bind to it
            vaoId = glGenVertexArrays().also {
                glBindVertexArray(it)
            }

            // Create VBO and bind to it
            vboId = glGenBuffers().also {
                glBindBuffer(GL_ARRAY_BUFFER, it)
            }

            verticesBuffer?.let {
                glBufferData(GL_ARRAY_BUFFER, it, GL_STATIC_DRAW)
            }


            glVertexAttribPointer(0, 3, GL_FLOAT,
                            false, 0, 0)

            // Unbind VBO
            glBindBuffer(GL_ARRAY_BUFFER, 0)

            // Unbind VAO
            glBindVertexArray(0)

        } catch (e: Exception) {
            System.err.println(e)
        }finally {
            verticesBuffer?.let {
                MemoryUtil.memFree(it)
            }
        }
    }

    private fun clear() = glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

    fun render(window: Window) {
        clear()

        if (window.isResized()) {
            glViewport(0, 0, window.width, window.height)
            window.resized = false
        }

        shaderProgram.bind()

        vaoId?.let {
            glBindVertexArray(it)
        }

        glEnableVertexAttribArray(0)

        // Draw the vertices
        glDrawArrays(GL11.GL_TRIANGLES, 0, 3)

        // Restore state
        glDisableVertexAttribArray(0)
        glBindVertexArray(0)

        shaderProgram.unbind()
    }

    fun cleanup() {
        shaderProgram.cleanup()

        glDisableVertexAttribArray(0)

        // Delete the VBO
        glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
        vboId?.let {
            glDeleteBuffers(it)
        }

        // Delete the VAO
        glBindVertexArray(0)
        vaoId?.let {
            glDeleteVertexArrays(it)
        }
    }
}

