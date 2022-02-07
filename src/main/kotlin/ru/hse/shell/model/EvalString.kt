package ru.hse.shell.model


sealed class EvalStringPart(val value: String?) {
    class Variable(val name: String) : EvalStringPart(null)
}

class EvalString (val parts: List<EvalStringPart>){}
