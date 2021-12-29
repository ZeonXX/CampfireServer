package com.dzen.campfire.server.tables

import com.sup.dev.java_pc.sql.Sql

object TCollisions {

    val NAME = "collisions"
    val id = "id"
    val owner_id = "owner_id"
    val collision_type = "collision_type"
    val collision_id = "collision_id"
    val collision_sub_id = "collision_sub_id"
    val collision_key = "collision_key"
    val collision_date_create = "collision_date_create"
    val value_1 = "value_1" //  Long
    val value_2 = "value_2" //  Text
    val value_3 = "value_3" //  Long
    val value_4 = "value_4" //  Long
    val value_5 = "value_5" //  Long

    val FANDOM_IMAGE_ID = Sql.IFNULL("(SELECT ${TFandoms.image_id} FROM ${TFandoms.NAME} WHERE ${TFandoms.id}=$collision_id)", 0)
    val FANDOM_NAME = Sql.IFNULL("(SELECT ${TFandoms.name} FROM ${TFandoms.NAME} WHERE ${TFandoms.id}=$collision_id)", "''")


}