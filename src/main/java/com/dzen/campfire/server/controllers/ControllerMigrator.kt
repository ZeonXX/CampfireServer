package com.dzen.campfire.server.controllers

import com.dzen.campfire.api.API
import com.dzen.campfire.api.API_TRANSLATE
import com.dzen.campfire.api_media.requests.RResourcesPut
import com.dzen.campfire.server.app.App
import com.dzen.campfire.server.tables.TFandoms
import com.dzen.campfire.server.tables.TPublications
import com.dzen.campfire.server.tables.TTranslates
import com.dzen.campfire.server.tables.TTranslatesHistory
import com.sup.dev.java.libs.debug.info
import com.sup.dev.java.libs.debug.log
import com.sup.dev.java.tools.ToolsDate
import com.sup.dev.java.tools.ToolsFiles
import com.sup.dev.java_pc.sql.*
import java.io.File
import java.lang.Exception
import javax.xml.crypto.Data


object ControllerMigrator {

    class XX(
            val key: String,
            val languageId: Long,
            val date: Long,
            val text: String
    )

    fun start() {

       // val v = Database.select("XXX", SqlQuerySelect(TPublications.NAME, TPublications.id, TPublications.fandom_id, TPublications.FANDOM_NAME)
       //         .where(Sql)
       // )




/*
        val v = Database.select("xxx", SqlQuerySelect(TTranslates.NAME, TTranslates.id, TTranslates.translate_key, TTranslates.text)
                .where(TTranslates.language_id, "=", 2)
        )

        while (v.hasNext()) {
            val id: Long = v.next()
            val translate_key: String = v.next()
            val oldText: String = v.next()

            try {
                val text = API_TRANSLATE.map[translate_key]!!.text
                Database.update("zzz", SqlQueryUpdate(TTranslates.NAME)
                        .where(TTranslates.id, "=", id)
                        .updateValue(TTranslates.text, text))

            } catch (e: Exception) {

            }
        }

        if(true) System.exit(1)
*/






   /*    val v = Database.select("xxx", SqlQuerySelect(TTranslatesHistory.NAME, TTranslatesHistory.translate_key, TTranslatesHistory.language_id, TTranslatesHistory.date_history_created, TTranslatesHistory.new_text))
        val list = ArrayList<XX>()
        while (v.hasNext()){
            val x = XX(v.next(), v.next(), v.next(), v.next())
            var needPut = true
            for(i in list) {
                if(i.key == x.key) {
                    if (i.date > x.date) {
                        needPut = false
                        break
                    }
                    else {
                        list.remove(i)
                        break
                    }
                }
            }
            if(needPut) list.add(x)
        }
        for(i in list){

            val id = Database.select("zzz", SqlQuerySelect(TTranslates.NAME, TTranslates.id)
                    .whereValue(TTranslates.translate_key, "=", i.key)
                    .whereValue(TTranslates.language_id, "=", i.languageId)
            ).nextLongOrZero()

            if(id > 0){
                Database.update("zzz", SqlQueryUpdate(TTranslates.NAME)
                        .whereValue(TTranslates.id, "=", id)
                        .updateValue(TTranslates.text, i.text))
            }else{
                Database.insert("ControllerServerTranslates.putTranslateWithRequest insert", TTranslates.NAME,
                        TTranslates.language_id, i.languageId,
                        TTranslates.translate_key, i.key,
                        TTranslates.text, i.text,
                        TTranslates.hint, "",
                        TTranslates.project_key, "Campfire"
                )
            }


        }
        if(true) System.exit(1)*/



        if(!App.test) {
            for (i in API_TRANSLATE.map.values) {
                if (i.serverFlag_WillUpload) ru(i.key, i.text)
            }
        }


        /*  Debug.saveTime()
          Database.update("xxx", SqlQueryUpdate(TAccounts.NAME)
                  .update(TAccounts.karma_count_total,
                          Sql.IFNULL("(SELECT ${Sql.SUM(TPublicationsKarmaTransactions.karma_count)}" +
                                  " FROM ${TPublicationsKarmaTransactions.NAME} " +
                                  "WHERE ${TPublicationsKarmaTransactions.target_account_id}=${TAccounts.NAME}.${TAccounts.id} AND ${TPublicationsKarmaTransactions.change_account_karma}=1)", 0)))
          Debug.printTime()*/


        // indexImages()


    }

    fun en(key: String, text: String) {
        x(API.LANGUAGE_EN, key, text)
    }

    fun ru(key: String, text: String) {
        x(API.LANGUAGE_RU, key, text)
    }

    fun x(languageId: Long, key: String, text: String) {
        info("Upload languageId[$languageId] key[$key], text[$text]")
        val v = Database.select("xxx", SqlQuerySelect(TTranslates.NAME, TTranslates.id)
                .where(TTranslates.language_id, "=", languageId)
                .whereValue(TTranslates.translate_key, "=", key)
        )
        if (!v.isEmpty) return
        Database.insert("xxx", TTranslates.NAME,
                TTranslates.language_id, languageId,
                TTranslates.translate_key, key,
                TTranslates.text, text,
                TTranslates.hint, "",
                TTranslates.project_key, API.PROJECT_KEY_CAMPFIRE
        )
    }

    //
    //  Indexing Images
    //


    fun indexImages() {

        val databaseLogin = ToolsFiles.readLineOrNull(File("secrets/Config.txt"), 6)?:""
        val databasePassword = ToolsFiles.readLineOrNull(File("secrets/Config.txt"), 7)?:""
        val databaseName = ToolsFiles.readLineOrNull(File("secrets/Config.txt"), 8)?:""
        val databaseAddress = ToolsFiles.readLineOrNull(File("secrets/Config.txt"), 9)?:""
        val db = DatabaseInstance(databaseLogin, databasePassword, databaseName, databaseAddress)

        info("indexing posts...")
        indexingPosts(db)
        info("indexing messages...")
        indexingMessages(db)
        info("indexing comments...")
        indexingComments(db)
        info("indexing fandom gallery...")
        indexingFandomGallery(db)
        info("indexing stickers...")
        indexingStickers(db)
        info("indexing stickers packs...")
        indexingStickersPacks(db)
        info("indexing tags...")
        indexingTags(db)
        info("indexing wiki...")
        indexingWiki(db)
        info("indexing TAccounts...")
        indexingTAccounts(db)
        info("indexing TActivities...")
        indexingTActivities(db)
        info("indexing TChats...")
        indexingTChats(db)
        info("indexing TFandoms...")
        indexingTFandoms(db)
        info("indexing TCollisions...")
        indexingTCollisions(db)
        info("indexing finished")
    }

    fun indexingPosts(db: DatabaseInstance) {
        val total = Database.select("indexImages count", SqlQuerySelect(TPublications.NAME, Sql.COUNT)
                .where(TPublications.publication_type, "=", API.PUBLICATION_TYPE_POST)).nextLongOrZero()
        //  1015212
        var offset = 0
        while (true) {

            val array = ControllerPublications.parseSelect(Database.select("indexImages select", ControllerPublications.instanceSelect(1)
                    .where(TPublications.publication_type, "=", API.PUBLICATION_TYPE_POST)
                    .offset_count(offset, 100)))
            if (array.isEmpty()) return
            offset += array.size

            for (publication in array) {
                val ids = publication.getResourcesList()
                if (ids.isEmpty()) continue
                db.update(SqlQueryUpdate("resources")
                        .where(SqlWhere.WhereIN("id", ids))
                        .update("publication_id", publication.id)
                )
            }

            info("indexing images $offset / $total")
        }

    }

    fun indexingMessages(db: DatabaseInstance) {
        throw RuntimeException()
    }

    fun indexingComments(db: DatabaseInstance) {
        throw RuntimeException()
    }

    fun indexingFandomGallery(db: DatabaseInstance) {
        throw RuntimeException()
    }

    fun indexingStickers(db: DatabaseInstance) {
        throw RuntimeException()
    }

    fun indexingStickersPacks(db: DatabaseInstance) {
        throw RuntimeException()
    }

    fun indexingTags(db: DatabaseInstance) {
        throw RuntimeException()
    }

    fun indexingWiki(db: DatabaseInstance) {
        throw RuntimeException()
    }

    fun indexingTAccounts(db: DatabaseInstance) {
        throw RuntimeException()
    }

    fun indexingTActivities(db: DatabaseInstance) {
        throw RuntimeException()
    }

    fun indexingTChats(db: DatabaseInstance) {
        throw RuntimeException()
    }

    fun indexingTFandoms(db: DatabaseInstance) {
        throw RuntimeException()
    }

    fun indexingTCollisions(db: DatabaseInstance) {
        throw RuntimeException()
    }


    //
    //  Upload Resources
    //

    fun uploadImages() {
       // val files = File("C:\\Users\\User\\Desktop\\xxx\\Аватарки").listFiles()
       // var x = 0
       // for (f in files) {
       //      RResourcesPut(ToolsFiles.readFile(f), 0, 0, "bg")
       //              .onComplete { System.err.println("${1 + x++} / ${files.size}   ${f.name} " + it.resourceId) }
       //              .sendNow(ControllerResources.api!!)
       // }
    }

}