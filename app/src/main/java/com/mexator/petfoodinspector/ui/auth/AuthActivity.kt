package com.mexator.petfoodinspector.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.mexator.petfoodinspector.data.UserRepository
import com.mexator.petfoodinspector.data.network.RemoteDataSource
import com.mexator.petfoodinspector.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityAuthBinding.inflate(layoutInflater)
    }
    private val navController by lazy {
        Navigation.findNavController(binding.fragmentContainer)
    }

    private val repository: UserRepository = RemoteDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
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