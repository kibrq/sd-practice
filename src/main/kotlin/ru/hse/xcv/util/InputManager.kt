package ru.hse.xcv.util

import org.hexworks.zircon.api.uievent.KeyCode
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.LinkedBlockingQueue

typealias InputManager = LinkedBlockingQueue<KeyCode>
