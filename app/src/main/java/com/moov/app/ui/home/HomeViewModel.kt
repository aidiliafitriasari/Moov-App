package com.moov.app.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moov.app.data.remote.TmdbMovieDto
import com.moov.app.data.repository.MovieRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repository = MovieRepository()

    var trendingMovies = mutableStateOf<List<TmdbMovieDto>>(emptyList())
    var nowPlayingMovies = mutableStateOf<List<TmdbMovieDto>>(emptyList())
    var popularMovies = mutableStateOf<List<TmdbMovieDto>>(emptyList())
    var isLoading = mutableStateOf(true)
    var errorMessage = mutableStateOf<String?>(null)

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            try {
                isLoading.value = true
                trendingMovies.value = repository.getTrendingMovies().results
                nowPlayingMovies.value = repository.getNowPlayingMovies().results
                popularMovies.value = repository.getPopularMovies().results
                isLoading.value = false
            } catch (e: Exception) {
                errorMessage.value = e.message
                isLoading.value = false
            }
        }
    }
}