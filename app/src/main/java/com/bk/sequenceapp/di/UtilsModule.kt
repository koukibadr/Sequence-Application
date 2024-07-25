package com.bk.sequenceapp.di

import com.bk.sequenceapp.utils.GenerateNumber
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UtilsModule {

    @Provides
    @Singleton
    fun provideGeneratorClass() : GenerateNumber {
        return  GenerateNumber()
    }

}