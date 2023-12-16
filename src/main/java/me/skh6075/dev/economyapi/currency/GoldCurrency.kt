package me.skh6075.dev.economyapi.currency

import org.bukkit.ChatColor

class GoldCurrency : Currency() {
    override fun getName(): String = "골드"

    override fun getSymbol(): String = ChatColor.translateAlternateColorCodes('&', "&l&6G&r&f")

    override fun getDefaultMoney(): Int = 10000

    override fun canTransaction(): Boolean = true

}