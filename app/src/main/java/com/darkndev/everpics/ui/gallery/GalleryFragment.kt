package com.darkndev.everpics.ui.gallery

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.darkndev.everpics.R
import com.darkndev.everpics.databinding.FragmentGalleryBinding
import com.darkndev.everpics.recyclerview.adapters.UnsplashLoadStateAdapter
import com.darkndev.everpics.recyclerview.adapters.UnsplashPagingAdapter
import com.darkndev.everpics.recyclerview.decoration.ItemOffsetDecoration
import com.darkndev.everpics.utils.onQueryTextSubmit
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery), MenuProvider {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GalleryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGalleryBinding.bind(view)

        val unsplashAdapter = UnsplashPagingAdapter {
            val action = GalleryFragmentDirections.actionGalleryFragmentToDetailsFragment(it)
            findNavController().navigate(action)
        }

        binding.apply {
            val menuHost: MenuHost = requireActivity()

            menuHost.addMenuProvider(
                this@GalleryFragment,
                viewLifecycleOwner,
                Lifecycle.State.RESUMED
            )

            recyclerView.apply {
                setHasFixedSize(true)
                itemAnimator = null
                adapter = unsplashAdapter.withLoadStateHeaderAndFooter(
                    header = UnsplashLoadStateAdapter { unsplashAdapter.retry() },
                    footer = UnsplashLoadStateAdapter { unsplashAdapter.retry() },
                )
                addItemDecoration(ItemOffsetDecoration(4))
            }

            retry.setOnClickListener {
                unsplashAdapter.retry()
            }

            unsplashAdapter.addLoadStateListener { loadState ->
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                retry.isVisible = loadState.source.refresh is LoadState.Error

                //empty view
                if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && unsplashAdapter.itemCount < 1) {
                    recyclerView.isVisible = false
                    textViewEmpty.isVisible = true
                } else {
                    textViewEmpty.isVisible = false
                }
            }
        }

        viewModel.photos.observe(viewLifecycleOwner) {
            unsplashAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_gallery, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.onQueryTextSubmit {
            binding.recyclerView.scrollToPosition(0)
            viewModel.searchPhotos(it)
            searchView.clearFocus()
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem) = true
}