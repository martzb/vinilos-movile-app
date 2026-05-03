package com.misw.vinilos.ui.collector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misw.vinilos.data.model.Collector
import com.misw.vinilos.data.repository.CollectorRepository
import kotlinx.coroutines.launch

class CollectorViewModel(
    private val repository: CollectorRepository = CollectorRepository()
) : ViewModel() {

    private val _collectors = MutableLiveData<List<Collector>>()
    val collectors: LiveData<List<Collector>> get() = _collectors

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    init {
        loadCollectors()
    }

    fun loadCollectors() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _collectors.value = repository.getCollectors()
            } catch (e: Exception) {
                _error.value = "No se pudo cargar la lista de coleccionistas. Verifica tu conexión."
            } finally {
                _isLoading.value = false
            }
        }
    }
}
