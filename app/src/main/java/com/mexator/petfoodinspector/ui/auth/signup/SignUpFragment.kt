package com.mexator.petfoodinspector.ui.auth.signup

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mexator.petfoodinspector.R
import com.mexator.petfoodinspector.databinding.AuthFieldsBinding
import com.mexator.petfoodinspector.databinding.FragmentSignUpBinding
import com.mexator.petfoodinspector.ui.StartActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign

/**
 * Screen where use can register
 */
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
            viewModel.viewState
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::render)

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