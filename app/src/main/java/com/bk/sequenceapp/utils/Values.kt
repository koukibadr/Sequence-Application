package com.bk.sequenceapp.utils

import com.bk.sequenceapp.R

enum class GameSteps {
    Start,
    FirstStep,
    SecondStep,
    ThirdStep,
}

enum class MessageTypes {
    StartButtonError,
    WrongStepPressed,
    WrongFinalStep,
    ErrorLimit,
    SuccessMessage
}

fun GameSteps.title(): String {
    return when(this){
        GameSteps.FirstStep -> "1"
        GameSteps.SecondStep -> "2"
        GameSteps.ThirdStep -> "3"
        else -> {
            "Start"
        }
    }
}

fun MessageTypes.title() : Int {
    return when(this) {
        MessageTypes.StartButtonError -> R.string.start_button_error
        MessageTypes.ErrorLimit -> R.string.limit_error_message
        MessageTypes.SuccessMessage -> R.string.success_message
        MessageTypes.WrongFinalStep -> R.string.wrong_final_step
        MessageTypes.WrongStepPressed -> R.string.wrong_step_pressed
    }
}

fun MessageTypes.color() : Int {
    return if(this == MessageTypes.SuccessMessage){
        R.color.green
    }else{
        R.color.red
    }
}