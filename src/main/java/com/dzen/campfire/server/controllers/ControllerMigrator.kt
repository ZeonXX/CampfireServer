package com.dzen.campfire.server.controllers

import com.dzen.campfire.api.API
import com.dzen.campfire.api.API_RESOURCES
import com.dzen.campfire.api.API_TRANSLATE
import com.dzen.campfire.api.models.publications.chat.PublicationChatMessage
import com.dzen.campfire.server.app.App
import com.dzen.campfire.server.tables.TAccountsEmails
import com.dzen.campfire.server.tables.TPublications
import com.dzen.campfire.server.tables.TTranslates
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ImportUserRecord
import com.google.firebase.auth.UserImportOptions
import com.google.firebase.auth.hash.Bcrypt
import com.sup.dev.java.libs.debug.err
import com.sup.dev.java.libs.debug.info
import com.sup.dev.java.tools.ToolsCryptography
import com.sup.dev.java.tools.ToolsFiles
import com.sup.dev.java.tools.ToolsThreads
import com.sup.dev.java_pc.sql.*
import java.io.File


object ControllerMigrator {

    fun start() {
        if (!App.test) {
            for (i in API_TRANSLATE.map.values) {
                if (i.serverFlag_WillUpload) ru(i.key, i.text)
            }
        }
//        uploadImages()
//        ToolsThreads.thread {
//            ToolsThreads.sleep(5000)
//            addImagePasswords()
//        }
        migrateToFirebase()
    }

    private fun migrateToFirebase() {
        val total = Database.select("migrateToFirebase 1", SqlQuerySelect(TAccountsEmails.NAME, Sql.COUNT))
            .nextLongOrZero()

        info("[ControllerMigrations] starting migration to firebase email accounts. $total accounts to migrate")
        var offset = 0
        var v: ResultRows
        val auth = FirebaseAuth.getInstance(ControllerFirebase.app)
        while (true) {
            v = Database.select("migrateToFirebase 2", SqlQuerySelect(TAccountsEmails.NAME, TAccountsEmails.account_id, TAccountsEmails.account_email, TAccountsEmails.account_password)
                .offset_count(offset, 500))
            if (v.isEmpty) break

            offset += v.rowsCount

            val import = mutableListOf<ImportUserRecord>()

            while (v.hasNext()) {
                val accountId = v.next<Long>()
                val accountEmail = v.next<String>()
                val accountPwd = v.next<String>()

                import.add(ImportUserRecord.builder()
                    .setUid("migrated$accountId")
                    .setEmail(accountEmail)
                    .setPasswordHash(accountPwd.toByteArray())
                    .setEmailVerified(true) // or they're gonna get deleted
                    .putCustomClaim("migratedFrom", accountId)
                    .build())

                try {
                    ControllerFirebase.setUid(accountId, "migrated$accountId")
                } catch (e: Exception) {
                    err("[ControllerMigrator] setUid failed for $accountId")
                }
            }

            val result = auth.importUsers(import, UserImportOptions.withHash(Bcrypt.getInstance()))
            for (error in result.errors) {
                err("[ControllerMigrator] failed to migrate user at index ${error.index}: ${error.reason}")
            }
            info("[ControllerMigrator] migrated ${result.successCount} users, ${result.failureCount} failures")
        }
        info("[ControllerMigrator] migration done! please don't forget to remove this from ControllerMigrator")
    }

    fun addImagePasswords() {
        val total = Database.select("addImagePasswords count", SqlQuerySelect(TPublications.NAME, Sql.COUNT)
            .where(TPublications.publication_type, "=", API.PUBLICATION_TYPE_CHAT_MESSAGE)).nextLongOrZero()
        var offset = 0
        while (true) {
            val start = System.currentTimeMillis()

            val array = ControllerPublications.parseSelect(Database.select("addImagePasswords select", ControllerPublications.instanceSelect(1)
                .where(TPublications.publication_type, "=", API.PUBLICATION_TYPE_CHAT_MESSAGE)
                .offset_count(offset, 100)))
            if (array.isEmpty()) break
            offset += array.size

            for (publication in array) {
                if (publication !is PublicationChatMessage) continue
                if (publication.resourceId > 0 && publication.imagePwd.isEmpty()) {
                    publication.imagePwd = ToolsCryptography.generateString(10)
                    ControllerResources.setPwd(publication.resourceId, publication.imagePwd)
                    ControllerPublications.replaceJson(publication.id, publication)
                } else if (publication.imageIdArray.isNotEmpty() && publication.imagePwdArray.isEmpty()) {
                    publication.imagePwdArray = Array(publication.imageIdArray.size) { ToolsCryptography.generateString(10) }
                    for (i in publication.imageIdArray.indices) {
                        ControllerResources.setPwd(publication.imageIdArray[i], publication.imagePwdArray[i])
                    }
                    ControllerPublications.replaceJson(publication.id, publication)
                }
            }

            info("[ControllerMigrator] progress: $offset / $total in ${System.currentTimeMillis() - start}ms " +
                    "| 600ms cooldown")
            ToolsThreads.sleep(600)
        }
        info("[ControllerMigrator] done!")
    }

    fun ru(key: String, text: String) {
        x(API.LANGUAGE_RU, key, text)
    }

    fun x(languageId: Long, key: String, text: String) {
        info("Upload languageId[$languageId] key[$key], text[$text]")
        val v = Database.select(
            "xxx", SqlQuerySelect(TTranslates.NAME, TTranslates.id)
                .where(TTranslates.language_id, "=", languageId)
                .whereValue(TTranslates.translate_key, "=", key)
        )
        if (!v.isEmpty) return
        Database.insert(
            "xxx", TTranslates.NAME,
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
        val total = Database.select(
            "indexImages count", SqlQuerySelect(TPublications.NAME, Sql.COUNT)
                .where(TPublications.publication_type, "=", API.PUBLICATION_TYPE_POST)
        ).nextLongOrZero()
        var offset = 0
        while (true) {

            val array = ControllerPublications.parseSelect(
                Database.select(
                    "indexImages select", ControllerPublications.instanceSelect(1)
                        .where(TPublications.publication_type, "=", API.PUBLICATION_TYPE_POST)
                        .offset_count(offset, 100)
                )
            )
            if (array.isEmpty()) return
            offset += array.size

            for (publication in array) {
                val ids = publication.getResourcesList()
                if (ids.isEmpty()) continue
                db.update(
                    SqlQueryUpdate("resources")
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
        /*val files = File(App.secretsConfig.getString("patch_prefix"))
            .resolve("upload/")
        val list = arrayOf("bg_lvl_16.png", "bg_lvl_17.png", "bg_lvl_18.png", "bg_lvl_19.png", "bg_lvl_20.png")
        for (name in list) {
            val id = ControllerResources.putTag(ToolsFiles.readFile(files.resolve(name)), 0, "bg")
            System.err.println("[uploadImages] $name | id: $id")
        }*/
    }

}
