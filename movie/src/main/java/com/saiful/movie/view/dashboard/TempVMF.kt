package com.saiful.movie.view.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.saiful.movie.data.repository.DashboardRepo

class TempVMF(private val repo: DashboardRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardVM::class.java))
            return DashboardVM(repo) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}