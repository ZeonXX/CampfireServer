package com.dzen.campfire.server.controllers

import com.dzen.campfire.api.API
import com.dzen.campfire.api.API_TRANSLATE
import com.dzen.campfire.server.app.App
import com.dzen.campfire.server.tables.TPublications
import com.dzen.campfire.server.tables.TTranslates
import com.sup.dev.java.libs.debug.info
import com.sup.dev.java.tools.ToolsFiles
import com.sup.dev.java_pc.sql.*
import java.io.File


object ControllerMigrator {

    fun start() {
        if(!App.test) {
            for (i in API_TRANSLATE.map.values) {
                if (i.serverFlag_WillUpload) ru(i.key, i.text)
            }
        }
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

        val databaseLogin = App.secretsConfig.getString("database_media_login")
        val databasePassword = App.secretsConfig.getString("database_media_password")
        val databaseName = App.secretsConfig.getString("database_media_name")
        val databaseAddress = App.secretsConfig.getString("database_media_address")
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