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

    ///list of all the steps the users reach before unlocking the final step
    private var gameSteps : MutableList<GameSteps> = mutableListOf()

    ///the error count increment on each time the user presses a wrong button
    var errorCount: Int = 0

    ///Live data of the final step button value (generated by generateNumber)
    val finalStepLiveData : MutableLiveData<Int?> by lazy {
        MutableLiveData()
    }

    ///live data stores the error based on the MessageType added
    val errorLiveData : MutableLiveData<MessageTypes> by lazy {
        MutableLiveData()
    }

    ///The timer live data stores the seconds generated by GenerateNumber class and notifies
    ///the UI to display the Timer to the user
    val timerLiveData : MutableLiveData<Int>  by lazy {
        MutableLiveData()
    }


    /*
    Invoed each time user presses one of the steps buttons "1", "2" or "3" and the "Start" button
    this callback check on the actual gameSteps list values and check whether the nextStep is valid
    or not
    This callback is triggered also when the user is on the Final step.
     */
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

    /*
    Notify the UI with an error message of type MessageTypes based on the event pressed by
    the user.
    the error count is incremented only when the game starts
    if the errorCount reach the limit the user will loose and need to press "Start" again
    check also if the user on the final step or not (finalStepLiveData.value != null)
     */
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


    /*
    Update the current step in the gameSteps values
    check if user completed all steps to generate the Timer's seconds and the final step value
     */
    private fun updateCurrentStep(step: GameSteps){
        gameSteps.add(step)
        if(gameSteps.size == GameSteps.values().size){
            errorCount = 0
            CoroutineScope(Dispatchers.Main).launch {
                generateFinalStep()
            }
        }
    }

    /*
    generate a new random seconds from GenerateNumber class, to trigger the delay function and
    the Timer in the UI.
    Call the suspended callback generateNumber() which will return the value after X seconds generated
    earlier.
     */
    private suspend fun generateFinalStep(){
        timerLiveData.value = generateNumber.randomizeSeconds()
        val finalStep = generateNumber.generateNumber()
        finalStepLiveData.value = finalStep
    }


    /*
    Invoked by the onGameStepChanged callback only when user is on the final step
    check if the step pressed equal the right final step or not
    if final step is correct a success message will be notified to the UI
    else the errorCount will increment and an error message will be displayed
     */
    private fun onFinalStepPressed(gameSteps: GameSteps){
        if(GameSteps.values().indexOf(gameSteps) != finalStepLiveData.value){
            notifyWithError(MessageTypes.WrongFinalStep)
        }else{
            errorLiveData.value = MessageTypes.SuccessMessage
            resetGameSetup()
        }
    }


    /*
    Invoked when pressing the "Stop" button or if the user presses the right final step
    reset all ViewModel attribute to reset the game state.
     */
    fun resetGameSetup(){
        gameSteps = mutableListOf()
        errorCount = 0
        finalStepLiveData.value = null
    }

}