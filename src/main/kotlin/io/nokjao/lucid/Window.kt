package io.nokjao.lucid

import org.lwjgl.glfw.*
import org.lwjgl.glfw.Callbacks.*
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryStack.*
import java.lang.IllegalStateException


class Window(private val title: String, private val width: Int,
             private val height: Int, private val vSync: Boolean) {

    private var windowRef: Long? = null
    private var resized = false

    fun init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set()

        if (!glfwInit())
            throw IllegalStateException("Unable to initialize GLFW")

        /** Configure GLFW */

        // Window will become visible after creation
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GL_TRUE)
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

                val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor())

                windowRef?.apply {
                    glfwSetWindowPos(
                        this,
                        (vidmode?.width() ?: -pWidth.get(0)) / 2,
                        (vidmode?.height() ?: -pHeight.get(0)) / 2
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
    }

    fun isKeyPressed(keyCode: Int) {
        windowRef?.let {
            glfwGetKey(it, keyCode) == GLFW_PRESS
        }
    }

    fun swapBuffers() {
        windowRef?.let {
            glfwSwapBuffers(it)
        }
    }

    fun shouldClose(): Boolean = glfwWindowShouldClose(windowRef!!)

    fun getTitle(): String = title

    fun destroy() {
        windowRef?.let {
            glfwFreeCallbacks(it)
            glfwDestroyWindow(it)
            glfwTerminate()
            glfwSetErrorCallback(null)?.free()
        }
    }

    fun pollEvents() = glfwPollEvents()
}