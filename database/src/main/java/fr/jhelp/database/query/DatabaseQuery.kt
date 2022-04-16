/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database.query

import fr.jhelp.database.AndCreation
import fr.jhelp.database.DatabaseNoSQL
import fr.jhelp.database.DatabaseObject
import fr.jhelp.database.NotCreation
import fr.jhelp.database.OrCreation
import fr.jhelp.database.PropertyCondition
import fr.jhelp.database.Query
import fr.jhelp.database.query.condition.DatabaseCondition
import fr.jhelp.database.query.condition.NotCondition
import fr.jhelp.database.query.condition.andConditions
import fr.jhelp.database.query.condition.booleans.BooleanEquals
import fr.jhelp.database.query.condition.doubles.DoubleBetween
import fr.jhelp.database.query.condition.doubles.DoubleEquals
import fr.jhelp.database.query.condition.doubles.DoubleInside
import fr.jhelp.database.query.condition.doubles.DoubleLower
import fr.jhelp.database.query.condition.doubles.DoubleLowerEquals
import fr.jhelp.database.query.condition.doubles.DoubleNotBetween
import fr.jhelp.database.query.condition.doubles.DoubleNotEquals
import fr.jhelp.database.query.condition.doubles.DoubleOutside
import fr.jhelp.database.query.condition.doubles.DoubleUpper
import fr.jhelp.database.query.condition.doubles.DoubleUpperEquals
import fr.jhelp.database.query.condition.enums.EnumEquals
import fr.jhelp.database.query.condition.enums.EnumInside
import fr.jhelp.database.query.condition.enums.EnumNotEquals
import fr.jhelp.database.query.condition.enums.EnumOutside
import fr.jhelp.database.query.condition.floats.FloatBetween
import fr.jhelp.database.query.condition.floats.FloatEquals
import fr.jhelp.database.query.condition.floats.FloatInside
import fr.jhelp.database.query.condition.floats.FloatLower
import fr.jhelp.database.query.condition.floats.FloatLowerEquals
import fr.jhelp.database.query.condition.floats.FloatNotBetween
import fr.jhelp.database.query.condition.floats.FloatNotEquals
import fr.jhelp.database.query.condition.floats.FloatOutside
import fr.jhelp.database.query.condition.floats.FloatUpper
import fr.jhelp.database.query.condition.floats.FloatUpperEquals
import fr.jhelp.database.query.condition.ints.IntBetween
import fr.jhelp.database.query.condition.ints.IntEquals
import fr.jhelp.database.query.condition.ints.IntInside
import fr.jhelp.database.query.condition.ints.IntLower
import fr.jhelp.database.query.condition.ints.IntLowerEquals
import fr.jhelp.database.query.condition.ints.IntNotBetween
import fr.jhelp.database.query.condition.ints.IntNotEquals
import fr.jhelp.database.query.condition.ints.IntOutside
import fr.jhelp.database.query.condition.ints.IntUpper
import fr.jhelp.database.query.condition.ints.IntUpperEquals
import fr.jhelp.database.query.condition.longs.LongBetween
import fr.jhelp.database.query.condition.longs.LongEquals
import fr.jhelp.database.query.condition.longs.LongInside
import fr.jhelp.database.query.condition.longs.LongLower
import fr.jhelp.database.query.condition.longs.LongLowerEquals
import fr.jhelp.database.query.condition.longs.LongNotBetween
import fr.jhelp.database.query.condition.longs.LongNotEquals
import fr.jhelp.database.query.condition.longs.LongOutside
import fr.jhelp.database.query.condition.longs.LongUpper
import fr.jhelp.database.query.condition.longs.LongUpperEquals
import fr.jhelp.database.query.condition.objects.ObjectEquals
import fr.jhelp.database.query.condition.objects.ObjectInside
import fr.jhelp.database.query.condition.objects.ObjectNotEquals
import fr.jhelp.database.query.condition.objects.ObjectOutside
import fr.jhelp.database.query.condition.objects.ObjectSelect
import fr.jhelp.database.query.condition.objects.ObjectSelectAll
import fr.jhelp.database.query.condition.strings.StringBetween
import fr.jhelp.database.query.condition.strings.StringEquals
import fr.jhelp.database.query.condition.strings.StringInside
import fr.jhelp.database.query.condition.strings.StringLower
import fr.jhelp.database.query.condition.strings.StringLowerEquals
import fr.jhelp.database.query.condition.strings.StringNotBetween
import fr.jhelp.database.query.condition.strings.StringNotEquals
import fr.jhelp.database.query.condition.strings.StringOutside
import fr.jhelp.database.query.condition.strings.StringUpper
import fr.jhelp.database.query.condition.strings.StringUpperEquals
import kotlin.reflect.KProperty1

@Query
class DatabaseQuery<DO : DatabaseObject>(private val objectClass: Class<DO>)
{
    private val conditions = ArrayList<DatabaseCondition>()

    fun query(): String
    {
        val stringBuilder = StringBuilder()
        val needParenthesis = this.conditions.size > 1
        var afterFirst = false

        stringBuilder.append("SELECT ")
        stringBuilder.append(DatabaseNoSQL.COLUMN_ID)
        stringBuilder.append(" FROM ")
        stringBuilder.append(DatabaseNoSQL.TABLE_OBJECTS)
        stringBuilder.append(" WHERE ")
        stringBuilder.append(DatabaseNoSQL.COLUMN_CLASS_NAME)
        stringBuilder.append("='")
        stringBuilder.append(this.objectClass.name)
        stringBuilder.append("'")

        if (this.conditions.isEmpty())
        {
            return stringBuilder.toString()
        }

        stringBuilder.append(" AND ")
        stringBuilder.append(DatabaseNoSQL.COLUMN_ID)
        stringBuilder.append(" IN (")

        if (needParenthesis)
        {
            stringBuilder.append("(")
        }

        for (condition in this.conditions)
        {
            if (afterFirst)
            {
                stringBuilder.append(") AND (")
            }

            stringBuilder.append("SELECT ")
            stringBuilder.append(DatabaseNoSQL.COLUMN_OBJECT_ID)
            stringBuilder.append(" FROM ")
            stringBuilder.append(DatabaseNoSQL.TABLE_FIELDS)
            stringBuilder.append(" WHERE ")
            condition.where(stringBuilder)
            afterFirst = true
        }

        if (needParenthesis)
        {
            stringBuilder.append(")")
        }

        stringBuilder.append(")")
        return stringBuilder.toString()
    }

    // Boolean's property

    @PropertyCondition
    val KProperty1<DO, Boolean>.isTrue: Unit
        get()
        {
            this@DatabaseQuery.conditions.add(BooleanEquals<DO>(this, true))
        }

    @PropertyCondition
    val KProperty1<DO, Boolean>.isFalse: Unit
        get()
        {
            this@DatabaseQuery.conditions.add(BooleanEquals<DO>(this, false))
        }

    // Int's property

    @PropertyCondition
    infix fun KProperty1<DO, Int>.LOWER(value: Int)
    {
        this@DatabaseQuery.conditions.add(IntLower<DO>(this, value))
    }

    @PropertyCondition
    infix fun KProperty1<DO, Int>.LOWER_EQUALS(value: Int)
    {
        this@DatabaseQuery.conditions.add(IntLowerEquals<DO>(this, value))
    }

    @PropertyCondition
    infix fun KProperty1<DO, Int>.EQUALS(value: Int)
    {
        this@DatabaseQuery.conditions.add(IntEquals<DO>(this, value))
    }

    @PropertyCondition
    infix fun KProperty1<DO, Int>.NOT_EQUALS(value: Int)
    {
        this@DatabaseQuery.conditions.add(IntNotEquals<DO>(this, value))
    }

    @PropertyCondition
    infix fun KProperty1<DO, Int>.UPPER_EQUALS(value: Int)
    {
        this@DatabaseQuery.conditions.add(IntUpperEquals<DO>(this, value))
    }

    @PropertyCondition
    infix fun KProperty1<DO, Int>.UPPER(value: Int)
    {
        this@DatabaseQuery.conditions.add(IntUpper<DO>(this, value))
    }

    @PropertyCondition
    fun KProperty1<DO, Int>.BETWEEN(value1: Int, value2: Int)
    {
        this@DatabaseQuery.conditions.add(IntBetween<DO>(this, value1, value2))
    }

    @PropertyCondition
    fun KProperty1<DO, Int>.NOT_BETWEEN(value1: Int, value2: Int)
    {
        this@DatabaseQuery.conditions.add(IntNotBetween<DO>(this, value1, value2))
    }

    @PropertyCondition
    fun KProperty1<DO, Int>.ONE_OF(vararg values: Int)
    {
        this@DatabaseQuery.conditions.add(IntInside<DO>(this, values))
    }

    @PropertyCondition
    fun KProperty1<DO, Int>.NONE_OF(vararg values: Int)
    {
        this@DatabaseQuery.conditions.add(IntOutside<DO>(this, values))
    }

    // Long's property

    @PropertyCondition
    infix fun KProperty1<DO, Long>.LOWER(value: Long)
    {
        this@DatabaseQuery.conditions.add(LongLower<DO>(this, value))
    }

    @PropertyCondition
    infix fun KProperty1<DO, Long>.LOWER_EQUALS(value: Long)
    {
        this@DatabaseQuery.conditions.add(LongLowerEquals<DO>(this, value))
    }

    @PropertyCondition
    infix fun KProperty1<DO, Long>.EQUALS(value: Long)
    {
        this@DatabaseQuery.conditions.add(LongEquals<DO>(this, value))
    }

    @PropertyCondition
    infix fun KProperty1<DO, Long>.NOT_EQUALS(value: Long)
    {
        this@DatabaseQuery.conditions.add(LongNotEquals<DO>(this, value))
    }

    @PropertyCondition
    infix fun KProperty1<DO, Long>.UPPER_EQUALS(value: Long)
    {
        this@DatabaseQuery.conditions.add(LongUpperEquals<DO>(this, value))
    }

    @PropertyCondition
    infix fun KProperty1<DO, Long>.UPPER(value: Long)
    {
        this@DatabaseQuery.conditions.add(LongUpper<DO>(this, value))
    }

    @PropertyCondition
    fun KProperty1<DO, Long>.BETWEEN(value1: Long, value2: Long)
    {
        this@DatabaseQuery.conditions.add(LongBetween<DO>(this, value1, value2))
    }

    @PropertyCondition
    fun KProperty1<DO, Long>.NOT_BETWEEN(value1: Long, value2: Long)
    {
        this@DatabaseQuery.conditions.add(LongNotBetween<DO>(this, value1, value2))
    }

    @PropertyCondition
    fun KProperty1<DO, Long>.ONE_OF(vararg values: Long)
    {
        this@DatabaseQuery.conditions.add(LongInside<DO>(this, values))
    }

    @PropertyCondition
    fun KProperty1<DO, Long>.NONE_OF(vararg values: Long)
    {
        this@DatabaseQuery.conditions.add(LongOutside<DO>(this, values))
    }

    // Float's property

    @PropertyCondition
    infix fun KProperty1<DO, Float>.LOWER(value: Float)
    {
        this@DatabaseQuery.conditions.add(FloatLower<DO>(this, value))
    }

    @PropertyCondition
    infix fun KProperty1<DO, Float>.LOWER_EQUALS(value: Float)
    {
        this@DatabaseQuery.conditions.add(FloatLowerEquals<DO>(this, value))
    }

    @PropertyCondition
    infix fun KProperty1<DO, Float>.EQUALS(value: Float)
    {
        this@DatabaseQuery.conditions.add(FloatEquals<DO>(this, value))
    }

    @PropertyCondition
    infix fun KProperty1<DO, Float>.NOT_EQUALS(value: Float)
    {
        this@DatabaseQuery.conditions.add(FloatNotEquals<DO>(this, value))
    }

    @PropertyCondition
    infix fun KProperty1<DO, Float>.UPPER_EQUALS(value: Float)
    {
        this@DatabaseQuery.conditions.add(FloatUpperEquals<DO>(this, value))
    }

    @PropertyCondition
    infix fun KProperty1<DO, Float>.UPPER(value: Float)
    {
        this@DatabaseQuery.conditions.add(FloatUpper<DO>(this, value))
    }

    @PropertyCondition
    fun KProperty1<DO, Float>.BETWEEN(value1: Float, value2: Float)
    {
        this@DatabaseQuery.conditions.add(FloatBetween<DO>(this, value1, value2))
    }

    @PropertyCondition
    fun KProperty1<DO, Float>.NOT_BETWEEN(value1: Float, value2: Float)
    {
        this@DatabaseQuery.conditions.add(FloatNotBetween<DO>(this, value1, value2))
    }

    @PropertyCondition
    fun KProperty1<DO, Float>.ONE_OF(vararg values: Float)
    {
        this@DatabaseQuery.conditions.add(FloatInside<DO>(this, values))
    }

    @PropertyCondition
    fun KProperty1<DO, Float>.NONE_OF(vararg values: Float)
    {
        this@DatabaseQuery.conditions.add(FloatOutside<DO>(this, values))
    }

    // Double's property

    @PropertyCondition
    infix fun KProperty1<DO, Double>.LOWER(value: Double)
    {
        this@DatabaseQuery.conditions.add(DoubleLower<DO>(this, value))
    }

    @PropertyCondition
    infix fun KProperty1<DO, Double>.LOWER_EQUALS(value: Double)
    {
        this@DatabaseQuery.conditions.add(DoubleLowerEquals<DO>(this, value))
    }

    @PropertyCondition
    infix fun KProperty1<DO, Double>.EQUALS(value: Double)
    {
        this@DatabaseQuery.conditions.add(DoubleEquals<DO>(this, value))
    }

    @PropertyCondition
    infix fun KProperty1<DO, Double>.NOT_EQUALS(value: Double)
    {
        this@DatabaseQuery.conditions.add(DoubleNotEquals<DO>(this, value))
    }

    @PropertyCondition
    infix fun KProperty1<DO, Double>.UPPER_EQUALS(value: Double)
    {
        this@DatabaseQuery.conditions.add(DoubleUpperEquals<DO>(this, value))
    }

    @PropertyCondition
    infix fun KProperty1<DO, Double>.UPPER(value: Double)
    {
        this@DatabaseQuery.conditions.add(DoubleUpper<DO>(this, value))
    }

    @PropertyCondition
    fun KProperty1<DO, Double>.BETWEEN(value1: Double, value2: Double)
    {
        this@DatabaseQuery.conditions.add(DoubleBetween<DO>(this, value1, value2))
    }

    @PropertyCondition
    fun KProperty1<DO, Double>.NOT_BETWEEN(value1: Double, value2: Double)
    {
        this@DatabaseQuery.conditions.add(DoubleNotBetween<DO>(this, value1, value2))
    }

    @PropertyCondition
    fun KProperty1<DO, Double>.ONE_OF(vararg values: Double)
    {
        this@DatabaseQuery.conditions.add(DoubleInside<DO>(this, values))
    }

    @PropertyCondition
    fun KProperty1<DO, Double>.NONE_OF(vararg values: Double)
    {
        this@DatabaseQuery.conditions.add(DoubleOutside<DO>(this, values))
    }

    // String's property

    @PropertyCondition
    infix fun KProperty1<DO, String>.LOWER(value: String)
    {
        this@DatabaseQuery.conditions.add(StringLower<DO>(this, value))
    }

    @PropertyCondition
    infix fun KProperty1<DO, String>.LOWER_EQUALS(value: String)
    {
        this@DatabaseQuery.conditions.add(StringLowerEquals<DO>(this, value))
    }

    @PropertyCondition
    infix fun KProperty1<DO, String>.EQUALS(value: String)
    {
        this@DatabaseQuery.conditions.add(StringEquals<DO>(this, value))
    }

    @PropertyCondition
    infix fun KProperty1<DO, String>.NOT_EQUALS(value: String)
    {
        this@DatabaseQuery.conditions.add(StringNotEquals<DO>(this, value))
    }

    @PropertyCondition
    infix fun KProperty1<DO, String>.UPPER_EQUALS(value: String)
    {
        this@DatabaseQuery.conditions.add(StringUpperEquals<DO>(this, value))
    }

    @PropertyCondition
    infix fun KProperty1<DO, String>.UPPER(value: String)
    {
        this@DatabaseQuery.conditions.add(StringUpper<DO>(this, value))
    }

    @PropertyCondition
    fun KProperty1<DO, String>.BETWEEN(value1: String, value2: String)
    {
        this@DatabaseQuery.conditions.add(StringBetween<DO>(this, value1, value2))
    }

    @PropertyCondition
    fun KProperty1<DO, String>.NOT_BETWEEN(value1: String, value2: String)
    {
        this@DatabaseQuery.conditions.add(StringNotBetween<DO>(this, value1, value2))
    }

    @PropertyCondition
    fun KProperty1<DO, String>.ONE_OF(vararg values: String)
    {
        this@DatabaseQuery.conditions.add(StringInside<DO>(this, values))
    }

    @PropertyCondition
    fun KProperty1<DO, String>.NONE_OF(vararg values: String)
    {
        this@DatabaseQuery.conditions.add(StringOutside<DO>(this, values))
    }

    // Enum's property

    @PropertyCondition
    infix fun <E : Enum<E>> KProperty1<DO, E>.EQUALS(value: E)
    {
        this@DatabaseQuery.conditions.add(EnumEquals<DO, E>(this, value))
    }

    @PropertyCondition
    infix fun <E : Enum<E>> KProperty1<DO, E>.NOT_EQUALS(value: E)
    {
        this@DatabaseQuery.conditions.add(EnumNotEquals<DO, E>(this, value))
    }

    @PropertyCondition
    fun <E : Enum<E>> KProperty1<DO, E>.ONE_OF(vararg values: E)
    {
        this@DatabaseQuery.conditions.add(EnumInside<DO, E>(this, values))
    }

    @PropertyCondition
    fun <E : Enum<E>> KProperty1<DO, E>.NONE_OF(vararg values: E)
    {
        this@DatabaseQuery.conditions.add(EnumOutside<DO, E>(this, values))
    }

    // Object's property

    @PropertyCondition
    infix fun <DOP : DatabaseObject> KProperty1<DO, DOP>.EQUALS(value: DOP)
    {
        this@DatabaseQuery.conditions.add(ObjectEquals<DO, DOP>(this, value))
    }

    @PropertyCondition
    infix fun <DOP : DatabaseObject> KProperty1<DO, DOP>.NOT_EQUALS(value: DOP)
    {
        this@DatabaseQuery.conditions.add(ObjectNotEquals<DO, DOP>(this, value))
    }

    @PropertyCondition
    fun <DOP : DatabaseObject> KProperty1<DO, DOP>.ONE_OF(vararg values: DOP)
    {
        this@DatabaseQuery.conditions.add(ObjectInside<DO, DOP>(this, values))
    }

    @PropertyCondition
    fun <DOP : DatabaseObject> KProperty1<DO, DOP>.NONE_OF(vararg values: DOP)
    {
        this@DatabaseQuery.conditions.add(ObjectOutside<DO, DOP>(this, values))
    }

    @PropertyCondition
    inline fun <reified DOP : DatabaseObject> KProperty1<DO, DOP>.SELECT(
        subQuery: DatabaseQuery<DOP>.() -> Unit)
    {
        val objectClass = DOP::class.java
        val query = DatabaseQuery<DOP>(objectClass)
        query.subQuery()
        this@DatabaseQuery.select(this, objectClass, query)
    }

    @AndCreation
    fun AND(create: AndCreator<DO>.() -> Unit)
    {
        val andCreator = AndCreator<DO>(this.objectClass)
        andCreator.create()
        this.conditions.add(andCreator.buildCondition())
    }

    @OrCreation
    fun OR(create: OrCreator<DO>.() -> Unit)
    {
        val orCreator = OrCreator<DO>(this.objectClass)
        orCreator.create()
        this.conditions.add(orCreator.buildCondition())
    }

    @NotCreation
    fun NOT(subQuery: DatabaseQuery<DO>.() -> Unit)
    {
        val query = DatabaseQuery<DO>(this.objectClass)
        query.subQuery()
        this.conditions.add(NotCondition(query.andConditions()))
    }

    fun <DOP : DatabaseObject> select(property: KProperty1<DO, DOP>,
                                      objectClass: Class<DOP>,
                                      subQuery: DatabaseQuery<DOP>)
    {
        val conditions = subQuery.conditions

        if (conditions.isEmpty())
        {
            this@DatabaseQuery.conditions.add(ObjectSelectAll<DO, DOP>(property, objectClass))
            return
        }

        val condition = andConditions(*conditions.toTypedArray())
        this@DatabaseQuery.conditions.add(ObjectSelect<DO, DOP>(property, condition, objectClass))
    }

    internal fun andConditions(): DatabaseCondition = andConditions(*this.conditions.toTypedArray())
}
