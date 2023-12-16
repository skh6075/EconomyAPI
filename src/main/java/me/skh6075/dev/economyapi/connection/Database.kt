package me.skh6075.dev.economyapi.connection

import org.bukkit.entity.Player
import me.skh6075.dev.economyapi.currency.Currency
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class Database(val connection: Connection) {
    fun getByCurrency(player: Player, currency: Currency): ResultSet{
        val statement: PreparedStatement = connection.prepareStatement("SELECT * FROM player_money WHERE `uuid` = ? AND `currency` = ?")

        statement.setString(1, player.uniqueId.toString())
        statement.setString(2, currency.getName())

        return statement.executeQuery()
    }

    fun addCurrency(player: Player, currency: Currency): ResultSet{
        val statement: PreparedStatement = connection.prepareStatement("INSERT INTO player_money (`uuid`, `currency`, `amount`) VALUES (?, ?, ?)");

        statement.setString(1, player.uniqueId.toString())
        statement.setString(2, currency.getName())
        statement.setInt(3, currency.getDefaultMoney())

        return statement.executeQuery()
    }

    fun updateCurrency(player: Player, currency: Currency, amount: Int): ResultSet{
        val statement: PreparedStatement = connection.prepareStatement("UPDATE player_money SET `amount` = ? WHERE `uuid` = ? AND `currency` = ?");

        statement.setInt(1, amount)
        statement.setString(2, player.uniqueId.toString())
        statement.setString(3, currency.getName())

        return statement.executeQuery()
    }
}