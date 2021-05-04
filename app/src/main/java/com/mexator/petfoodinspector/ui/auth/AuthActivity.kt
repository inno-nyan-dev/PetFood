package com.mexator.petfoodinspector.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.mexator.petfoodinspector.AppController
import com.mexator.petfoodinspector.data.UserDataSource
import com.mexator.petfoodinspector.databinding.ActivityAuthBinding
import javax.inject.Inject

class AuthActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityAuthBinding.inflate(layoutInflater)
    }
    private val navController by lazy {
        Navigation.findNavController(binding.fragmentContainer)
    }

    @Inject
    lateinit var repository: UserDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        AppController.component.inject(this)
    }

    override fun onStart() {
        super.onStart()
        NavigationUI.setupActionBarWithNavController(this, navController)
        repository.logout().subscribe()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}