package ru.hse.xcv.util

import java.util.concurrent.ArrayBlockingQueue

import org.hexworks.zircon.api.uievent.KeyCode

typealias InputManager = ArrayBlockingQueue<Pair<Int, KeyCode>>
