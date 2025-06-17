package com.jet.contact.model

import androidx.annotation.DrawableRes
import com.jet.contact.R

enum class Account(
    val ACCOUNT_TYPE: String,
    @DrawableRes val ICON_RES: Int)
{
    WHATSAPP("com.whatsapp",R.drawable.whatsapp),
    TELEGRAM("org.telegram.messenger",R.drawable.telegram),
    SIGNAL("org.thoughtcrime.securesms",R.drawable.signal)
}