package com.example.core.extensions.viewbinding

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty

inline fun <reified VB : ViewBinding> Fragment.viewBinding(): ReadOnlyProperty<Fragment, VB> =
    FragmentViewBindingProperty(this, VB::class.java)


inline fun <reified VB : ViewBinding> AppCompatActivity.viewBinding(): ReadOnlyProperty<AppCompatActivity, VB> =
    ActivityViewBindingProperty(this, VB::class.java)

inline val <reified VB : ViewBinding> VB.context: Context
    get() = root.context
