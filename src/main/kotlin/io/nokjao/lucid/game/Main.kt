package io.nokjao.lucid.game

import io.nokjao.lucid.core.Engine

class Main

fun main() {
    try {
        // val vSync = true
        val gameLogic = Game()
        val gameEng = Engine(gameLogic)
        gameEng.start()
    } catch (e: Exception) {
        e.printStackTrace()
        System.exit(-1)
    }
}