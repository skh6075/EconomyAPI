package me.skh6075.dev.economyapi.connection

import org.bukkit.configuration.file.FileConfiguration
import java.sql.Connection
import java.sql.DriverManager

class Connector(config: FileConfiguration){
    private val url = "jdbc:mysql://${config.getString("host", "127.0.0.1")}:${config.getString("port", "3306")}/${config.getString("scheme")}"
    private val username = config.getString("username")
    private val password = config.getString("password")

    fun get(): Connection{
        return DriverManager.getConnection(url, username, password)
    }
}