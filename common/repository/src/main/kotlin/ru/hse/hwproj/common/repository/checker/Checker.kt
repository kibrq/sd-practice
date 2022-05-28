package ru.hse.hwproj.common.repository.checker

import org.jooq.impl.DefaultDSLContext
import org.springframework.stereotype.Component
import ru.hse.hwproj.common.repository.Tables
import ru.hse.hwproj.common.repository.utils.withinTry
import java.util.*


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
class CheckerRepositoryImpl(private val dsl: DefaultDSLContext) : CheckerRepository {

    override fun upload(prototype: CheckerPrototype): String? {
        val imageIdentifier = UUID.randomUUID().toString()
        return withinTry {
            dsl.insertInto(Tables.CHECKERS)
                .columns(Tables.CHECKERS.fields().asList())
                .values(imageIdentifier, prototype.dockerfile)
                .returningResult(Tables.CHECKERS.ID)
                .fetchOne().let { it != null }
            imageIdentifier
        }
    }

    override fun getAll(): List<Checker> {
        return dsl.select(Tables.CHECKERS.ID, Tables.CHECKERS.DOCKERFILE)
            .from(Tables.CHECKERS)
            .fetch()
            .into(Checker::class.java)
    }

    override fun getByIds(ids: List<String>): List<Checker> {
        return dsl.select(Tables.CHECKERS.ID, Tables.CHECKERS.DOCKERFILE)
            .from(Tables.CHECKERS)
            .where(Tables.CHECKERS.ID.`in`(ids))
            .fetch()
            .into(Checker::class.java)
    }
}
