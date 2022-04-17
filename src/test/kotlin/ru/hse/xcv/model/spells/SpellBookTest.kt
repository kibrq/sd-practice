package ru.hse.xcv.model.spells

import org.junit.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNull


class SpellBookTest {
    @Test
    fun `test search`() {
        val book = SpellBook()
        val healSpell = HealSpell()
        val fireballSpell = FireballSpell()
        book.addSpell(healSpell)
        book.addSpell(fireballSpell)
        assertEquals(fireballSpell, book.search("J"))
        assertNull(book.search("ZXC"))
    }

    @Test
    fun `test prefix search`() {
        val book = SpellBook()
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
