package io.nokjao.lucid.core.entities

import java.util.UUID

sealed class BaseEntity
class Entity: BaseEntity() {
    var id = UUID.randomUUID().toString()
        private set
}