package me.skh6075.dev.economyapi

import io.github.monun.kommand.StringType
import io.github.monun.kommand.kommand
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import me.skh6075.dev.economyapi.connection.Connector
import me.skh6075.dev.economyapi.connection.Database
import me.skh6075.dev.economyapi.currency.CashCurrency
import me.skh6075.dev.economyapi.currency.Currency
import me.skh6075.dev.economyapi.currency.CurrencyConvertType
import me.skh6075.dev.economyapi.currency.GoldCurrency
import me.skh6075.dev.economyapi.executor.Executor
import me.skh6075.dev.economyapi.session.EconomySession
import java.util.Objects
import java.util.logging.Level

class EconomyAPI : JavaPlugin(), Listener {
    companion object {
        private val instance: EconomyAPI = EconomyAPI()

        val prefix: String = ChatColor.translateAlternateColorCodes('&', "&r&l&f[&e재화&f]&r&7 ")

        private val sessions: MutableMap<Player, EconomySession> = mutableMapOf()

        private val currencies: MutableMap<String, Currency> = mutableMapOf()
        private val defaultCurrency: Currency = GoldCurrency()

        init {
            addCurrency(defaultCurrency, CashCurrency())
        }

        fun getInstance(): EconomyAPI = instance;

        fun addCurrency(vararg currencies: Currency){
            for (currency in currencies) {
                this.currencies[currency.getName()] = currency
            }
        }
    }

    lateinit var database: Database

    override fun onEnable() {
        saveDefaultConfig()
        if(config.getString("scheme", "your_scheme") === "your_scheme"){
            logger.log(Level.WARNING, "MySQL 기본 설정을 완료해주세요.")
            server.pluginManager.disablePlugin(this)
            return
        }

        database = Database(Connector(config).get())

        kommand {
            register("내돈"){
                val currencyNames = string(StringType.QUOTABLE_PHRASE)

                requires { isPlayer }
                executes { _ ->
                    player.sendMessage(Component.text("${prefix}/내돈 [재화]"))
                }
                then("currency" to currencyNames){
                    executes {
                        val session = getSession(player)!!
                        val currency = currencies[it["currency"]]!!

                        player.sendMessage("${prefix}현재 내가 보유한 ${currency.getName()} : " + ChatColor.translateAlternateColorCodes('&', "&f${currency.format(session.getMoney(currency), CurrencyConvertType.DEFAULT)}"))
                    }
                }
            }
            register("돈주기"){
                val target = player()
                target.suggests {
                    suggest(server.onlinePlayers.map { it.name }.toMutableSet())
                }
                val currencyNames = string(StringType.QUOTABLE_PHRASE)

                requires { isOp }
                executes { _ ->
                    player.sendMessage("${prefix}/돈주기 [플레이어] [액수] [재화]")
                }
                then("target" to target){
                    executes { Executor.giveMoney(player, it["target"]) }
                    then("amount" to int()){
                        executes { Executor.giveMoney(player, it["target"], it["amount"]) }
                        then("currency" to currencyNames){
                            executes { Executor.giveMoney(player, it["target"], it["amount"], currencies[it["currency"]]!!) }
                        }
                    }
                }
            }
        }

        Objects.requireNonNull(getCommand("내돈"))?.setDescription("내 재화를 확인합니다.")
        Objects.requireNonNull(getCommand("돈주기"))?.setDescription("플레이어에게 재화를 지급합니다.")

        server.pluginManager.registerEvents(this, this)
    }

    override fun onDisable() {
        sessions.forEach{ (_, session) ->
            session.save()
        }
    }

    fun currencyNames(): MutableSet<String> = currencies.keys

    fun getCurrencies(): MutableMap<String, Currency> = currencies

    fun getDefaultCurrency(): Currency = defaultCurrency

    fun getSession(player: Player): EconomySession? = sessions[player]

    fun removeSession(player: Player){
        val session = getSession(player)
        if(session !== null){
            session.save()
            sessions.remove(player)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerJoinEvent(event: PlayerJoinEvent){
        sessions[event.player] = EconomySession(event.player)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerQuitEvent(event: PlayerQuitEvent){
        removeSession(event.player)
    }
}
