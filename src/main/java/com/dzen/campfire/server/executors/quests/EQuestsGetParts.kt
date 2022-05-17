package com.dzen.campfire.server.executors.quests

import com.dzen.campfire.api.API
import com.dzen.campfire.api.models.quests.QuestPart
import com.dzen.campfire.api.requests.quests.RQuestsGetParts
import com.dzen.campfire.api.requests.quests.RQuestsModify
import com.dzen.campfire.api.tools.ApiException
import com.dzen.campfire.server.tables.TPublications
import com.dzen.campfire.server.tables.TQuestParts
import com.sup.dev.java.libs.json.Json
import com.sup.dev.java_pc.sql.Database
import com.sup.dev.java_pc.sql.SqlQuerySelect

class EQuestsGetParts : RQuestsGetParts(0) {
    override fun check() {
        val v = Database.select("EQuestsGetParts check", SqlQuerySelect(
            TPublications.NAME, TPublications.status, TPublications.creator_id
        ).where(TPublications.id, "=", id))
        val status = v.next<Long>()
        val creator = v.next<Long>()
        if (status != API.STATUS_PUBLIC && (creator != apiAccount.id && status != API.STATUS_DRAFT))
            throw ApiException(API.ERROR_ACCESS, "not public")
    }

    override fun execute(): Response {
        val parts = Database.select(
            "EQuestsGetParts execute",
            SqlQuerySelect(TQuestParts.NAME, TQuestParts.id, TQuestParts.json_db)
                .where(TQuestParts.unit_id, "=", id)
        )
        val ret = arrayListOf<QuestPart>()
        while (parts.hasNext()) {
            val id = parts.next<Long>()
            val json = Json(parts.next<String>())
            val part = QuestPart.instance(json)
            part.id = id
            ret.add(part)
        }
        return Response(ret.toTypedArray())
    }
}