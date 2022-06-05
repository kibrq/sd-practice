package ru.hse.xcv.model.spells

import org.junit.jupiter.api.Test
import ru.hse.xcv.model.spells.book.HeroSpellBook
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class SpellBookTest {
    @Test
    fun `Test search`() {
        val book = HeroSpellBook()
        val healSpell = HealSpell()
        val fireballSpell = FireballSpell()
        book.addSpell(healSpell)
        book.addSpell(fireballSpell)
        assertEquals(fireballSpell, book.search("J"))
        assertNull(book.search("ZXC"))
    }

    @Test
    fun `Test prefix search`() {
        val book = HeroSpellBook()
        val healSpell = HealSpell()
        val chainLightningSpell = ChainLightningSpell()
        val fireballSpell = FireballSpell()
        book.addSpell(healSpell)
        book.addSpell(chainLightningSpell)
        book.addSpell(fireballSpell)
        assertEquals(0, book.prefixSearch("Q").size)
        val list = book.prefixSearch("H")
        assertEquals(2, list.size)
        assertContains(list, healSpell)
        assertContains(list, chainLightningSpell)
    }
}
