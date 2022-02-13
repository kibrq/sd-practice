package ru.hse.shell.model

import ru.hse.shell.util.Environment

/*
 * Interface describing a string, consisting of Variables and plain Strings.
 * It can be evaluated using the Environment.
 */
sealed interface EvalString {
    /*
     * Evaluate EvalString, substituting all variables.
     */
    fun eval(env: Environment): String

    companion object {
        /*
         * Create an EvalString -- a variable with the given name.
         */
        fun ofVariable(name: String): EvalString = Variable(name)

        /*
         * Create an EvalString -- a plain String evaluating to itself.
         */
        fun ofString(value: String): EvalString = JustString(value)

        /*
         * Create an EvalString -- a list of EvalStrings, evaluated as a concatenation with no separator.
         */
        fun ofList(list: List<EvalString>): EvalString = Compose(list)
    }

    private class Variable(private val name: String) : EvalString {
        override fun eval(env: Environment) = env.get(name)
    }

    private class JustString(private val value: String) : EvalString {
        override fun eval(env: Environment) = value
    }

    private class Compose(private val parts: List<EvalString>) : EvalString {
        override fun eval(env: Environment) = parts.joinToString(separator = "") { it.eval(env) }
    }
}
