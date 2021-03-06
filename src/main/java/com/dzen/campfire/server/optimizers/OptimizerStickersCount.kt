package com.dzen.campfire.server.optimizers

import com.dzen.campfire.server.controllers.ControllerStickers

object OptimizerStickersCount {

    val cash = HashMap<Long, Long>()

    fun get(accountId: Long):Long {
        var count:Long?
        synchronized(cash) {
            count = cash[accountId]
        }
        if (count == null) {
            count = ControllerStickers.getStickersCount(accountId)
            synchronized(cash) {
                cash[accountId] = count?:0L
            }
        }
        return count?:0L
    }

    fun increment(accountId: Long) {
        synchronized(OptimizerRatesCount.cash) {
            OptimizerRatesCount.cash[accountId] = (OptimizerRatesCount.cash[accountId]?:0L) + 1
        }
    }

    fun decrement(accountId: Long) {
        synchronized(OptimizerRatesCount.cash) {
            OptimizerRatesCount.cash[accountId] = (OptimizerRatesCount.cash[accountId]?:0L) - 1
        }
    }
}