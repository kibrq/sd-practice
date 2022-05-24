package ru.hse.core.checker

import org.jooq.DSLContext
import org.jooq.impl.DefaultDSLContext
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import ru.hse.core.Tables
import ru.hse.core.tables.records.CheckersRecord
import java.util.concurrent.atomic.AtomicLong

object CheckerIdentifierHolder {
    private val currentIdentifier = AtomicLong()

    fun newIdentifier() = "Image${currentIdentifier.incrementAndGet()}"
}
enum class CheckerVerdict(val value: Boolean) {
    YES(true),
    NO(false);

    companion object {
        fun valueOf(value: Boolean): CheckerVerdict {
            return if (value) YES
            else NO
        }
    }
}

data class Checker(
    val dockerfile: String,
    val imageIdentifier: String
)

data class CheckerPrototype(val dockerfile: String)

class CheckerRepository(private val dsl: DefaultDSLContext) {
    fun uploadChecker(prototype: CheckerPrototype): Boolean {
        return dsl.insertInto(Tables.CHECKERS)
            .columns(Tables.CHECKERS.fields().asList())
            .values(CheckersRecord(
                CheckerIdentifierHolder.newIdentifier(),
                prototype.dockerfile
            ))
            .execute().let { it == 0 }
    }

    fun getAllCheckers(): Collection<Checker> {
        return dsl.select(Tables.CHECKERS.ID, Tables.CHECKERS.DOCKERFILE)
            .from(Tables.CHECKERS)
            .fetch()
            .into(Checker::class.java)
    }

    fun getCheckerById(id: String): Checker? {
        return dsl.select(Tables.CHECKERS.ID, Tables.CHECKERS.DOCKERFILE)
            .from(Tables.CHECKERS)
            .where(Tables.CHECKERS.ID.eq(id))
            .fetchOne()
            ?.into(Checker::class.java)
    }

}
