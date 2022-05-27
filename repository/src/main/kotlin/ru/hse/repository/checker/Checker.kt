package ru.hse.repository.checker

import org.jooq.impl.DefaultDSLContext
import org.springframework.stereotype.Component
import ru.hse.repository.Tables
import ru.hse.repository.utils.withinTry


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

data class CheckerPrototype(val imageIdentifier: String, val dockerfile: String)

@Component
class CheckerRepository(private val dsl: DefaultDSLContext) {

    fun upload(prototype: CheckerPrototype): Boolean {
        return withinTry {
            dsl.insertInto(Tables.CHECKERS)
                .columns(Tables.CHECKERS.fields().asList())
                .values(prototype.imageIdentifier, prototype.dockerfile)
                .returningResult(Tables.CHECKERS.ID)
                .fetchOne().let { it != null }
        }.let { it != null }
    }

    fun getAll(): Collection<Checker> {
        return dsl.select(Tables.CHECKERS.ID, Tables.CHECKERS.DOCKERFILE)
            .from(Tables.CHECKERS)
            .fetch()
            .into(Checker::class.java)
    }

    fun getById(id: String): Checker? {
        return getByIds(listOf(id)).getOrNull(0)
    }

    fun getByIds(ids: List<String>): List<Checker> {
        return dsl.select(Tables.CHECKERS.ID, Tables.CHECKERS.DOCKERFILE)
            .from(Tables.CHECKERS)
            .where(Tables.CHECKERS.ID.`in`(ids))
            .fetch()
            .into(Checker::class.java)
    }
}
