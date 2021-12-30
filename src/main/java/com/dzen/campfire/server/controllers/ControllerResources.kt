package com.dzen.campfire.server.controllers

import com.dzen.campfire.api_media.requests.*
import com.dzen.campfire.server.app.App
import com.dzen.campfire.server.tables.TPublications
import com.dzen.campfire.server.tables.TResources
import com.sup.dev.java_pc.sql.*

object ControllerResources {

    val database = DatabasePool("sayzen_campfire", "SayzenSTK2012", "campfire_db_media", if(App.test) "46.254.16.245"  else "127.0.0.1", 8)

    //
    //  Methods
    //

    fun sizeOfTable():Long{
        return database.select("ControllerResources.size",SqlQuerySelect(
            TResources.NAME, Sql.COUNT)).next()!!
    }

    fun removeAndPut(resourceId: Long, resource: ByteArray, publicationId:Long): Long {
        remove(resourceId)
        return put(resource, publicationId)
    }

    fun replace(resourceId: Long, resource: ByteArray, publicationId:Long): Long {
        if (resourceId == 0L) return put(resource, publicationId)

        if (checkExist(resourceId)) {
            database.update("EResourcesReplace", SqlQueryUpdate(TResources.NAME)
                    .where(TResources.id, "=", resourceId)
                    .updateValue(TResources.image_bytes, resource))
        } else {
            RResourcesPut.Response(database.insert("EResourcesPut 2", TResources.NAME,
                    TResources.image_bytes, resource,
                    TResources.publication_id, publicationId,
                    TResources.size, resource.size,
                    TResources.id, resourceId))
        }

        return resourceId
    }

    fun put(resource: ByteArray?, publicationId:Long): Long {
       return database.insert("EResourcesPut 1", TResources.NAME,
               TResources.image_bytes, resource,
               TResources.publication_id, publicationId,
               TResources.size, resource?.size ?: 0
       )
    }

    //
    //  Remove
    //

    fun remove(resourceId: Long) {
        if (resourceId < 1) return
        database.remove("EResourcesRemove", SqlQueryRemove(TResources.NAME)
                .where(TResources.id, "=", resourceId))
    }

    //
    //  Checkers
    //

    fun checkExist(resourceId: Long): Boolean {
        return !database.select("EResourcesCheckExist", SqlQuerySelect(TResources.NAME, TResources.id)
                .where(TResources.id, "=", resourceId)).isEmpty
    }

}