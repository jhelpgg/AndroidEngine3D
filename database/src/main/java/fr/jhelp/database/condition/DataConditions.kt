/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.database.condition

infix fun DataCondition.AND(dataCondition:DataCondition) = DataAnd(this, dataCondition)

infix fun DataCondition.OR(dataCondition:DataCondition) = DataOr(this, dataCondition)

infix fun String.lower(value:Int) = DataIntLower(this, value)
infix fun String.lower(value:Long) = DataLongLower(this, value)
infix fun String.lower(value:Float) = DataFloatLower(this, value)
infix fun String.lower(value:Double) = DataDoubleLower(this, value)
infix fun String.lower(value:String) = DataStringLower(this, value)

infix fun String.upper(value:Int) = DataIntUpper(this, value)
infix fun String.upper(value:Long) = DataLongUpper(this, value)
infix fun String.upper(value:Float) = DataFloatUpper(this, value)
infix fun String.upper(value:Double) = DataDoubleUpper(this, value)
infix fun String.upper(value:String) = DataStringUpper(this, value)

infix fun String.lowerEqual(value:Int) = DataIntLowerEqual(this, value)
infix fun String.lowerEqual(value:Long) = DataLongLowerEqual(this, value)
infix fun String.lowerEqual(value:Float) = DataFloatLowerEqual(this, value)
infix fun String.lowerEqual(value:Double) = DataDoubleLowerEqual(this, value)
infix fun String.lowerEqual(value:String) = DataStringLowerEqual(this, value)

infix fun String.upperEqual(value:Int) = DataIntUpperEqual(this, value)
infix fun String.upperEqual(value:Long) = DataLongUpperEqual(this, value)
infix fun String.upperEqual(value:Float) = DataFloatUpperEqual(this, value)
infix fun String.upperEqual(value:Double) = DataDoubleUpperEqual(this, value)
infix fun String.upperEqual(value:String) = DataStringUpperEqual(this, value)

infix fun String.equal(value:Int) = DataIntEqual(this, value)
infix fun String.equal(value:Long) = DataLongEqual(this, value)
infix fun String.equal(value:Float) = DataFloatEqual(this, value)
infix fun String.equal(value:Double) = DataDoubleEqual(this, value)
infix fun String.equal(value:String) = DataStringEqual(this, value)

infix fun String.notEqual(value:Int) = DataIntNotEqual(this, value)
infix fun String.notEqual(value:Long) = DataLongNotEqual(this, value)
infix fun String.notEqual(value:Float) = DataFloatNotEqual(this, value)
infix fun String.notEqual(value:Double) = DataDoubleNotEqual(this, value)
infix fun String.notEqual(value:String) = DataStringNotEqual(this, value)

infix fun String.where(condition: DataCondition) = DataStorableCondition(this, condition)
