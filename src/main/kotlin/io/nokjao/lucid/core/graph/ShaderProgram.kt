package io.nokjao.lucid.core.graph

import org.lwjgl.opengl.GL20.*

class ShaderProgram {

    private var programId: Int? = null

    private var vertexShaderId: Int? = null

    private var fragmentShaderId: Int? = null


    init {
        programId = glCreateProgram()

        if (programId == 0) {
            throw Exception("Could not create Shader")
        }
    }

    @Throws(Exception::class)
    fun createVertexShader(shaderCode: String) {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER)
    }

    @Throws(Exception::class)
    fun createFragmentShader(shaderCode: String) {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER)
    }

    @Throws(Exception::class)
    fun createShader(shaderCode: String, shaderType: Int): Int {
        val shaderId = glCreateShader(shaderType)
        if (shaderId == 0) {
            throw Exception("Error creating shader. Type: $shaderType")
        }

        glShaderSource(shaderId, shaderCode)
        glCompileShader(shaderId)

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw Exception("Error compiling Shader code: ${glGetShaderInfoLog(shaderId, 1024)}")
        }

        programId?.let {
            glAttachShader(it, shaderId)
        }

        return shaderId
    }

    @Throws(Exception::class)
    fun link() {
        programId?.also { pid ->
            glLinkProgram(pid)
            if (glGetProgrami(pid, GL_LINK_STATUS) == 0) {
                throw Exception("Error linking Shader code: ${glGetProgramInfoLog(pid, 1024)}")
            }

            vertexShaderId?.let { sid ->
                if (sid != 0) {
                    glDetachShader(pid, sid)
                }
            }

            fragmentShaderId?.let { fid ->
                if (fid != 0) {
                    glDetachShader(pid, fid)
                }
            }
        }

        programId?.also {
            glValidateProgram(it)
            if (glGetProgrami(it, GL_VALIDATE_STATUS) == 0) {
                System.err.println("Warning validating Shader code: ${glGetProgramInfoLog(it, 1024)}")
            }
        }
    }

    fun bind() {
        programId?.let {
            glUseProgram(it)
        }
    }

    fun unbind() {
        glUseProgram(0)
    }

    fun cleanup() {
        unbind()
        programId?.let {
            if (it != 0) {
                glDeleteProgram(it)
            }
        }
    }
}