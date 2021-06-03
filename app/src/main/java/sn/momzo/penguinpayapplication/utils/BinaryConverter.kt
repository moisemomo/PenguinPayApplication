package sn.momzo.penguinpayapplication.utils

import java.lang.StringBuilder
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.pow

object BinaryConverter {
    fun binaryToIntNumber(amount: String): Int {
        var num = amount.toLong()
        var intAmount = 0
        var i = 0
        var remainder: Long

        while (num.toInt() != 0) {
            remainder = num % 10
            num /= 10
            intAmount += (remainder * 2.0.pow(i.toDouble())).toInt()
            ++i
        }
        return intAmount
    }

    /**
     * Multiply value by 2^n, convert to an BigInteger, convert to binary String,
     *  add a decimal point at position n (from right to left).
     */
    fun bigDecimalToBinary(amount: BigDecimal): String {
        val n = 20
        var bd = amount
        val mult: BigDecimal = BigDecimal(2).pow(n)
        bd = bd.multiply(mult)
        val bi: BigInteger = bd.toBigInteger()
        val str = StringBuilder(bi.toString(2))

        while (str.length < n + 1) {  // +1 for leading zero
            str.insert(0, "0")
        }
        str.insert(str.length - n, ".")
        return str.toString()
    }
}