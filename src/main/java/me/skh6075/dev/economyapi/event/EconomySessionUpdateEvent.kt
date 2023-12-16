package me.skh6075.dev.economyapi.event

import org.bukkit.event.HandlerList
import me.skh6075.dev.economyapi.session.EconomySession

class EconomySessionUpdateEvent(session: EconomySession) : EconomyEvent(session) {
    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }
}