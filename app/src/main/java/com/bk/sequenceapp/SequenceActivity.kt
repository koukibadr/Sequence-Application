package com.bk.sequenceapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bk.sequenceapp.databinding.ActivitySequenceBinding

class SequenceActivity : AppCompatActivity() {

    lateinit var sequenceActivityBinding : ActivitySequenceBinding
    lateinit var sequenceActivityVM: SequenceActivityVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sequenceActivityBinding = ActivitySequenceBinding.inflate(layoutInflater)
        setContentView(sequenceActivityBinding.root)

        sequenceActivityVM = SequenceActivityVM()
        sequenceActivityVM.errorLiveData.observe(this) {errorMessage ->
            Toast.makeText(this,errorMessage,Toast.LENGTH_SHORT).show()
        }
        sequenceActivityVM.finalStepLiveData.observe(this) {finalStep ->
            sequenceActivityBinding.finalStepLabel.text = "You need to press the button ${finalStep} before the time is up"
            sequenceActivityBinding.topViewLayout.visibility = View.VISIBLE
        }

        sequenceActivityBinding.start.setOnClickListener {
            sequenceActivityVM.onGameStepChanged(GameSteps.start)
        }

        sequenceActivityBinding.firstStep.setOnClickListener {
            sequenceActivityVM.onGameStepChanged(GameSteps.firstStep)
        }

        sequenceActivityBinding.secondStep.setOnClickListener {
            sequenceActivityVM.onGameStepChanged(GameSteps.secondStep)
        }

        sequenceActivityBinding.thirsStep.setOnClickListener {
            sequenceActivityVM.onGameStepChanged(GameSteps.thirdStep)
        }
    }
}