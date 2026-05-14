package com.misw.vinilos.ui.collector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misw.vinilos.data.model.Collector
import com.misw.vinilos.data.repository.CollectorRepository
import kotlinx.coroutines.launch

class CollectorDetailViewModel : ViewModel() {

    private val repository = CollectorRepository()
    private val albumRepository = com.misw.vinilos.data.repository.AlbumRepository()

    private val _collector = MutableLiveData<Collector>()
    val collector: LiveData<Collector> = _collector

    private val _albums = MutableLiveData<List<com.misw.vinilos.data.model.Album>>()
    val albums: LiveData<List<com.misw.vinilos.data.model.Album>> = _albums

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun getCollectorDetail(collectorId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val fetchedCollector = repository.getCollector(collectorId)
                _collector.value = fetchedCollector

                // Obtener álbumes completos para renderizar las portadas
                if (fetchedCollector.collectorAlbums.isNotEmpty()) {
                    val allAlbums = albumRepository.getAlbums()
                    val collectorAlbumIds = fetchedCollector.collectorAlbums.map { it.id }
                    _albums.value = allAlbums.filter { it.id in collectorAlbumIds }
                } else {
                    _albums.value = emptyList()
                }

            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
