package com.github.di

import com.github.ringtonesmartkit.viewmodules.ContactPickerViewModel
import com.github.ringtonesmartkit.viewmodules.ContactViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val  ViewModelModules = module {
    singleOf(::ContactViewModel)
    singleOf(::ContactPickerViewModel)
}