package com.dzen.campfire.server.app

import com.dzen.campfire.api.API
import com.dzen.campfire.server.controllers.*
import com.dzen.campfire.api.tools.server.ApiServer
import com.dzen.campfire.api.tools.server.RequestFactory
import com.sup.dev.java.libs.debug.err
import com.sup.dev.java.libs.json.Json
import com.sup.dev.java.tools.ToolsDate
import com.sup.dev.java.tools.ToolsFiles
import com.sup.dev.java.tools.ToolsThreads
import com.sup.dev.java_pc.google.GoogleAuth
import com.sup.dev.java_pc.google.GoogleNotification
import com.sup.dev.java_pc.sql.Database
import com.sup.dev.java_pc.sql.DatabasePool
import java.io.File
import java.nio.charset.Charset

object App {

    val accountProvider = AccountProviderImpl()
    val secrets = Json(ToolsFiles.readString("secrets/Secrets.json"))
    val secretsBotsTokens = secrets.getStrings("bots_tokens")!!.map { it?:"" }.toTypedArray()
    val secretsConfig = secrets.getJson("config")!!
    val secretsKeys = secrets.getJson("keys")!!
    val test = secretsConfig.getString("build_type")!="release"

    @JvmStatic
    fun main(args: Array<String>) {

        val patchPrefix = secretsConfig.getString("patch_prefix")
        val databaseLogin = secretsConfig.getString("database_login")
        val databasePassword = secretsConfig.getString("database_password")
        val databaseName = secretsConfig.getString("database_name")
        val databaseAddress = secretsConfig.getString("database_address")

        val googleNotificationKey = secretsKeys.getString("google_notification_key")
        val googleAuthKeyId = secretsKeys.getString("google_auth_key_id")
        val googleAuthKeySecret = secretsKeys.getString("google_auth_key_secret")
        val jksPassword = secretsKeys.getString("jks_password")

        val keyFileJKS = File("secrets/Certificate.jks")
        val keyFileBKS = File("secrets/Certificate.bks")
        val jarFile = "${patchPrefix}CampfireServer.jar"

        try {
            System.err.println("Sayzen Studio")
            System.err.println(ToolsDate.getTimeZoneName() + " ( " + ToolsDate.getTimeZoneHours() + " )")
            System.err.println("Charset: " + Charset.defaultCharset())
            System.err.println("API Version: " + API.VERSION)

            GoogleNotification.init(googleNotificationKey, arrayOf("https://push.33rd.dev/push"))
            GoogleAuth.init(googleAuthKeyId, googleAuthKeySecret)

            val requestFactory = RequestFactory(jarFile, File("").absolutePath + "\\CampfireServer\\src\\main\\java")

            val apiServer = ApiServer(requestFactory,
                    accountProvider,
                    ToolsFiles.readFile(keyFileJKS),
                    ToolsFiles.readFile(keyFileBKS),
                    jksPassword,
                    API.PORT_HTTPS,
                    API.PORT_HTTP,
                    API.PORT_CERTIFICATE,
                    secretsBotsTokens,
            )

            while (true) {
                try {
                    Database.setGlobal(DatabasePool(databaseLogin, databasePassword, databaseName, databaseAddress, if(test) 1 else 8) { key, time -> ControllerStatistic.logQuery(key, time, API.VERSION) })
                    break
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    System.err.println("Database crash... try again at 5 sec")
                    ToolsThreads.sleep(5000)
                }

            }

            apiServer.onError = { key, ex -> ControllerStatistic.logError(key, ex) }
            apiServer.statisticCollector = { key, time, version -> ControllerStatistic.logRequest(key, time, version) }

            ControllerNotifications.init()
            System.err.println("Starting of migrator")
            ControllerMigrator.start()
            System.err.println("Starting of demons [ControllerUpdater]")
            ControllerUpdater.start()
            System.err.println("Starting of demons [ControllerGarbage]")
            ControllerGarbage.start()
            System.err.println("Starting of demons [ControllerPending]")
            ControllerPending.start()
            System.err.println("Starting of demons [ControllerDonates]")
            ControllerDonates.start()
            System.err.println("Starting of demons [ControllerCensor]")
            ControllerCensor.start()
            System.err.println("Starting of demons [ControllerServerTranslates]")
            ControllerServerTranslates.start()

            System.err.println("Update karma category")
            ControllerOptimizer.karmaCategoryUpdateIfNeed()
            System.err.println("------------ (\\/)._.(\\/) ------------")

            apiServer.startServer()

        } catch (th: Throwable) {
            err(th)
        }

    }


}
