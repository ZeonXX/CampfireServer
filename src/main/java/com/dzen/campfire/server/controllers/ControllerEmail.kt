package com.dzen.campfire.server.controllers

import com.dzen.campfire.server.tables.TAccountsEmails
import com.sup.dev.java_pc.sql.Database
import com.sup.dev.java_pc.sql.SqlQuerySelect
import com.sup.dev.java_pc.sql.SqlQueryUpdate

object ControllerEmail {

    fun checkExist(email:String):Boolean{
        return Database.select("ControllerEmail.checkExist", SqlQuerySelect(TAccountsEmails.NAME, TAccountsEmails.id)
                .whereValue(TAccountsEmails.account_email, "=", email)
        ).hasNext()
    }

    fun getAccountId(email:String, password:String):Long{
        return Database.select("ControllerEmail.getAccountId", SqlQuerySelect(TAccountsEmails.NAME, TAccountsEmails.account_id)
                .whereValue(TAccountsEmails.account_email, "=", email)
                .whereValue(TAccountsEmails.account_password, "=", password)
        ).nextLongOrZero()
    }

    fun insert(accountId:Long, email:String, password:String){
        Database.insert("ControllerEmail.insert", TAccountsEmails.NAME,
                TAccountsEmails.account_id, accountId,
                TAccountsEmails.account_email, email,
                TAccountsEmails.account_password, password,
                TAccountsEmails.date_create, System.currentTimeMillis())

    }

    fun setPassword(accountId:Long, email:String, password:String){
        Database.update("ControllerEmail.setPassword", SqlQueryUpdate(TAccountsEmails.NAME)
                .where(TAccountsEmails.account_id, "=", accountId)
                .whereValue(TAccountsEmails.account_email, "=", email)
                .updateValue(TAccountsEmails.account_password, password)
        )

    }

}