package com.jet.contact.model

data class Contact(
    val id: Long,
    val name: String,
    val number: String,
    val photoUri: String?,
    val accounts: Set<Account>
)
