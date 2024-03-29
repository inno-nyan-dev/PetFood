package com.mexator.petfoodinspector.ui.fooddetail

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mexator.petfoodinspector.R
import com.mexator.petfoodinspector.databinding.FragmentFoodDetailBinding
import com.mexator.petfoodinspector.ui.data.FoodPictureDrawableFactory
import com.mexator.petfoodinspector.ui.data.toUIDangerLevel
import com.mexator.petfoodinspector.ui.foodlist.FoodListPageFragment
import com.mexator.petfoodinspector.ui.getResources
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers

class FoodDetailFragment : Fragment() {
    private lateinit var binding: FragmentFoodDetailBinding
    private val viewModel: FoodDetailViewModel by viewModels()
    private var viewModelDisposable: Disposable? = null

    private val foodId: Int by lazy { arguments?.getInt(ARG_FOOD_KEY) ?: 0 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFoodDetailBinding.inflate(layoutInflater, container, false)
        viewModel.foodId = foodId
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelDisposable =
            viewModel.viewState
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = this::applyViewState,
                    onError = { throwable -> Log.d(FoodListPageFragment.TAG, "", throwable) }
                )
    }

    private var favorite: Boolean = false

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.food_detail_actionbar, menu)
        menu.getItem(0).setIcon(R.drawable.ic_star_border_24)
        super.onCreateOptionsMenu(menu, inflater)
        // Todo change setting of this flag
        favorite = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_fav -> {
                favorite = if (favorite) {
                    item.setIcon(R.drawable.ic_star_border_24)
                    false
                } else {
                    item.setIcon(R.drawable.ic_star_filled_24)
                    true
                }
                // Todo process update here
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModelDisposable?.dispose()
    }

    private fun applyViewState(state: FoodDetailViewModel.FoodDetailViewState) {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = state.foodDetail.foodItem.name
        binding.foodDetailText.text = state.foodDetail.detailText
        state.foodDetail.foodItem.let { item ->
            Single.defer {
                Single.just(
                    FoodPictureDrawableFactory().createDrawable(
                        binding.foodPicture.context,
                        item.imageData
                    )
                )
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = { binding.foodPicture.setImageDrawable(it) }
                )

            val uiDangerLevel = item.dangerLevel.toUIDangerLevel()

            binding.dangerLevel.text = uiDangerLevel.levelString
            val color =
                ResourcesCompat.getColor(binding.getResources(), uiDangerLevel.colorRes, null)
            binding.dangerLevel.backgroundTintList = ColorStateList.valueOf(color)
        }
    }

    companion object {
        @Suppress("unused")
        const val TAG = "FoodDetailFragment"
        const val ARG_FOOD_KEY = "foodId"
    }
}