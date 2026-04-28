package com.misw.vinilos.ui.collector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.misw.vinilos.data.model.Collector

class CollectorViewModel : ViewModel() {

    private val _collectors = MutableLiveData<List<Collector>>()
    val collectors: LiveData<List<Collector>> get() = _collectors

    init {
        _collectors.value = listOf(
            Collector(1, "Jaime Monsalve", "j.monsalve@gmail.com"),
            Collector(2, "Manolo Betancourt", "m.betancourt@gmail.com"),
            Collector(3, "Catalina López", "c.lopez@gmail.com"),
            Collector(4, "Andrés Torres", "a.torres@gmail.com"),
            Collector(5, "Patricia Reyes", "p.reyes@gmail.com")
        )
    }
}
