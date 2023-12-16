package me.skh6075.dev.economyapi.transaction

import me.skh6075.dev.economyapi.currency.Currency
import me.skh6075.dev.economyapi.event.EconomyTransactionEvent
import me.skh6075.dev.economyapi.session.EconomySession
import me.skh6075.dev.economyapi.transaction.EconomyTransactionResult

class EconomyTransaction(
        val session: EconomySession,
        val transactionType: EconomyTransactionType,
        val currency: Currency,
        val amount: Int,
        val receiverSession: EconomySession? = null
) {
    fun execute(sync: Boolean = true): EconomyTransactionResult {
        val result: Boolean = EconomyTransactionEvent(session).callEvent()
        if(!result){
            return EconomyTransactionResult.PLUGIN_CANCELLED
        }

        return when(true){
            (transactionType === EconomyTransactionType.ADD) -> {
                session.addMoney(amount, currency, sync)
                return EconomyTransactionResult.SUCCESS
            }
            (transactionType === EconomyTransactionType.REDUCE) -> {
                session.reduceMoney(amount, currency, sync)
                return EconomyTransactionResult.SUCCESS
            }
            (transactionType === EconomyTransactionType.SET) -> {
                session.setMoney(amount, currency, sync)
                return EconomyTransactionResult.SUCCESS
            }
            (transactionType === EconomyTransactionType.PAY) -> {
                if(receiverSession === null){
                    return EconomyTransactionResult.PLUGIN_CANCELLED
                }

                if(!currency.canTransaction()){
                    return EconomyTransactionResult.NOT_ALLOWED
                }

                if(session.getMoney(currency) - amount < 0){
                    return EconomyTransactionResult.NOT_ENOUGH_MONEY
                }

                session.reduceMoney(amount, currency, sync)
                receiverSession.addMoney(amount, currency, sync)

                return EconomyTransactionResult.SUCCESS
            }
            else -> EconomyTransactionResult.PLUGIN_CANCELLED
        }
    }
}