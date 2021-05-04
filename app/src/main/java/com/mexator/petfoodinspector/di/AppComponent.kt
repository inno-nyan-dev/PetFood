package com.mexator.petfoodinspector.di

import com.mexator.petfoodinspector.data.network.RemoteFavoritesDataSource
import com.mexator.petfoodinspector.data.network.RemoteFoodsDataSource
import com.mexator.petfoodinspector.ui.DrawerViewModel
import com.mexator.petfoodinspector.ui.auth.AuthActivity
import com.mexator.petfoodinspector.ui.auth.login.LoginViewModel
import com.mexator.petfoodinspector.ui.auth.signup.SignUpViewModel
import com.mexator.petfoodinspector.ui.fooddetail.FoodDetailViewModel
import com.mexator.petfoodinspector.ui.foodlist.model.FoodListViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, RepositoryModule::class])
interface AppComponent {
    fun inject(ds: RemoteFavoritesDataSource)
    fun inject(ds: RemoteFoodsDataSource)
    fun inject(vm: LoginViewModel)
    fun inject(vm: DrawerViewModel)
    fun inject(vm: FoodListViewModel)
    fun inject(vm: SignUpViewModel)
    fun inject(vm: FoodDetailViewModel)
    fun inject(activity: AuthActivity)
}