package ru.hse.hwproj.api.ipc

/*
 * Handles publishing messages to a runner.
 */
interface HwprojPublisher {
    /*
     * Publish a message meaning that a checker's docker image with specified `id` is ready to be built.
     */
    fun publishChecker(id: Int)

    /*
     * Publish a message meaning that a submission with specified `id` is ready to be checked.
     */
    fun publishSubmission(id: Int)
}
