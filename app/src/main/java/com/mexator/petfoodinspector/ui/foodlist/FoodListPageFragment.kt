package com.mexator.petfoodinspector.ui.foodlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mexator.petfoodinspector.BuildConfig
import com.mexator.petfoodinspector.R
import com.mexator.petfoodinspector.databinding.FragmentPageFoodlistBinding
import com.mexator.petfoodinspector.domain.data.FoodItem
import com.mexator.petfoodinspector.ui.data.toUIDangerLevel
import com.mexator.petfoodinspector.ui.dpToPx
import com.mexator.petfoodinspector.ui.fooddetail.FoodDetailFragment
import com.mexator.petfoodinspector.ui.foodlist.model.FoodListViewModel
import com.mexator.petfoodinspector.ui.foodlist.model.FoodListViewState
import com.mexator.petfoodinspector.ui.foodlist.model.TempEvent
import com.mexator.petfoodinspector.ui.foodlist.recycler.FoodHolderFactory
import com.mexator.petfoodinspector.ui.foodlist.recycler.FoodItemClickCallback
import com.mexator.petfoodinspector.ui.foodlist.recycler.FoodUI
import com.mexator.petfoodinspector.ui.foodsearch.FoodSearchFragment
import com.mexator.petfoodinspector.ui.recycler.base.BaseAdapter
import com.mexator.petfoodinspector.ui.recycler.base.ViewTyped
import com.mexator.petfoodinspector.ui.recycler.common.SpaceDecorator
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

class FoodListPageFragment : Fragment() {
    private lateinit var binding: FragmentPageFoodlistBinding
    private val foodClickCallback: FoodItemClickCallback = object : FoodItemClickCallback {
        override fun itemClicked(food: FoodUI) {
            this@FoodListPageFragment.onFoodClicked(food)
        }

        override fun starClicked(food: FoodUI) {
            if (food.isFavorite) {
                viewModel.unFav(food.uid)
            } else {
                viewModel.fav(food.uid)
            }
        }
    }
    private val adapter = BaseAdapter<ViewTyped>(FoodHolderFactory(foodClickCallback))
    private val viewModel: FoodListViewModel by navGraphViewModels(R.id.main_navigation)
    private var compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPageFoodlistBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.foodRecycler.adapter = adapter
        binding.foodRecycler.addItemDecoration(SpaceDecorator(requireContext().dpToPx(8)))
        binding.foodRecycler.layoutManager = LinearLayoutManager(binding.foodRecycler.context)

        compositeDisposable += (parentFragment as FoodSearchFragment)
            .searchObservable
            .subscribeOn(Schedulers.computation())
            .subscribeBy(
                onNext = viewModel::submitQuery,
                onError = { ex -> Log.wtf(TAG, "This should never happen", ex) }
            )

        compositeDisposable +=
            viewModel.viewState
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = this::applyViewState,
                )

        compositeDisposable +=
            viewModel.tempEventsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = { event ->
                        when (event) {
                            is TempEvent.FavError -> Snackbar.make(
                                requireContext(),
                                binding.root,
                                event.message,
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                )

        viewModel.onAttachView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.dispose()
    }

    private val errorSnackBar: Snackbar by lazy {
        Snackbar.make(binding.root, "", Snackbar.LENGTH_INDEFINITE).apply {
            setAction(R.string.action_retry) {
                viewModel.onAttachView()
                this.dismiss()
            }
        }
    }

    private fun applyViewState(state: FoodListViewState) {
        if (BuildConfig.DEBUG) Log.d(TAG, state.toString())
        binding.foodRecycler.visibility = if (state.progress) View.INVISIBLE else View.VISIBLE
        binding.foodProgress.visibility = if (state.progress) View.VISIBLE else View.INVISIBLE
        adapter.items = state.displayedItems
            .map { mapItem(it, it.id in state.favoriteIDs) }
            .sortedWith(compareBy(
                { !it.isFavorite },
                { it.name }
            ))
        if (state.error != null && !state.progress) {
            errorSnackBar.setText(state.error)
            errorSnackBar.show()
        } else {
            errorSnackBar.dismiss()
        }
    }

    private fun mapItem(foodItem: FoodItem, isFavorite: Boolean): FoodUI {
        return FoodUI(
            foodItem.name,
            foodItem.imageData,
            foodItem.dangerLevel.toUIDangerLevel(),
            isFavorite,
            foodItem.id
        )
    }

    private fun onFoodClicked(food: FoodUI) {
        val navController = findNavController()
        val args: Bundle = Bundle()
        args.putInt(FoodDetailFragment.ARG_FOOD_KEY, food.uid)
        navController.navigate(R.id.action_foodSearchFragment_to_foodDetailFragment, args)
    }


    companion object {
        const val TAG = "FoodListPageFragment"
    }
}