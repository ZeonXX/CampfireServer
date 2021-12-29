package com.dzen.campfire.server.executors.accounts

import com.dzen.campfire.api.API
import com.dzen.campfire.api.models.account.Account
import com.dzen.campfire.api.models.account.AccountSettings
import com.dzen.campfire.api.requests.accounts.RAccountsLogin
import com.dzen.campfire.server.controllers.*
import com.sup.dev.java.libs.json.Json
import java.lang.Exception

class EAccountsLogin : RAccountsLogin("", 0) {

    private var account: Account? = null


    override fun check() {

    }

    override fun execute(): Response {

        loadAccount()
        ControllerOptimizer.insertEnter(apiAccount.id)

        updateEnter()
        ControllerAccounts.addNotificationToken(apiAccount.id, tokenNotification)
        ControllerSubThread.inSub("EAccountsLogin") {
            if (languageId > 0) ControllerProjects.initAccountForProject(apiAccount, languageId, requestProjectKey)
        }

        val accountSettings = AccountSettings()
        try {
            accountSettings.json(false, Json(apiAccount.settings))
        } catch (e: Exception) {
        }

        return Response(API.VERSION, API.SUPPORTED_VERSION, arrayOf("0.82b", "0.83b", "0.84b", "0.841b", "0.850b", "0.851b", "0.860b", "0.861b"), API.PROTOADMINS, account, accountSettings, apiAccount.tag_s_1.isNotEmpty(),
                languageId, ControllerServerTranslates.getMap(languageId),
                ControllerServerTranslates.getMap(API.LANGUAGE_EN)
        )
    }

    private fun loadAccount() {
        if (apiAccount.id < 1) return
        account = ControllerAccounts.instance(apiAccount.id, apiAccount.accessTag, System.currentTimeMillis(), apiAccount.name, apiAccount.imageId, apiAccount.sex, apiAccount.accessTagSub)
    }

    private fun updateEnter() {
        val last = ControllerOptimizer.getCollisionNullable(apiAccount.id, API.COLLISION_ACCOUNT_LAST_DAILY_ENTER_DATE) ?: 0
        if (last + 1000L * 60 * 60 * 24 < System.currentTimeMillis()) {
            ControllerOptimizer.updateOrCreateCollision(apiAccount.id, API.COLLISION_ACCOUNT_LAST_DAILY_ENTER_DATE, System.currentTimeMillis())
            ControllerAccounts.updateEnters(apiAccount.id, 1)
            ControllerAchievements.addAchievementWithCheck(apiAccount.id, API.ACHI_ENTERS)
        }
    }


}
