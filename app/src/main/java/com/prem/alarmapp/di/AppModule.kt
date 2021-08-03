package com.prem.alarmapp.di

import android.content.Context
import androidx.room.Room
import com.prem.alarmapp.data.dao.AlarmDao
import com.prem.alarmapp.data.database.AlarmDatabase
import com.prem.alarmapp.data.repository.AlarmRepository
import com.prem.alarmapp.data.repository.DefaultAlarmRepository
import com.prem.alarmapp.utils.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAlarmDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, AlarmDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideAlarmDao(
        database: AlarmDatabase
    ) = database.getAlarmDao()

    @Singleton
    @Provides
    fun provideDefaultAlarmRepository(
        dao: AlarmDao
    ) = DefaultAlarmRepository(dao) as AlarmRepository
}