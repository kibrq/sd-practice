package ru.hse.xcv.model.stats

class Experience {
    private var experience: Int = 0
        get() = field

    private var level: Int = 1
        get() = field

    fun applyExperience(exp: Int) {
        check(exp > 0)
        experience += exp
        if (experience >= 100) {
            level += experience / 100
            experience %=100
        }
    }
}
