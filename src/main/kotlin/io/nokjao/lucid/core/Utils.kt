package io.nokjao.lucid.core

import java.io.InputStream
import java.lang.Exception
import java.util.*

class Utils {
    companion object {
        @JvmStatic
        @Throws(Exception::class)
        fun loadResource(filename: String): String {
            val result: String
            try {
                val infile: InputStream = Class.forName(Utils::class.java.name).getResourceAsStream(filename)
                val scanner = Scanner(infile, "UTF-8")
                result = scanner.useDelimiter("\\A").next()
            } catch (e: Exception) {
                TODO()
            }
            println("result: $result")
            return result
        }
    }
}