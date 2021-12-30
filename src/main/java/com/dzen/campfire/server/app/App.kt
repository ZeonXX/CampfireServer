package com.dzen.campfire.server.app

import com.dzen.campfire.api.API
import com.dzen.campfire.server.controllers.*
import com.dzen.campfire.server.tables.TStatistic
import com.dzen.campfire.api.tools.client.TokenProvider
import com.dzen.campfire.api.tools.server.ApiServer
import com.dzen.campfire.api.tools.server.RequestFactory
import com.sup.dev.java.libs.debug.err
import com.sup.dev.java.libs.debug.log
import com.sup.dev.java.tools.ToolsDate
import com.sup.dev.java.tools.ToolsFiles
import com.sup.dev.java.tools.ToolsThreads
import com.sup.dev.java_pc.google.GoogleAuth
import com.sup.dev.java_pc.google.GoogleNotification
import com.sup.dev.java_pc.sql.Database
import com.sup.dev.java_pc.sql.DatabasePool
import com.sup.dev.java_pc.sql.SqlQueryRemove
import java.io.File
import java.nio.charset.Charset

object App {

    val accountProvider = AccountProviderImpl()
    val test = (ToolsFiles.readLineOrNull(File("secrets/Config.txt"), 0)?:"")!="release"

    @JvmStatic
    fun main(args: Array<String>) {

        val patchPrefix = ToolsFiles.readLineOrNull(File("secrets/Config.txt"), 1)?:""
        val databaseLogin = ToolsFiles.readLineOrNull(File("secrets/Config.txt"), 2)?:""
        val databasePassword = ToolsFiles.readLineOrNull(File("secrets/Config.txt"), 3)?:""
        val databaseName = ToolsFiles.readLineOrNull(File("secrets/Config.txt"), 4)?:""
        val databaseAddress = ToolsFiles.readLineOrNull(File("secrets/Config.txt"), 5)?:""

        val keysFile = File("secrets/Keys.txt")
        val googleNotificationKey = ToolsFiles.readLineOrNull(keysFile, 0)?:""
        val googleAuthKeyId = ToolsFiles.readLineOrNull(keysFile, 1)?:""
        val googleAuthKeySecret = ToolsFiles.readLineOrNull(keysFile, 2)?:""
        val jksPassword = ToolsFiles.readLineOrNull(keysFile, 3)?:""

        val keyFileJKS = File("secrets/Certificate.jks")
        val keyFileBKS = File("secrets/Certificate.bks")
        System.setProperty("javax.net.ssl.keyStore","secrets/Certificate.jks")
        System.setProperty("javax.net.ssl.keyStorePassword", jksPassword)
        System.setProperty("javax.net.ssl.trustStore","secrets/Certificate.jks")
        System.setProperty("javax.net.ssl.trustStorePassword", jksPassword)

        val jarFile = "${patchPrefix}CampfireServer.jar"

        val botTokensList = ToolsFiles.readListOrNull("secrets/BotsTokens.txt")?:ArrayList()

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
                    botTokensList,
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
