package com.mexator.petfoodinspector

import com.mexator.petfoodinspector.di.NetworkModule
import com.mexator.petfoodinspector.di.RepositoryModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, RepositoryModule::class])
interface TestComponent {
    fun inject(test: BackendConnectivityTest)
}