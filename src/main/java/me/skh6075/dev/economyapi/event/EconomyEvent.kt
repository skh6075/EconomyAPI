package me.skh6075.dev.economyapi.event

import org.bukkit.event.Event
import me.skh6075.dev.economyapi.session.EconomySession

abstract class EconomyEvent(@get:JvmName("getSession") val session: EconomySession) : Event()
