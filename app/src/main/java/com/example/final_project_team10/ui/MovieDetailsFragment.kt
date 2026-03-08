package com.example.final_project_team10.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.example.final_project_team10.R
import com.example.final_project_team10.data.Movie_Info
import com.google.android.material.progressindicator.CircularProgressIndicator
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private val API_KEY = "142812ec11e8136f3be45a8439922fa8"
    private val viewModel: MovieDetailsViewModel by viewModels()

    private lateinit var loadingIndicator: CircularProgressIndicator
    private lateinit var loadingErrorTV: TextView

    private lateinit var posterImage: ImageView
    private lateinit var titleText: TextView
    private lateinit var dateText: TextView
    private lateinit var ratingText: TextView
    private lateinit var overviewText: TextView
    private lateinit var genreText: TextView
    private lateinit var runtimeText: TextView
    private lateinit var budgetText: TextView
    private lateinit var revenueText: TextView
    private lateinit var trailerWebView: WebView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingErrorTV = view.findViewById(R.id.tv_loading_error_details)
        loadingIndicator = view.findViewById(R.id.loading_indicator_details)

        posterImage = view.findViewById(R.id.posterImageDetails)
        titleText = view.findViewById(R.id.titleTextDetails)
        dateText = view.findViewById(R.id.dateTextDetails)
        ratingText = view.findViewById(R.id.ratingTextDetails)
        overviewText = view.findViewById(R.id.overviewTextDetails)
        genreText = view.findViewById(R.id.genreTextDetails)
        runtimeText = view.findViewById(R.id.runtimeTextDetails)
        budgetText = view.findViewById(R.id.budgetTextDetails)
        revenueText = view.findViewById(R.id.revenueTextDetails)

        trailerWebView = view.findViewById(R.id.trailerWebView)
        setupTrailerWebView()

        viewModel.movieDetails.observe(viewLifecycleOwner) { movie ->
            if (movie != null) {
                bindMovie(movie)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                loadingErrorTV.text = getString(R.string.loading_error, error.message)
                loadingErrorTV.visibility = View.VISIBLE
                Log.e("MovieDetailsFragment", "Error fetching movie details: ${error.message}")
                error.printStackTrace()
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                loadingIndicator.visibility = View.VISIBLE
                loadingErrorTV.visibility = View.INVISIBLE
                trailerWebView.visibility = View.GONE
            } else {
                loadingIndicator.visibility = View.INVISIBLE
            }
        }

        viewModel.trailer.observe(viewLifecycleOwner) { trailer ->
            if (trailer != null && trailer.site == "YouTube") {
                loadTrailerInWebView(trailer.key)
                trailerWebView.visibility = View.VISIBLE
            } else {
                trailerWebView.visibility = View.GONE
            }
        }

    }

    override fun onResume() {
        super.onResume()

        val movieId = requireArguments().getInt("movieId")

        if (viewModel.movieDetails.value == null) {
            viewModel.loadMovieInfo(movieId, API_KEY)
        }

        if (viewModel.trailer.value == null) {
            viewModel.loadMovieTrailer(movieId, API_KEY)
        }
    }

    private fun bindMovie(movie: Movie_Info) {
        titleText.text = movie.title
        dateText.text = "Release Date: ${movie.release_date.ifBlank { "Unknown" }}"
        ratingText.text = "Rating: %.1f/10".format(movie.vote_average)
        overviewText.text = movie.overview.ifBlank { "No description available." }

        val genreNames = if (movie.genres.isNullOrEmpty()) {
            "Unknown"
        } else {
            movie.genres.joinToString(", ") { it.name }
        }
        genreText.text = genreNames

        runtimeText.text = formatRuntime(movie.runtime)
        budgetText.text = formatMoney(movie.budget)
        revenueText.text = formatMoney(movie.revenue)

        posterImage.load(getPosterUrl(movie.poster_path)) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
            error(R.drawable.ic_launcher_background)
        }
    }

    private fun formatRuntime(runtime: Int?): String {
        if (runtime == null || runtime <= 0) {
            return "Unknown"
        }

        val hours = runtime / 60
        val minutes = runtime % 60

        return if (hours > 0) {
            "${hours}h ${minutes}m"
        } else {
            "${minutes}m"
        }
    }

    private fun formatMoney(amount: Long?): String {
        if (amount == null || amount <= 0L) {
            return "Unknown"
        }
        return "$" + "%,d".format(amount)
    }

    private fun getPosterUrl(posterPath: String?): String? {
        return if (posterPath.isNullOrBlank()) {
            null
        } else {
            "https://image.tmdb.org/t/p/w500$posterPath"
        }
    }

    private fun setupTrailerWebView() {
        trailerWebView.webViewClient = WebViewClient()
        trailerWebView.webChromeClient = WebChromeClient()

        val webSettings = trailerWebView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.mediaPlaybackRequiresUserGesture = false
        webSettings.loadsImagesAutomatically = true
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
    }

    private fun loadTrailerInWebView(videoKey: String) {
        val appBaseUrl = "https://${requireContext().packageName}"
        val embedUrl =
            "https://www.youtube.com/embed/$videoKey?enablejsapi=1&playsinline=1&rel=0&origin=$appBaseUrl"

        val html = """
        <html>
          <body style="margin:0;padding:0;background-color:black;">
            <iframe
              id="player"
              type="text/html"
              width="100%"
              height="100%"
              src="$embedUrl"
              frameborder="0"
              allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
              allowfullscreen>
            </iframe>
          </body>
        </html>
    """.trimIndent()

        trailerWebView.loadDataWithBaseURL(
            appBaseUrl,
            html,
            "text/html",
            "utf-8",
            null
        )
    }

    override fun onDestroyView() {
        trailerWebView.loadUrl("about:blank")
        trailerWebView.stopLoading()
        trailerWebView.destroy()
        super.onDestroyView()
    }
}