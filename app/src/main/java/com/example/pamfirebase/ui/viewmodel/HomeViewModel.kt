package com.example.pamfirebase.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pamfirebase.model.Mahasiswa
import com.example.pamfirebase.repository.RepositoryMhs
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeViewModel (
    private val mhs: RepositoryMhs
): ViewModel(){
    fun getMhs(){
        viewModelScope.launch{
            mhs.getAllMahasiswa().onStart{
                mhsUiState = HomeUiState.Loading
            }
                .catch{
                    mhsUiState = HomeUiState.Error(it)
                }
                .collect{
                    mhsUiState = if(it.isEmpty()){
                        HomeUiState.Error(Exception("Belum ada data mahasiswa"))
                    } else{
                        HomeUiState.Success
                    }
                }
        }
    }
}

sealed class HomeUiState{
    //Loading
    object Loading: HomeUiState()
    //sukses
    data class Success(val data: List<Mahasiswa>): HomeUiState()
    //eror
    data class Error(val e: Throwable): HomeUiState()

}