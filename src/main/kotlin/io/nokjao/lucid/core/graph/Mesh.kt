package io.nokjao.lucid.core.graph

import java.nio.FloatBuffer
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil
import java.nio.IntBuffer

class Mesh(private val positions: FloatArray,
           private val colors: FloatArray,
           private val indices: IntArray) {

    var vaoId: Int? = null
        private set

    var posVboId: Int? = null
        private set

    var idxVboId: Int? = null
        private set

    var colorVboId: Int? = null
        private set

    var vertexCount: Int? = null
        private set

    init {
        var posBuffer: FloatBuffer? = null
        var colorsBuffer: FloatBuffer? = null
        var indicesBuffer: IntBuffer? = null
        try {
            vertexCount = indices.size
            vaoId = glGenVertexArrays().also {
                glBindVertexArray(it)
            }

            // Position VBO
            posVboId = glGenBuffers().also {
                glBindBuffer(GL_ARRAY_BUFFER, it)
            }
            posBuffer = MemoryUtil.memAllocFloat(positions.size).also {
                it.put(positions).flip()
            }.also {
                glBufferData(GL_ARRAY_BUFFER, it, GL_STATIC_DRAW)
            }
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

            // Color VBO
            colorVboId = glGenBuffers().also {
                glBindBuffer(GL_ARRAY_BUFFER, it)
            }
            colorsBuffer = MemoryUtil.memAllocFloat(colors.size).also {
                it.put(colors).flip()
            }.also {
                glBufferData(GL_ARRAY_BUFFER, it, GL_STATIC_DRAW)
            }
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0)

            // Index VBO
            idxVboId = glGenBuffers().also {
                glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, it)
            }
            indicesBuffer = MemoryUtil.memAllocInt(indices.size).also {
                it.put(indices).flip()
            }.also {
                glBufferData(GL_ELEMENT_ARRAY_BUFFER, it, GL_STATIC_DRAW)
            }

            // Unbind VBO
            glBindBuffer(GL_ARRAY_BUFFER, 0)
            // Unbind VAO
            glBindVertexArray(0)
        } catch (e: Exception) {
            System.err.println(e)
        } finally {
            posBuffer?.let {
                MemoryUtil.memFree(it)
            }
            colorsBuffer?.let {
                MemoryUtil.memFree(it)
            }
            indicesBuffer?.let {
                MemoryUtil.memFree(it)
            }
        }
    }

    fun cleanup() {
        glDisableVertexAttribArray(0)

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        posVboId?.let {
            glDeleteBuffers(it)
        }
        colorVboId?.let {
            glDeleteBuffers(it)
        }
        idxVboId?.let {
            glDeleteBuffers(it)
        }

        // Delete the VAO
        glBindVertexArray(0)
        vaoId?.let {
            glDeleteVertexArrays(it)
        }
    }
}