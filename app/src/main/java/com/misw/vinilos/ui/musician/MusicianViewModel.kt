package com.misw.vinilos.ui.musician

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misw.vinilos.data.model.Musician
import com.misw.vinilos.data.repository.MusicianRepository
import kotlinx.coroutines.launch

class MusicianViewModel : ViewModel() {

    private val repository = MusicianRepository()

    private val _musicians = MutableLiveData<List<Musician>>()
    val musicians: LiveData<List<Musician>> get() = _musicians

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    init {
        loadMusicians()
    }

    fun loadMusicians() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _musicians.value = repository.getMusicians()
            } catch (e: Exception) {
                _error.value = "No se pudo cargar la lista de artistas. Verifica tu conexión."
            } finally {
                _isLoading.value = false
            }
        }
    }
}
