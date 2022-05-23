package ru.hse.core.checker

import org.jooq.DSLContext
import org.springframework.stereotype.Component
import ru.hse.core.Tables
import ru.hse.core.tables.records.CheckersRecord

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

@Component
class CheckerRepository(private val dsl: DSLContext) {

    private class Mapper {
        fun toRecord(prototype: CheckerPrototype) = CheckersRecord("STUB", prototype.dockerfile)
    }

    private val mapper = Mapper()

    fun upload(prototype: CheckerPrototype): Boolean {
        return dsl.insertInto(Tables.CHECKERS)
            .columns(Tables.CHECKERS.fields().asList())
            .values(mapper.toRecord(prototype))
            .execute().let { it == 0 }
    }

    fun getAll(): Collection<Checker> {
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
