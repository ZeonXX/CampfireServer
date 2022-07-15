package com.dzen.campfire.server.executors.quests

import com.dzen.campfire.api.API
import com.dzen.campfire.api.models.quests.QuestPart
import com.dzen.campfire.api.models.quests.QuestPartUnknown
import com.dzen.campfire.api.requests.quests.RQuestsChangePart
import com.dzen.campfire.api.tools.ApiException
import com.dzen.campfire.server.controllers.ControllerAccounts
import com.dzen.campfire.server.controllers.ControllerUserQuests
import com.dzen.campfire.server.tables.TQuestParts
import com.sup.dev.java.libs.json.Json
import com.sup.dev.java_pc.sql.Database
import com.sup.dev.java_pc.sql.SqlQuerySelect
import com.sup.dev.java_pc.sql.SqlQueryUpdate
import kotlin.properties.Delegates

class EQuestsChangePart : RQuestsChangePart(0, QuestPartUnknown()) {
    private var questId by Delegates.notNull<Long>()

    override fun check() {
        ControllerAccounts.checkAccountBanned(apiAccount.id)

        val qp = Database.select("EQuestsChangePart_check", SqlQuerySelect(TQuestParts.NAME, TQuestParts.unit_id)
            .where(TQuestParts.id, "=", partId))
        if (qp.isEmpty) throw ApiException(API.ERROR_GONE)

        questId = qp.next()
        checkQuestEditable(questId, apiAccount)
    }

    override fun execute(): Response {
        val oldPartText = Database.select("EQuestsChangePart_oldPart", SqlQuerySelect(TQuestParts.NAME, TQuestParts.json_db)
            .where(TQuestParts.id, "=", partId))
            .next<String>()
        val oldPart = QuestPart.instance(Json(oldPartText))
        ControllerUserQuests.partClean(oldPart)

        ControllerUserQuests.censorAndUploadPart(questId, part)
        val newPartText = part.json(true, Json()).toString()
        Database.update("EQuestsChangePart_change", SqlQueryUpdate(TQuestParts.NAME)
            .where(TQuestParts.id, "=", partId)
            .updateValue(TQuestParts.json_db, newPartText))

        return Response(part)
    }
}