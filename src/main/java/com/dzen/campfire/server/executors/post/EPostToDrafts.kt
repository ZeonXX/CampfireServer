package com.dzen.campfire.server.executors.post

import com.dzen.campfire.api.API
import com.dzen.campfire.api.models.publications.history.HistoryBackDraft
import com.dzen.campfire.api.models.publications.post.PublicationPost
import com.dzen.campfire.api.requests.post.RPostToDrafts
import com.dzen.campfire.server.controllers.ControllerAccounts
import com.dzen.campfire.server.controllers.ControllerPublications
import com.dzen.campfire.server.controllers.ControllerPublicationsHistory
import com.dzen.campfire.server.tables.TPublications
import com.dzen.campfire.api.tools.ApiException
import com.sup.dev.java_pc.sql.Database
import com.sup.dev.java_pc.sql.SqlQueryUpdate

class EPostToDrafts : RPostToDrafts(0) {

    private var publication = PublicationPost()

    @Throws(ApiException::class)
    override fun check() {

        val publicationX = ControllerPublications.getPublication(publicationId, apiAccount.id)
        if(publicationX == null || publicationX !is PublicationPost)  throw ApiException(API.ERROR_GONE)
        publication = publicationX


        if (publication.status != API.STATUS_PUBLIC && publication.status != API.STATUS_PENDING) throw ApiException(E_BAD_STATUS)
        if (publication.publicationType != API.PUBLICATION_TYPE_POST) throw ApiException(E_BAD_TYPE)
        if (publication.creator.id != apiAccount.id) throw ApiException(API.ERROR_ACCESS)
    }

    override fun execute(): Response {
        val update = SqlQueryUpdate(TPublications.NAME)
                .where(TPublications.id, "=", publication.id)
                .update(TPublications.status, API.STATUS_DRAFT)

        if (publication.fandom.languageId < 1) {
            if (publication.tag_5 < 1) throw ApiException(API.ERROR_ACCESS)
            update.update(TPublications.language_id, publication.tag_5)
        }

        Database.update("EPostToDrafts update", update)
        ControllerAccounts.updatePostsCount(apiAccount.id, -1)
        ControllerPublicationsHistory.put(publicationId, HistoryBackDraft(apiAccount.id, apiAccount.imageId, apiAccount.name))

        return Response()
    }
}
