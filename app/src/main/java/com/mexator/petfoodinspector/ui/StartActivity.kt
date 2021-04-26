package com.mexator.petfoodinspector.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.mexator.petfoodinspector.BuildConfig
import com.mexator.petfoodinspector.R
import com.mexator.petfoodinspector.databinding.ActivityStartBinding
import com.mexator.petfoodinspector.databinding.DrawerHeaderBinding

class StartActivity : AppCompatActivity() {
    private val binding: ActivityStartBinding by lazy {
        ActivityStartBinding.inflate(layoutInflater)
    }
    private val drawerHeaderBinding: DrawerHeaderBinding by lazy {
        DrawerHeaderBinding.bind(binding.navView.getHeaderView(0))
    }

    private val navController by lazy {
        Navigation.findNavController(binding.fragmentContainer)
    }

    private val viewModel: DrawerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.viewState.subscribe(this::updateDrawer)
    }

    override fun onStart() {
        super.onStart()
        binding.navView.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout)
        viewModel.onViewAttach()
    }

    override fun onSupportNavigateUp(): Boolean {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return false
        } else {
            return NavigationUI.navigateUp(navController, binding.drawerLayout)
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else
            super.onBackPressed()
    }

    private fun updateDrawer(state: DrawerState) {
        if (BuildConfig.DEBUG) Log.d(TAG, state.toString())
        val loginItem = binding.navView.menu.findItem(R.id.authActivity)

        if (state.user != null) {
            drawerHeaderBinding.usernameView.text = "Not implemented yet"
            drawerHeaderBinding.emailView.visibility = View.VISIBLE
            drawerHeaderBinding.emailView.text = state.user.email

            loginItem.setIcon(R.drawable.ic_logout_24)
            loginItem.setTitle(R.string.logout_title)
        } else {
            drawerHeaderBinding.emailView.visibility = View.GONE
            drawerHeaderBinding.usernameView.setText(R.string.header_text_no_user)

            loginItem.setIcon(R.drawable.ic_account_24)
            loginItem.setTitle(R.string.login_title)
        }
    }

    companion object {
        private const val TAG = "StartActivity"
    }
}