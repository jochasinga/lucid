package io.nokjao.lucid.interfaces

import io.nokjao.lucid.Window
import java.lang.Exception

interface IGameLogic {
    @Throws(Exception::class)
    fun init()

    fun input(window: Window)

    fun update(interval: Float)

    fun render(window: Window)
}