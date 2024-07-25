package com.bk.sequenceapp.sequence

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bk.sequenceapp.utils.GameSteps
import com.bk.sequenceapp.utils.GenerateNumber
import com.bk.sequenceapp.utils.MessageTypes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

const val MAX_ERRORS = 3
class SequenceActivityVM @Inject constructor(val generateNumber: GenerateNumber): ViewModel() {

    private var gameSteps : MutableList<GameSteps> = mutableListOf()
    var errorCount: Int = 0

    val finalStepLiveData : MutableLiveData<Int?> by lazy {
        MutableLiveData()
    }
    val errorLiveData : MutableLiveData<MessageTypes> by lazy {
        MutableLiveData()
    }
    val timerLiveData : MutableLiveData<Int>  by lazy {
        MutableLiveData()
    }

    fun onGameStepChanged(nextStep: GameSteps){
        if(gameSteps.size == GameSteps.values().size){
            onFinalStepPressed(nextStep)
        }else{
            if(gameSteps.isEmpty()) {
                if(nextStep == GameSteps.Start){
                    updateCurrentStep(nextStep)
                }else {
                    notifyWithError(MessageTypes.StartButtonError)
                }
            }else {
                when(gameSteps.last()){
                    GameSteps.Start -> {
                        if(nextStep != GameSteps.FirstStep){
                            notifyWithError(MessageTypes.WrongStepPressed)
                            return
                        }
                    }
                    GameSteps.FirstStep -> {
                        if(nextStep != GameSteps.SecondStep){
                            notifyWithError(MessageTypes.WrongStepPressed)
                            return
                        }
                    }
                    GameSteps.SecondStep -> {
                        if(nextStep != GameSteps.ThirdStep){
                            notifyWithError(MessageTypes.WrongStepPressed)
                            return
                        }
                    }
                    else -> {
                        updateCurrentStep(nextStep)
                    }
                }
                updateCurrentStep(nextStep)
            }
        }


    }

    private fun notifyWithError(error: MessageTypes){
        if(gameSteps.isNotEmpty()){
            errorCount++
        }
        if(errorCount == MAX_ERRORS){
            ///User on final step of the game and reached the max error count
            ///the app reset the final step and user must start again
            if(finalStepLiveData.value != null){
                finalStepLiveData.value = null
            }
            errorLiveData.value = MessageTypes.ErrorLimit
            gameSteps = mutableListOf()
            return
        }
        errorLiveData.value = error
    }

    private fun updateCurrentStep(step: GameSteps){
        gameSteps.add(step)
        if(gameSteps.size == GameSteps.values().size){
            errorCount = 0
            CoroutineScope(Dispatchers.Main).launch {
                generateFinalStep()
            }
        }
    }

    private suspend fun generateFinalStep(){
        timerLiveData.value = generateNumber.randomizeSeconds()
        val finalStep = generateNumber.generateNumber()
        finalStepLiveData.value = finalStep
    }

    private fun onFinalStepPressed(gameSteps: GameSteps){
        if(GameSteps.values().indexOf(gameSteps) != finalStepLiveData.value){
            notifyWithError(MessageTypes.WrongFinalStep)
        }else{
            errorLiveData.value = MessageTypes.SuccessMessage
            resetGameSetup()
        }
    }

    fun resetGameSetup(){
        gameSteps = mutableListOf()
        errorCount = 0
        finalStepLiveData.value = null
    }

}