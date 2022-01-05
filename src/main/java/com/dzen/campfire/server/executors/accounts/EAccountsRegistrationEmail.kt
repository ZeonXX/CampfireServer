package com.dzen.campfire.server.executors.accounts

import com.dzen.campfire.api.API
import com.dzen.campfire.api.models.chat.ChatTag
import com.dzen.campfire.api.requests.accounts.RAccountsRegistration
import com.dzen.campfire.api.requests.accounts.RAccountsRegistrationEmail
import com.dzen.campfire.server.controllers.ControllerAccounts
import com.dzen.campfire.server.controllers.ControllerChats
import com.dzen.campfire.server.controllers.ControllerResources
import com.dzen.campfire.server.tables.TAccounts
import com.dzen.campfire.api.tools.ApiException
import com.dzen.campfire.server.controllers.ControllerEmail
import com.dzen.campfire.server.tables.TAccountsEmails
import com.sup.dev.java.tools.ToolsFiles
import com.sup.dev.java.tools.ToolsText
import com.sup.dev.java_pc.google.GoogleAuth
import com.sup.dev.java_pc.sql.Database
import com.sup.dev.java_pc.sql.SqlQueryUpdate
import com.sup.dev.java_pc.tools.ToolsImage

class EAccountsRegistrationEmail : RAccountsRegistrationEmail("", "", 0) {
    @Throws(ApiException::class)
    override fun check() {
        throw ApiException(E_EMAIL_EXIST)   //  Отключена регистрация по Email из-за простоты создания ботов
        if (!ToolsText.isValidEmailAddress(email)) throw RuntimeException("Invalid email [$email]")
        if(ControllerEmail.checkExist(email)) throw ApiException(E_EMAIL_EXIST)
    }

    override fun execute(): Response {

        var image = ToolsFiles.readFileSalient("campfire/res/def_image.png")
        if(image == null) image = ToolsFiles.readFileSalient("CampfireServer/res/def_image.png")
        val imgId = ControllerResources.put(image!!, API.RESOURCES_PUBLICATION_DATABASE_LINKED)

        val accountId = Database.insert("EAccountsRegistrationEmail insert", TAccounts.NAME,
                TAccounts.google_id, "",
                TAccounts.date_create, System.currentTimeMillis(),
                TAccounts.name, System.currentTimeMillis(),
                TAccounts.img_id, imgId,
                TAccounts.last_online_time, System.currentTimeMillis(),
                TAccounts.subscribes, "",
                TAccounts.refresh_token, "",
                TAccounts.refresh_token_date_create, 0L,
                TAccounts.account_settings, ""
        )

        ControllerEmail.insert(accountId, email, password)

        Database.update("EAccountsRegistrationEmail update", SqlQueryUpdate(TAccounts.NAME)
                .updateValue(TAccounts.name, "user#$accountId")
                .where(TAccounts.id, "=", accountId))

        return Response(accountId, imgId)
    }

}
