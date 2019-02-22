package io.nokjao.lucid.core.interfaces

import io.nokjao.lucid.core.Window
import java.lang.Exception

interface IGameLogic {
    @Throws(Exception::class)
    fun init()

    fun input(window: Window)

    fun update(interval: Float)

    fun render(window: Window)
}