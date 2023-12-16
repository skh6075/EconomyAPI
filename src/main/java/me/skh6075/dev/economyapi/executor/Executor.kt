package me.skh6075.dev.economyapi.executor

import org.bukkit.ChatColor
import org.bukkit.entity.Player
import me.skh6075.dev.economyapi.EconomyAPI
import me.skh6075.dev.economyapi.currency.Currency

object Executor {
    fun giveMoney(sender: Player, player: Player, amount: Int? = null, currency: Currency = EconomyAPI.getInstance().getDefaultCurrency()){
        val session = EconomyAPI.getInstance().getSession(player)
        if(session === null){
            sender.sendMessage("${EconomyAPI.prefix}해당 플레이어는 오프라인 입니다.")
            return
        }

        if(amount === null || amount <= 0){
            sender.sendMessage("${EconomyAPI.prefix}/돈주기 [플레이어] [액수] [재화]")
            return
        }

        session.addMoney(amount, currency)
        sender.sendMessage(EconomyAPI.prefix + ChatColor.translateAlternateColorCodes('&', "&e${player.name}님&7에게 &f${currency.format(amount)}&r&7 재화를 지급했습니다."))
    }
}