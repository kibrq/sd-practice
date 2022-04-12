package ru.hse.xcv.model.stats

class Experience {
    private var experience: Int = 0
        get() = field

    private var level: Int = 1
        get() = field

    fun applyExperience(exp: Int): Int {
        check(exp > 0)
        experience += exp
        val levelIncrease = experience / 100
        if (experience >= 100) {
            level += levelIncrease
            experience %= 100
        }
        return levelIncrease
    }
}
