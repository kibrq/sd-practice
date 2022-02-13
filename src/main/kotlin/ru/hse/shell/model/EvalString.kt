package ru.hse.shell.model

import ru.hse.shell.util.Environment

// Interface describing EvalString
// String consisting of Variables and usual Strings
// It can be evaluated using Environment
sealed interface EvalString {

    // Evaluate EvalString
    // Substitute all variables
    fun eval(env: Environment): String

    companion object {
        fun ofVariable(name: String) : EvalString = Variable(name)
        fun ofString(value: String) : EvalString = JustString(value)
        fun ofList(list: List<EvalString>) : EvalString = Compose(list)
    }

    // class describing Variable
    private class Variable(private val name: String): EvalString {
        override fun eval(env: Environment) = env.get(name)
    }

    // class describing usual String
    private class JustString(private val value: String): EvalString {
        override fun eval(env: Environment) = value
    }

    // class describing union of smaller EvalString_s
    private class Compose(private val parts: List<EvalString>) : EvalString {
        override fun eval(env: Environment) = parts.joinToString(separator = "") { it.eval(env) }
    }
}
