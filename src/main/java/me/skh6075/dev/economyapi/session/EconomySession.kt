package me.skh6075.dev.economyapi.session

import org.bukkit.entity.Player
import me.skh6075.dev.economyapi.EconomyAPI
import me.skh6075.dev.economyapi.currency.Currency
import me.skh6075.dev.economyapi.event.EconomySessionUpdateEvent
import java.sql.ResultSet

class EconomySession(private val player: Player) {
    private val plugin: EconomyAPI = EconomyAPI.getInstance()

    private val currencies: MutableMap<String, Int> = mutableMapOf()

    init {
        plugin.getCurrencies().forEach { (_, currency) ->
            val resultSet: ResultSet = plugin.database.getByCurrency(player, currency)
            if(resultSet.next()){
                this.currencies[currency.getName()] = resultSet.getInt("amount")
            }else{
                registerCurrencyData(currency)
            }
        }
    }

    private fun registerCurrencyData(currency: Currency){
        plugin.database.addCurrency(player, currency)
        this.currencies[currency.getName()] = currency.getDefaultMoney()
    }

    private fun isLoadedCurrency(currency: Currency): Boolean = currencies.containsKey(currency.getName())

    fun getMoney(currency: Currency): Int{
        if(!isLoadedCurrency(currency)){
            return currency.getDefaultMoney()
        }

        return currencies[currency.getName()]!!
    }

    fun addMoney(amount: Int, currency: Currency = plugin.getDefaultCurrency(), sync: Boolean = false){
        if(!isLoadedCurrency(currency)){
            return
        }

        this.currencies[currency.getName()] = getMoney(currency) + amount
        if(sync){
            plugin.database.updateCurrency(player, currency, this.currencies[currency.getName()]!!)
        }

        EconomySessionUpdateEvent(this).callEvent()
    }

    fun reduceMoney(amount: Int, currency: Currency = plugin.getDefaultCurrency(), sync: Boolean = false){
        if(!isLoadedCurrency(currency)){
            return
        }

        this.currencies[currency.getName()] = getMoney(currency) - amount
        if(sync){
            plugin.database.updateCurrency(player, currency, this.currencies[currency.getName()]!!)
        }

        EconomySessionUpdateEvent(this).callEvent()
    }

    fun setMoney(amount: Int, currency: Currency = plugin.getDefaultCurrency(), sync: Boolean = false){
        if(!isLoadedCurrency(currency)){
            return
        }

        this.currencies[currency.getName()] = amount
        if(sync){
            plugin.database.updateCurrency(player, currency, this.currencies[currency.getName()]!!)
        }

        EconomySessionUpdateEvent(this).callEvent()
    }

    fun save(){
        plugin.getCurrencies().forEach { (_, currency) ->
            plugin.database.updateCurrency(player, currency, getMoney(currency))
        }
    }
}