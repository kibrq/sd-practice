package ru.hse.hwproj.api.ipc

interface HwprojPublisher {
    fun publishChecker(id: Int)
    fun publishSubmission(id: Int)
}
