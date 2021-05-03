package com.mexator.petfoodinspector.ui.auth.signup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.mexator.petfoodinspector.databinding.AuthFieldsBinding
import com.mexator.petfoodinspector.databinding.FragmentSignUpBinding
import com.mexator.petfoodinspector.ui.StartActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var fieldsBinding: AuthFieldsBinding
    private val viewModel: SignUpViewModel by viewModels()
    private val viewModelDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        fieldsBinding = AuthFieldsBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelDisposable +=
            viewModel.viewState.subscribe(this::render)

        binding.buttonSignUp.setOnClickListener {
            viewModel.logIn(
                fieldsBinding.emailField.text.toString(),
                fieldsBinding.passwordField.text.toString()
            )
        }
    }

    private fun render(state: SignInViewState) {
        when (state) {
            is ProgressState -> {
                binding.progress.visibility = View.VISIBLE
            }
            is SuccessState -> {
                binding.progress.visibility = View.INVISIBLE
                val startActivityIntent = Intent(requireActivity(), StartActivity::class.java)
                startActivity(startActivityIntent)
                activity?.finish()
            }
            is ErrorState -> {
                binding.progress.visibility = View.INVISIBLE
                Snackbar.make(binding.root, state.message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}