package com.dzen.campfire.server.controllers

object ControllerUserQuests {
    private fun checkEffect(effect: QuestEffect): Boolean {
        when (effect) {
            is QuestEffectVibrate -> {
                if (effect.times < 0 || effect.times > API.QUEST_EFFECT_VIBRATE_COUNT_MAX)
                    return false
                if (effect.length < 0 || effect.length > API.QUEST_EFFECT_VIBRATE_LENGTH_MAX)
                    return false
                if (effect.delayStart < 0 || effect.delayStart > API.QUEST_EFFECT_VIBRATE_DELAY_START_MAX)
                    return false
                if (effect.delayBetween < 0 || effect.delayBetween > API.QUEST_EFFECT_VIBRATE_DELAY_BETWEEN_MAX)
                    return false
            }
            is QuestEffectBox -> {
                if (!effect.box.link.startsWith("box_")) return false
            }
            is QuestEffectBoxReset -> {
                // no fields
            }
            else -> return false
        }
        return true
    }
    private fun checkEffects(effects: Array<QuestEffect>): Boolean {
        val visited = mutableSetOf<Long>()
        for (effect in effects) {
            if (visited.contains(effect.getEffectType())) return false
            if (!checkEffect(effect)) return false
            visited.add(effect.getEffectType())
        }
        return true
    }

    private fun checkInput(details: QuestDetails, input: QuestInput): Boolean {
        if (input.hint.length > API.QUEST_INPUT_HINT_MAX_L) return false
        if (!checkVariableValue(input.type, input.defaultValue)) return false
        if (details.variables.indexOfFirst { it.id == input.varId } == -1) return false
        return true
    }
    private fun checkInputs(details: QuestDetails, inputs: Array<QuestInput>): Boolean {
        if (inputs.size > API.QUEST_TEXT_INPUTS_MAX) return false
        for (input in inputs) if (!checkInput(details, input)) return false
        return true
    }

    private fun checkButton(button: QuestButton): Boolean {
        if (button.label.length > API.QUEST_BUTTON_LABEL_MAX_L) return false
        if (!API.QUEST_BUTTON_COLORS.contains(button.color)) return false
        if (button.jumpToId < -2) return false
        return true
    }
    private fun checkButtons(buttons: Array<QuestButton>): Boolean {
        if (buttons.size > API.QUEST_TEXT_BUTTONS_MAX) return false
        for (button in buttons) if (!checkButton(button)) return false
        return true
    }

    private fun checkVariableValue(type: Long, value: String): Boolean {
        if (value.length > API.QUEST_VARIABLE_MAX_VALUE_L) return false
        when (type) {
            API.QUEST_TYPE_TEXT   -> {}
            API.QUEST_TYPE_NUMBER -> if (value.toLongOrNull() == null) return false
            API.QUEST_TYPE_BOOL   -> if (!setOf("0", "1", "").contains(value)) return false
            else -> return false
        }
        return true
    }

    private fun checkQuestAction(details: QuestDetails, part: QuestPartAction): Boolean {
        val variable = details.variables.find { it.id == part.varId } ?: return false
        when (part.actionType) {
            API.QUEST_ACTION_SET_LITERAL ->
                if (!checkVariableValue(variable.type, part.sArg)) return false
            API.QUEST_ACTION_SET_RANDOM -> {
                if (variable.type != API.QUEST_TYPE_NUMBER) return false
                if (part.lArg1 >= part.lArg2) return false
            }
            API.QUEST_ACTION_SET_ANOTHER -> {
                val var2 = details.variables.find { it.id == part.lArg1 } ?: return false
                if (variable.type != var2.type) return false
            }
            API.QUEST_ACTION_ADD_LITERAL -> {
                if (variable.type != API.QUEST_TYPE_NUMBER) return false
            }
            API.QUEST_ACTION_ADD_ANOTHER -> {
                val var2 = details.variables.find { it.id == part.lArg1 } ?: return false
                if (variable.type != API.QUEST_TYPE_NUMBER) return false
                if (var2.type != API.QUEST_TYPE_NUMBER) return false
            }
            API.QUEST_ACTION_SET_ARANDOM -> {
                val var2 = details.variables.find { it.id == part.lArg1 } ?: return false
                val var3 = details.variables.find { it.id == part.lArg2 } ?: return false
                if (variable.type != API.QUEST_TYPE_NUMBER) return false
                if (var2.type != API.QUEST_TYPE_NUMBER) return false
                if (var3.type != API.QUEST_TYPE_NUMBER) return false
            }
            else -> return false
        }
        return true
    }

    private fun checkQuestCondition(details: QuestDetails, part: QuestPartCondition): Boolean {
        fun condToType(cv: QuestConditionValue): Long? = when (cv.type) {
            API.QUEST_CONDITION_VALUE_LITERAL_LONG -> API.QUEST_TYPE_NUMBER
            API.QUEST_CONDITION_VALUE_LITERAL_TEXT -> API.QUEST_TYPE_TEXT
            API.QUEST_CONDITION_VALUE_LITERAL_BOOL -> API.QUEST_TYPE_BOOL
            API.QUEST_CONDITION_VALUE_VAR -> details.variables.find { it.id == cv.value }?.type
            else -> null
        }
        val leftType = condToType(part.leftValue)
        val rightType = condToType(part.leftValue)
        when (part.cond) {
            API.QUEST_CONDITION_EQ, API.QUEST_CONDITION_NEQ -> {
                if (leftType != rightType) return false
            }
            API.QUEST_CONDITION_LESS, API.QUEST_CONDITION_LEQ,
            API.QUEST_CONDITION_GEQ, API.QUEST_CONDITION_GREATER -> {
                if (leftType != API.QUEST_TYPE_NUMBER || rightType != API.QUEST_TYPE_NUMBER)
                    return false
            }
            else -> return false
        }

        if (part.falseJumpId < -1 || part.trueJumpId < -1) return false
        return true
    }

    fun checkPart(details: QuestDetails, part: QuestPart): Boolean {
        if (part.devLabel.length > API.QUEST_DEV_LABEL_MAX_L) return false
        when (part) {
            is QuestPartText -> {
                if (part.title.length > API.QUEST_TEXT_TITLE_MAX_L) return false
                if (part.text.length > API.QUEST_TEXT_TEXT_MAX_L) return false
                if (!checkInputs(details, part.inputs)) return false
                if (!checkButtons(part.buttons)) return false
                if (!checkEffects(part.effects)) return false
            }
            is QuestPartAction -> {
                if (!checkQuestAction(details, part)) return false
            }
            is QuestPartCondition -> {
                if (!checkQuestCondition(details, part)) return false
            }
            else -> return false
        }
        return true
    }

    fun partClean(part: QuestPart) {
        when (part) {
            is QuestPartText -> {
                ControllerResources.remove(part.imageId)
                ControllerResources.remove(part.gifId)
            }
        }
    }

    fun censorAndUploadPart(questId: Long, part: QuestPart) {
        when (part) {
            is QuestPartText -> {
                part.title = part.title.censor()
                part.text = part.text.censor()
                for (input in part.inputs) {
                    input.hint = input.hint.censorNoFormat()
                }
                for (button in part.buttons) {
                    button.label = button.label.censorNoFormat()
                }

                if (part.insertBytes != null)
                    part.imageId = ControllerResources.put(part.insertBytes, questId)
                if (part.insertGifBytes != null)
                    part.gifId = ControllerResources.put(part.insertGifBytes, questId)
            }
            is QuestPartCondition -> {
                part.leftValue.sValue = part.leftValue.sValue.censorNoFormat()
                part.rightValue.sValue = part.rightValue.sValue.censorNoFormat()
            }
            is QuestPartAction -> {
                part.sArg = part.sArg.censorNoFormat()
            }
        }
    }

    fun insertPart(order: Long, questId: Long, part: QuestPart) {
        censorAndUploadPart(questId, part)
        Database.insert(
            "ControllerUserQuests insertPart", TQuestParts.NAME,
            TQuestParts.part_order, order,
            TQuestParts.unit_id, questId,
            TQuestParts.json_db, part.json(true, Json()),
        )
    }
}
