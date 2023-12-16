package me.skh6075.dev.economyapi.currency

import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.pow

abstract class Currency{
    companion object{
        private val KOREAN_BIG_ORDERS = arrayOf("", "만", "억", "조", "경")
    }

    abstract fun getName(): String

    abstract fun getSymbol(): String

    abstract fun getDefaultMoney(): Int

    open fun canTransaction(): Boolean = false

    fun format(amount: Int, formatType: CurrencyConvertType = CurrencyConvertType.DEFAULT): String{
        return when(formatType) {
            CurrencyConvertType.DEFAULT -> {
                if(amount < 10000){
                    return "${amount}${getSymbol()}"
                }

                var num = amount;
                var str = ""

                for(i in KOREAN_BIG_ORDERS.size - 1 downTo 0){
                    val unit = 10000.0.pow(i.toDouble()).toInt()
                    val part = (num / unit)
                    if (part > 0) {
                        str += "$part${KOREAN_BIG_ORDERS[i]}"
                    }

                    num %= unit
                }

                return "${str}${getSymbol()}"
            }
            CurrencyConvertType.SHORTEN -> {
                if(amount < 10000){
                    return "${amount}.0${getSymbol()}"
                }

                val unitSizeof = KOREAN_BIG_ORDERS.size - 1
                val decimal = max(0.0, floor(log10(amount.toDouble()) / unitSizeof)).toInt()
                val amountFormatted = amount.toDouble() / 10.0.pow(decimal * unitSizeof)

                return "${floor(amountFormatted * 10) / 10}${KOREAN_BIG_ORDERS[decimal]}${getSymbol()}"
            }
            else -> return "${amount}${getSymbol()}"
        }
    }
}