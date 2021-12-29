package com.dzen.campfire.server.executors.fandoms

import com.dzen.campfire.api.API
import com.dzen.campfire.api.models.account.Account
import com.dzen.campfire.api.models.fandoms.Fandom
import com.dzen.campfire.api.requests.fandoms.RFandomsSuggestedGet
import com.dzen.campfire.server.controllers.ControllerAccounts
import com.dzen.campfire.server.controllers.ControllerFandom
import com.dzen.campfire.server.tables.TAccounts
import com.dzen.campfire.api.tools.ApiException

class EFandomsSuggestedGet : RFandomsSuggestedGet(0) {

    private var fandom: Fandom? = null

    @Throws(ApiException::class)
    override fun check() {

    }

    @Throws(ApiException::class)
    override fun execute(): Response {
        fandom = ControllerFandom.getFandom(fandomId)
        if (fandom == null) throw ApiException(API.ERROR_GONE)

        val creator = ControllerAccounts.getAccount(fandom!!.creatorId)?: Account()


        return Response(
                fandom!!,
                creator,
                ControllerFandom.getParams(fandomId, 1),
                ControllerFandom.getParams(fandomId, 2),
                ControllerFandom.getParams(fandomId, 3),
                ControllerFandom.getParams(fandomId, 4)
        )
    }

}
