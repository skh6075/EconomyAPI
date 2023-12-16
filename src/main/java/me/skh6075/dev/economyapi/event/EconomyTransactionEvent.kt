package me.skh6075.dev.economyapi.event

import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import me.skh6075.dev.economyapi.session.EconomySession

class EconomyTransactionEvent(session: EconomySession) : EconomyEvent(session), Cancellable {
    companion object{
        @JvmStatic
        var handlerList = HandlerList()
    }

    private var isCancelled = false

    override fun getHandlers(): HandlerList = handlerList

    override fun isCancelled(): Boolean = isCancelled

    fun setCancelled(){
        setCancelled(true)
    }

    override fun setCancelled(cancel: Boolean) {
        isCancelled = cancel
    }
}