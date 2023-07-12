package com.darkndev.everpics.ui.bookmark

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.darkndev.everpics.R
import com.darkndev.everpics.databinding.FragmentBookmarkBinding
import com.darkndev.everpics.recyclerview.adapters.UnsplashListAdapter
import com.darkndev.everpics.recyclerview.decoration.ItemOffsetDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkFragment : Fragment(R.layout.fragment_bookmark) {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookmarkViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBookmarkBinding.bind(view)

        val photoAdapter = UnsplashListAdapter {
            val action = BookmarkFragmentDirections.actionBookmarkFragmentToDetailsFragment(it)
            findNavController().navigate(action)
        }

        binding.apply {
            recyclerView.apply {
                setHasFixedSize(true)
                adapter = photoAdapter
                addItemDecoration(ItemOffsetDecoration(4))
            }
        }

        viewModel.apply {
            photosList.observe(viewLifecycleOwner) {
                binding.textViewEmpty.isVisible = it.isEmpty()
                photoAdapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}