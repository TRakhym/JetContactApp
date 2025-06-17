package com.jet.contact.data.utils

import android.content.Context
import android.provider.ContactsContract
import com.jet.contact.model.Account
import com.jet.contact.model.Contact

class ContactsRepository {

    fun getContacts(context: Context): List<Contact> {
        val contacts = mutableListOf<Contact>()

        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
        )

        cursor?.use {
            val idIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val id = it.getLong(idIndex)
                val name = it.getString(nameIndex)
                val number = it.getString(numberIndex)
                val photoUri = getPhoto(context, id)
                val accounts = getSocialAccounts(context, id)
                var needToAdd = true
                for(contact in contacts){
                    if(contact.name.equals(name)){
                        contact.copy(accounts = accounts.plus(accounts))
                        needToAdd = false
                    }
                }
                if(needToAdd){
                    contacts.add(Contact(id, name, number, photoUri, accounts))
                }
            }
        }
        return contacts
    }

    private fun getPhoto(context: Context, contactId: Long): String? {
        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.PHOTO_URI
        )

        val cursor = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            "${ContactsContract.Contacts._ID}=?",
            arrayOf(contactId.toString()),
            null
        )

        cursor?.use {
            val photoUriIndex = it.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)

            while (it.moveToNext()) {
                return it.getString(photoUriIndex)
            }
        }
        return null
    }

    private fun getSocialAccounts(context: Context, contactId: Long): Set<Account> {
        val accounts = mutableSetOf<Account>()

        val projection = arrayOf(
            ContactsContract.RawContacts.CONTACT_ID,
            ContactsContract.RawContacts.ACCOUNT_TYPE
        )

        val cursor = context.contentResolver.query(
            ContactsContract.RawContacts.CONTENT_URI,
            projection,
            "${ContactsContract.RawContacts.CONTACT_ID}=?",
            arrayOf(contactId.toString()),
            "${ContactsContract.RawContacts.ACCOUNT_TYPE} ASC"
        )

        cursor?.use {
            val accountTypeIndex = it.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE)

            while (it.moveToNext()) {
                val accountType = it.getString(accountTypeIndex)

                when(accountType){
                    Account.WHATSAPP.ACCOUNT_TYPE -> accounts.add(Account.WHATSAPP)
                    Account.TELEGRAM.ACCOUNT_TYPE -> accounts.add(Account.TELEGRAM)
                    Account.SIGNAL.ACCOUNT_TYPE -> accounts.add(Account.SIGNAL)
                }
            }
        }

        return accounts
    }

}