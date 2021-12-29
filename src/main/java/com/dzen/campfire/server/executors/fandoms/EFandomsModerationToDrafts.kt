package com.dzen.campfire.server.executors.fandoms

import com.dzen.campfire.api.API
import com.dzen.campfire.api.models.notifications.post.NotificationModerationToDraft
import com.dzen.campfire.api.models.publications.Publication
import com.dzen.campfire.api.models.publications.history.HistoryAdminBackDraft
import com.dzen.campfire.api.models.publications.moderations.posts.ModerationToDrafts
import com.dzen.campfire.api.requests.fandoms.RFandomsModerationToDrafts
import com.dzen.campfire.server.controllers.*
import com.dzen.campfire.server.tables.TAccounts
import com.dzen.campfire.server.tables.TPublications
import com.dzen.campfire.api.tools.ApiException
import com.sup.dev.java_pc.sql.Database
import com.sup.dev.java_pc.sql.SqlQueryUpdate

class EFandomsModerationToDrafts : RFandomsModerationToDrafts(0, "") {

    var publication: Publication? = null

    override fun check() {
        publication = ControllerPublications.getPublication(publicationId, apiAccount.id)
        if (publication == null) throw ApiException(API.ERROR_GONE)
        if (publication!!.publicationType != API.PUBLICATION_TYPE_POST) throw ApiException(E_BAD_TYPE)
        if (publication!!.status == API.STATUS_DRAFT) throw ApiException(E_ALREADY)
        if (publication!!.status != API.STATUS_PUBLIC) throw ApiException(E_BLOCKED)


        ControllerFandom.checkCan(apiAccount, publication!!.fandom.id, publication!!.fandom.languageId, API.LVL_MODERATOR_TO_DRAFTS)
        if (!ControllerFandom.checkCanModerate(apiAccount, publication!!.creator.id, publication!!.fandom.id, publication!!.fandom.languageId)) throw ApiException(E_LOW_KARMA_FORCE)
        ControllerModeration.parseComment(comment)
    }

    override fun execute(): Response {

        val update = SqlQueryUpdate(TPublications.NAME)
                .where(TPublications.id, "=", publicationId)
                .update(TPublications.status, API.STATUS_DRAFT)
        if(publication!!.fandom.languageId < 1){
            if(publication!!.tag_5 < 1)  throw ApiException(API.ERROR_ACCESS)
            update.update(TPublications.language_id, publication!!.tag_5)
        }
        Database.update("EFandomsModerationToDrafts", update)


        val v = ControllerAccounts.get(publication!!.creator.id, TAccounts.name, TAccounts.img_id)
        val creatorName = v.next<String>()
        val creatorImageId = v.next<Long>()

        val moderationId = ControllerPublications.moderation(
                ModerationToDrafts(comment, publicationId, publication!!.publicationType, publication!!.creator.id, creatorName, creatorImageId),
                apiAccount.id, publication!!.fandom.id, publication!!.fandom.languageId, publication!!.id)


        ControllerNotifications.push(publication!!.creator.id, NotificationModerationToDraft(comment, publication!!.fandom.imageId, moderationId, apiAccount.sex, apiAccount.name, ControllerPublications.getMaskText(publication!!), ControllerPublications.getMaskPageType(publication!!)))
        ControllerPublicationsHistory.put(publicationId, HistoryAdminBackDraft(apiAccount.id, apiAccount.imageId, apiAccount.name, comment))

        return Response()
    }


}