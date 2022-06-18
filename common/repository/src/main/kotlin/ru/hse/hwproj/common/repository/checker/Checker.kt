package ru.hse.hwproj.common.repository.checker

import org.jooq.impl.DefaultDSLContext
import org.springframework.stereotype.Component
import ru.hse.hwproj.common.repository.Sequences
import ru.hse.hwproj.common.repository.Tables
import ru.hse.hwproj.common.repository.utils.withinTry

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

class Checker(
    val id: Int,
    val dockerfile: String
)

data class CheckerPrototype(val dockerfile: String)

/*
 * CheckerRepository that stores checkers in a database via specified DSL context.
 */
@Component
class CheckerRepositoryImpl(private val dsl: DefaultDSLContext) : CheckerRepository {
    override fun upload(prototype: CheckerPrototype): Int? {
        return withinTry {
            dsl.insertInto(Tables.CHECKERS)
                .columns(Tables.CHECKERS.fields().asList())
                .values(
                    Sequences.CHECKER_ID_SEQ.nextval(),
                    prototype.dockerfile
                )
                .returningResult(Tables.CHECKERS.ID)
                .fetchOne()
                ?.value1()
        }
    }

    override fun getAll(): List<Checker> {
        return dsl.select()
            .from(Tables.CHECKERS)
            .fetch()
            .into(Checker::class.java)
    }

    override fun getByIds(ids: List<String>): List<Checker> {
        return dsl.select()
            .from(Tables.CHECKERS)
            .where(Tables.CHECKERS.ID.`in`(ids))
            .fetch()
            .into(Checker::class.java)
    }
}
