package ru.hse.shell.model

import ru.hse.shell.util.Environment

sealed interface EvalString {
    fun eval(env: Environment): String

    companion object {
        fun ofVariable(name: String) : EvalString = Variable(name)
        fun ofString(value: String) : EvalString = JustString(value)
        fun ofList(list: List<EvalString>) : EvalString = Compose(list)
    }

    private class Variable(private val name: String): EvalString {
        override fun eval(env: Environment) = env.get(name)
    }

    private class JustString(private val value: String): EvalString {
        override fun eval(env: Environment) = value
    }

    private class Compose(private val parts: List<EvalString>) : EvalString {
        override fun eval(env: Environment) = parts.joinToString(separator = "") { it.eval(env) }
    }
}
