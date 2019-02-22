package io.nokjao.lucid.core.components

import java.util.*

sealed class BaseComponent
data class Component(private var id: String = ""): BaseComponent() {
    init {
        id = UUID.randomUUID().toString()
    }
}