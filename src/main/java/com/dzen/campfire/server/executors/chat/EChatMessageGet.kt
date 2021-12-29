package com.dzen.campfire.server.executors.chat

import com.dzen.campfire.api.API
import com.dzen.campfire.api.models.chat.ChatTag
import com.dzen.campfire.api.models.publications.chat.PublicationChatMessage
import com.dzen.campfire.api.requests.chat.RChatMessageGet
import com.dzen.campfire.server.controllers.ControllerPublications
import com.dzen.campfire.server.tables.TPublications
import com.dzen.campfire.api.tools.ApiException
import com.sup.dev.java_pc.sql.Database

class EChatMessageGet : RChatMessageGet(ChatTag(), 0) {

    override fun check() {
        tag.setMyAccountId(apiAccount.id)
    }

    override fun execute(): Response {
        val publications = ControllerPublications.parseSelect(Database.select("EChatMessageGet",
                ControllerPublications.instanceSelect(apiAccount.id)
                        .where(TPublications.id, "=", messageId)
                        .where(TPublications.tag_1, "=", tag.chatType)
                        .where(TPublications.fandom_id, "=", tag.targetId)
                        .where(TPublications.language_id, "=", tag.targetSubId)
                        .where(TPublications.publication_type, "=", API.PUBLICATION_TYPE_CHAT_MESSAGE)
        ))

        if (publications.isEmpty()) throw ApiException(API.ERROR_GONE, GONE_REMOVE)

        val publication = publications[0] as PublicationChatMessage

        if (publication.status == API.STATUS_BLOCKED || publication.status == API.STATUS_DEEP_BLOCKED)
            throw ApiException.instance(API.ERROR_GONE, GONE_BLOCKED, ControllerPublications.getModerationIdForPublication(messageId))
        if (publication.status == API.STATUS_REMOVED) throw ApiException(API.ERROR_GONE, GONE_REMOVE)
        if (publication.status != API.STATUS_PUBLIC) throw ApiException(API.ERROR_GONE)

        ControllerPublications.loadSpecDataForPosts(apiAccount.id, arrayOf(publication))

        return Response(publication)
    }

}