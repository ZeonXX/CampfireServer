package com.dzen.campfire.server.executors.activities

import com.dzen.campfire.api.API
import com.dzen.campfire.api.requests.activities.RActivitiesRelayGetRejected
import com.dzen.campfire.server.controllers.ControllerAccounts
import com.dzen.campfire.server.tables.TActivitiesCollisions
import com.sup.dev.java_pc.sql.Database
import com.sup.dev.java_pc.sql.SqlQuerySelect

class EActivitiesRelayGetRejected : RActivitiesRelayGetRejected(0, 0) {

    override fun check() {

    }

    override fun execute(): Response {
        val v = Database.select("EActivitiesRelayGetRejected", SqlQuerySelect(TActivitiesCollisions.NAME, TActivitiesCollisions.account_id)
                .where(TActivitiesCollisions.type, "=", API.ACTIVITIES_COLLISION_TYPE_RELAY_RACE_REJECTED)
                .where(TActivitiesCollisions.activity_id, "=", userActivityId)
                .sort(TActivitiesCollisions.date_create, false)
        )

        if(v.isEmpty) return Response(emptyArray())

        return Response(ControllerAccounts.getAccounts(Array(v.rowsCount){v.next<Long>()}))
    }


}