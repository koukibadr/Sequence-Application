package com.bk.sequenceapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random
import kotlin.random.nextInt

class SequenceActivityVM: ViewModel() {

    private val gameSteps : MutableList<GameSteps> = mutableListOf()

    val finalStepLiveData : MutableLiveData<Int?> by lazy {
        MutableLiveData()
    }

    val errorLiveData : MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    fun onGameStepChanged(step: GameSteps){
        if(gameSteps.isEmpty()) {
            if(step == GameSteps.start){
                updateCurrentStep(step)
            }else {
                notifyWithError("Press start to start the game")
            }
        }else {
            when(gameSteps.last()){
                GameSteps.start -> {
                    if(step != GameSteps.firstStep){
                        notifyWithError("Wrong button pressed")
                        return
                    }
                }
                GameSteps.firstStep -> {
                    if(step != GameSteps.secondStep){
                        notifyWithError("Wrong button pressed")
                        return
                    }
                }
                GameSteps.secondStep -> {
                    if(step != GameSteps.thirdStep){
                        notifyWithError("Wrong button pressed")
                        return
                    }
                }
                else -> {
                    updateCurrentStep(step)
                }
            }
            updateCurrentStep(step)
        }
    }

    private fun notifyWithError(error: String){
        errorLiveData.value = error
    }

    private fun updateCurrentStep(step: GameSteps){
        gameSteps.add(step)
        if(gameSteps.size == GameSteps.values().size){
            finalStepLiveData.value = Random.nextInt(1..3)
        }
    }


}