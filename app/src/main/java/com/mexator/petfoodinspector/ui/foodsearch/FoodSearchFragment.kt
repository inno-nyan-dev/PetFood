package com.mexator.petfoodinspector.ui.foodsearch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.mexator.petfoodinspector.BuildConfig
import com.mexator.petfoodinspector.databinding.FragmentFoodSearchBinding
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

/**
 * Wrapper for FoodList fragments. Provides its children with ability to listen to
 * search queries via [searchObservable]
 */
class FoodSearchFragment : Fragment() {
    private lateinit var binding: FragmentFoodSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (BuildConfig.DEBUG) Log.d(TAG,"onCreateView")
        binding = FragmentFoodSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private val searchSubject: BehaviorSubject<String> = BehaviorSubject.createDefault("")

    /**
     * Emits trimmed, lower-cased search queries. Includes debounce already
     */
    val searchObservable: Observable<String> = searchSubject
        .map(String::trim)
        .map(String::lowercase)
        .debounce(400, TimeUnit.MILLISECONDS)

    private val onQueryTextListener = object : SearchView.OnQueryTextListener {
        // We listen to every change, so we can ignore submit events
        override fun onQueryTextSubmit(query: String?): Boolean = true

        override fun onQueryTextChange(newText: String?): Boolean {
            searchSubject.onNext(newText)
            return true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.foodSearch.setOnQueryTextListener(onQueryTextListener)
    }

    companion object {
        private const val TAG = "FoodSearchFragment"
    }
}