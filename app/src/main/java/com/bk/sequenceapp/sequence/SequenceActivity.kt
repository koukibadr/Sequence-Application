package com.bk.sequenceapp.sequence

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bk.sequenceapp.ApplicationClass
import com.bk.sequenceapp.R
import com.bk.sequenceapp.databinding.ActivitySequenceBinding
import com.bk.sequenceapp.utils.GameSteps
import com.bk.sequenceapp.utils.title
import javax.inject.Inject

class SequenceActivity : AppCompatActivity() {

    lateinit var sequenceActivityBinding : ActivitySequenceBinding

    @Inject
    lateinit var sequenceActivityVM: SequenceActivityVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as ApplicationClass).appComponent.injectSequenceActivity(this)

        sequenceActivityBinding = ActivitySequenceBinding.inflate(layoutInflater)
        sequenceActivityBinding.viewmodel = sequenceActivityVM
        setContentView(sequenceActivityBinding.root)


        sequenceActivityBinding.buttonsRecyclerView.layoutManager = LinearLayoutManager(this)
        sequenceActivityBinding.buttonsRecyclerView.adapter = ButtonsRecyclerAdapter(
            GameSteps.values().filter {
                it != GameSteps.Start
            }.toList(),
            onItemPressed = ::onGameStepPressed
        )

        sequenceActivityVM.errorLiveData.observe(this) {errorMessage ->
            Toast.makeText(this,errorMessage.title(),Toast.LENGTH_SHORT).show()
        }

        sequenceActivityVM.finalStepLiveData.observe(this) {finalStep ->
            if(finalStep == null){
                sequenceActivityBinding.topViewLayout.visibility = View.GONE
                sequenceActivityBinding.finalStepLabel.text = ""
            } else {
                sequenceActivityBinding.finalStepLabel.text = String.format(resources.getString(R.string.final_step_message),finalStep)
            }
        }

        sequenceActivityVM.timerLiveData.observe(this) { delaySeconds ->
            sequenceActivityBinding.topViewLayout.visibility = View.VISIBLE
            object : CountDownTimer(delaySeconds * 1000L, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val secondsRemaining = millisUntilFinished / 1000
                    if(secondsRemaining >= 10){
                        sequenceActivityBinding.timerDisplay.text = "00:$secondsRemaining"
                    }else{
                        sequenceActivityBinding.timerDisplay.text = "00:0$secondsRemaining"
                    }
                }
                override fun onFinish() {}
            }.start()
        }

    }

    private fun onGameStepPressed(stepIndex: Int){
        sequenceActivityVM.onGameStepChanged(GameSteps.values()[stepIndex+1])
    }
}