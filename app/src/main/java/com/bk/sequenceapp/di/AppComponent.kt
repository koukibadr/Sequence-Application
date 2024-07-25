package com.bk.sequenceapp.di

import com.bk.sequenceapp.sequence.SequenceActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [UtilsModule::class])
interface AppComponent {
    fun injectSequenceActivity(sequenceActivity: SequenceActivity)

}