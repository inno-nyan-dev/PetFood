 package com.mexator.petfoodinspector.di

import com.mexator.petfoodinspector.data.network.RemoteFavoritesDataSource
import com.mexator.petfoodinspector.data.network.RemoteFoodsDataSource
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface AppComponent {
    fun inject(ds: RemoteFavoritesDataSource)
    fun inject(ds: RemoteFoodsDataSource)
}