package com.dzen.campfire.server.executors.activities

import com.dzen.campfire.api.API
import com.dzen.campfire.api.requests.activities.RActivitiesRelayRaceMember
import com.dzen.campfire.server.controllers.ControllerAccounts
import com.dzen.campfire.server.controllers.ControllerActivities
import com.dzen.campfire.api.tools.ApiException

class EActivitiesRelayRaceMember : RActivitiesRelayRaceMember(0, false) {

    override fun check() {
        ControllerAccounts.checkAccountBanned(apiAccount.id)
    }

    override fun execute(): Response {

        val activity = ControllerActivities.getActivity(activityId, apiAccount.id)
        if (activity == null) throw ApiException(API.ERROR_GONE)

        ControllerActivities.removeMember(apiAccount.id, activityId)
        var myIsCurrentMember = false
        if (member) {
            ControllerActivities.addMember(apiAccount.id, activityId)
            if (activity.currentAccount.id < 1 || System.currentTimeMillis() > activity.tag_2 + API.ACTIVITIES_RELAY_RACE_TIME) {
                ControllerActivities.makeCurrentMember(apiAccount.id, activity)
                myIsCurrentMember = true
            }
            val accountSettings = ControllerAccounts.getSettings(apiAccount.id)
            if (accountSettings.userActivitiesAutoSubscribe) ControllerActivities.setSubscribe(apiAccount.id, activityId)
        } else {
            ControllerActivities.removeSubscribe(apiAccount.id, activityId)
        }

        return Response(myIsCurrentMember)
    }


}
