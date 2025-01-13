package com.example.pamfirebase.ui.viewmodel

import android.net.http.HttpException
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pamfirebase.model.Mahasiswa
import com.example.pamfirebase.repository.NetworkRepositoryMhs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException


sealed class DetailUiState {
    data class Success(val mahasiswa: Mahasiswa) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
    object Loading : DetailUiState()
}

class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val mhs: NetworkRepositoryMhs
) : ViewModel() {
    private val _nim: String = checkNotNull(savedStateHandle["nim"])

    private val _detailUiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val detailUiState: StateFlow<DetailUiState> = _detailUiState.asStateFlow()

    init {
        getMahasiswaByNim(_nim)
    }

    private fun getMahasiswaByNim(nim: String) {
        viewModelScope.launch {
            _detailUiState.value = DetailUiState.Loading
            _detailUiState.value = try {
                val mahasiswa = mhs.getMhs(nim)
                DetailUiState.Success(mahasiswa)
            } catch (e: IOException) {
                DetailUiState.Error("Terjadi kesalahan jaringan")
            } catch (e: HttpException) {
                DetailUiState.Error("Terjadi kesalahan server")
            }
        }
    }
}