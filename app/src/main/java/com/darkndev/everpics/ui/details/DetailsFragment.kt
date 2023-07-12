package com.darkndev.everpics.ui.details

import android.Manifest
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.darkndev.everpics.R
import com.darkndev.everpics.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailsViewModel by viewModels()

    private val unsplashPhoto get() = viewModel.statePhoto

    private val permissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                viewModel.writePermissionGranted = isGranted
            } else {
                startActivity(Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.parse("package:" + context?.packageName)
                })
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailsBinding.bind(view)

        binding.apply {
            Glide.with(this@DetailsFragment)
                .load(unsplashPhoto.urls.regular)
                .error(R.drawable.error)
                .listener(listener)
                //.apply(RequestOptions.bitmapTransform(RoundedCorners(48)))
                .into(imageView)

            Glide.with(this@DetailsFragment)
                .load(unsplashPhoto.user.profile_image.large)
                .error(R.drawable.person)
                .circleCrop()
                .into(profileView)

            description.isVisible = unsplashPhoto.description != null
            description.text = unsplashPhoto.description

            val date = ZonedDateTime.parse(unsplashPhoto.created_at)
                .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
            creationTime.text = StringBuilder("Created: $date")

            dimensions.text =
                StringBuilder("${unsplashPhoto.width}x${unsplashPhoto.height}")
            likes.text = unsplashPhoto.likes.toString()

            creatorName.text = unsplashPhoto.user.name

            creatorUsername.text = StringBuilder("@${unsplashPhoto.user.username}")

            visitUnsplash.setOnClickListener {
                navigate(unsplashPhoto.user.userUrl)
            }
            download.setOnClickListener {
                viewModel.checkWritePermissionAndDownload(unsplashPhoto.links.download)
            }

            bookmark.setOnClickListener {
                viewModel.bookmarkClicked()
            }
            imageCard.setOnClickListener {
                navigate(unsplashPhoto.links.html)
            }
            visit.setOnClickListener {
                navigate(unsplashPhoto.links.html)
            }
        }
        viewModel.databasePhoto.observe(viewLifecycleOwner) { photo ->
            binding.bookmark.isChecked = photo != null
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.event.collectLatest {
                when (it) {
                    DetailsViewModel.Event.CheckWritePermission -> {
                        permissionsLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                }
            }
        }
    }

    private fun navigate(url: String) {
        val intent =
            Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context?.startActivity(intent)
    }

    private val listener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            binding.progressBar.show()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            binding.apply {
                progressBar.hide()
                imageDetails.isVisible = true
            }
            return false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}