package ru.hse.shell.model

/*
 * Statement represents an executable command or assignment.
 */
sealed class Statement {
    /*
     * An assignment represents a 'name=value' statement.
     */
    class Assignment(val name: String, val value: String) : Statement()

    /*
     * A RawCommand represents an executable command of the type 'arg[0] agr[1] agr[2]...'.
     */
    class RawCommand(val arguments: List<String>) : Statement()
}
