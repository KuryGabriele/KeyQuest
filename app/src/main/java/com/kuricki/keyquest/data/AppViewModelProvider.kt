package com.kuricki.keyquest.data

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kuricki.keyquest.KeyquestApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            LoginViewModel(
                keyquestApplication().container.userSessionRepository
            )
        }
    }
}

fun CreationExtras.keyquestApplication(): KeyquestApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as KeyquestApplication)