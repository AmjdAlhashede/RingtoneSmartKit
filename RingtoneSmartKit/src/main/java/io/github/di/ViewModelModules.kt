package io.github.di

import io.github.ringtonesmartkit.viewmodules.ContactPickerViewModel
import io.github.ringtonesmartkit.viewmodules.ContactViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val  ViewModelModules = module {
    singleOf(::ContactViewModel)
    singleOf(::ContactPickerViewModel)
}