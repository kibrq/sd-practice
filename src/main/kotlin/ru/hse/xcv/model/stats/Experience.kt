package ru.hse.xcv.model.stats

const val EXP_IN_LEVEL = 100

class Experience {
    private var experience: Int = 0
    private var level: Int = 1

    fun applyExperience(exp: Int): Int {
        require(exp >= 0)
        experience += exp
        val levelIncrease = experience / EXP_IN_LEVEL
        if (experience >= EXP_IN_LEVEL) {
            level += levelIncrease
            experience %= EXP_IN_LEVEL
        }
        return levelIncrease
    }
}
