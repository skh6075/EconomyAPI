package me.skh6075.dev.economyapi.currency

import org.bukkit.ChatColor

class CashCurrency : Currency() {
    override fun getName(): String = "캐쉬"

    override fun getSymbol(): String = ChatColor.translateAlternateColorCodes('&', "&l&eC&r&f")

    override fun getDefaultMoney(): Int = 0
}