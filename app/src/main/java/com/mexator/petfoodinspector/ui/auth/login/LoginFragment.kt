package com.mexator.petfoodinspector.ui.auth.login

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mexator.petfoodinspector.R
import com.mexator.petfoodinspector.databinding.AuthFieldsBinding
import com.mexator.petfoodinspector.databinding.FragmentLoginBinding
import com.mexator.petfoodinspector.ui.StartActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign

/**
 * Screen at which user can log in into existing account
 */
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var fieldsBinding: AuthFieldsBinding
    private val viewModel: LoginViewModel by viewModels()
    private val viewModelDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        fieldsBinding = AuthFieldsBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelDisposable +=
            viewModel.viewState
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::render)

        binding.buttonLogin.setOnClickListener {
            viewModel.logIn(
                fieldsBinding.emailField.text.toString(),
                fieldsBinding.passwordField.text.toString()
            )
        }

        binding.buttonSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }

    override fun onDestroyView() {
        viewModelDisposable.clear()
        super.onDestroyView()
    }

    private fun render(state: LoginViewState) {
        when (state) {
            is ProgressState -> {
                binding.progress.visibility = View.VISIBLE
            }
            is SuccessState -> {
                binding.progress.visibility = View.INVISIBLE
                val startActivityIntent = Intent(activity, StartActivity::class.java)
                startActivityIntent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(startActivityIntent)
                activity?.finish()
            }
            is ErrorState -> {
                binding.progress.visibility = View.INVISIBLE
                AlertDialog.Builder(context)
                    .setTitle(R.string.dialog_error_title)
                    .setMessage(state.message)
                    .setPositiveButton("OK") { dialogInterface, _ -> dialogInterface.dismiss() }
                    .create()
                    .show()
            }
        }
    }
}