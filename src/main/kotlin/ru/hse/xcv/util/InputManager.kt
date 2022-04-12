package ru.hse.xcv.util

import org.hexworks.zircon.api.uievent.KeyCode
import java.util.concurrent.ArrayBlockingQueue

typealias InputManager = ArrayBlockingQueue<Pair<Int, KeyCode>>
