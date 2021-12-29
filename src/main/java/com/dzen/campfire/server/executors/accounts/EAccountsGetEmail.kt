package com.dzen.campfire.server.executors.accounts

import com.dzen.campfire.api.requests.accounts.RAccountsGetEmail
import com.dzen.campfire.server.tables.TAccounts
import com.dzen.campfire.server.tables.TAccountsEmails
import com.sup.dev.java_pc.sql.Database
import com.sup.dev.java_pc.sql.SqlQuerySelect

class EAccountsGetEmail : RAccountsGetEmail() {

    override fun check() {

    }

    override fun execute(): Response {

        var v = Database.select("EAccountsGetEmail select1", SqlQuerySelect(TAccountsEmails.NAME, TAccountsEmails.account_email)
                .where(TAccountsEmails.account_id, "=", apiAccount.id))

        var email = ""
        if(v.hasNext()) email = v.next()

        v = Database.select("EAccountsGetEmail select12", SqlQuerySelect(TAccounts.NAME, TAccounts.google_id)
                .where(TAccounts.id, "=", apiAccount.id))

        var googleId = ""
        if(v.hasNext()) googleId = v.next()

        return Response(email, googleId)
    }

}