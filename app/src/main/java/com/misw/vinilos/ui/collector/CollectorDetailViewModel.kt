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

    private val _collector = MutableLiveData<Collector>()
    val collector: LiveData<Collector> = _collector

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun getCollectorDetail(collectorId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _collector.value = repository.getCollector(collectorId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
