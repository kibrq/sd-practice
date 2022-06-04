package ru.hse.xcv.model.stats

const val EXP_IN_LEVEL = 100

/*
 * Hero's experience and level.
 */
class Experience {
    var experience: Int = 0
        private set
    var level: Int = 1
        private set

    /*
     * Add `exp` points of experience. Returns number of level increased.
     */
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
