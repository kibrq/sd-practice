package ru.hse.core.checker

import org.jooq.DSLContext
import ru.hse.core.Tables
import ru.hse.core.tables.records.CheckersRecord

data class CheckerPrototype(val dockerfile: String)

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

    fun getById(id: String): Checker? {
        return dsl.select(Tables.CHECKERS.ID, Tables.CHECKERS.DOCKERFILE)
            .from(Tables.CHECKERS)
            .where(Tables.CHECKERS.ID.eq(id))
            .fetchOne()
            ?.into(Checker::class.java)
    }

}
data class Checker(val id: String, val dockerfile: String)
