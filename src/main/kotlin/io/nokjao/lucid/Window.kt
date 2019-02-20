package io.nokjao.lucid

import org.lwjgl.glfw.*
import org.lwjgl.glfw.Callbacks.*
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryStack.*
import java.lang.IllegalStateException


class Window(val title: String, val width: Int,
             val height: Int, var vSync: Boolean) {

    private var windowRef: Long? = null
    var resized = false

    fun init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set()

        if (!glfwInit())
            throw IllegalStateException("Unable to initialize GLFW")

        /** Configure GLFW */

        // Window will become visible after creation
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE)

        windowRef = glfwCreateWindow(width, height, title as CharSequence, 0, 0)
        if (windowRef == null)
            throw RuntimeException("Failed to create the GLFW window")

        windowRef?.let {
            glfwSetKeyCallback(it, fun (window: Long, key: Int, _: Int, action: Int, _: Int) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    glfwSetWindowShouldClose(window, true)
            })
        }

        try {
            stackPush().run {
                val pWidth = mallocInt(1)
                val pHeight = mallocInt(1)

                windowRef?.run {
                    glfwGetWindowSize(this, pWidth, pHeight)
                }

                val videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor())

                windowRef?.apply {
                    glfwSetWindowPos(
                        this,
                        (videoMode?.width() ?: -pWidth.get(0)) / 2,
                        (videoMode?.height() ?: -pHeight.get(0)) / 2
                    )
                }
            }
        } catch (e: Exception) {}

        windowRef?.let {
            glfwMakeContextCurrent(it)
        }

        // Enable v-sync
        if (vSync)
            glfwSwapInterval(1)

        windowRef?.let {
            glfwShowWindow(it)
        }

        GL.createCapabilities()

        // Set clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
    }

    fun setClearColor(r: Float, g: Float, b: Float, alpha: Float) {
        glClearColor(r, g, b, alpha)
    }

    fun isKeyPressed(keyCode: Int): Boolean {
        return windowRef?.let {
            glfwGetKey(it, keyCode) == GLFW_PRESS
        } ?: false
    }

    fun shouldClose() {
        windowRef?.let {
            glfwWindowShouldClose(it)
        }
    }

    fun isResized() = resized

    fun isvSync() = vSync

    fun setvSync(vSync: Boolean) {
        this.vSync = vSync
    }

    fun destroy() {
        windowRef?.let {
            glfwFreeCallbacks(it)
            glfwDestroyWindow(it)
            glfwTerminate()
            glfwSetErrorCallback(null)?.free()
        }
    }

    fun update() {
        glfwSwapBuffers(windowRef!!)
        glfwPollEvents()
    }
}