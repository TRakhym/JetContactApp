package com.jet.contact.domain

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jet.contact.data.utils.ContactsRepository
import com.jet.contact.model.Contact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    contactsRepository: ContactsRepository = ContactsRepository(),
    context: Context,
) : ViewModel() {

    private val _contacts: MutableStateFlow<List<Contact>> = MutableStateFlow(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts

    init {
        viewModelScope.launch {
            val contactList = contactsRepository.getContacts(context)
            _contacts.value = contactList
        }
    }

}