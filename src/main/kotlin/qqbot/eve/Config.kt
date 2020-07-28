package qqbot.eve

import com.natpryce.konfig.*
import java.io.File

data class ConfigProperty(var QQID: Long = 0,
                          var QQPWD: String = "",
                          var PREFIX: String = "?",
                          var EVE_PRICE_SEARCH: String = "s",
                          var ROLL: String = "r")

/**
 * 配置
 */
class RobotConfig {
    public var config: ConfigProperty

    constructor() {
        val qqid = Key("QQID", longType)
        val qqpwd = Key("QQPWD", stringType)
        val botPrefix = Key("PREFIX", stringType)
        val eve_price = Key("EVE_PRICE_SEARCH", stringType)
        val roll = Key("ROLL", stringType)

        val config = ConfigurationProperties.fromFile(File("private.properties"))
        val botConfig = ConfigurationProperties.fromFile(File("gradle.properties"))

        this.config = ConfigProperty(
                QQID = config[qqid],
                QQPWD = config[qqpwd],
                PREFIX = botConfig[botPrefix],
                EVE_PRICE_SEARCH = botConfig[eve_price],
                ROLL = botConfig[roll]
        )

        println(this.config.toString())
    }
}