@file:Suppress("UNCHECKED_CAST")

package com.example.core.extensions.viewbinding

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentViewBindingProperty<VB : ViewBinding>(
    private val fragment: Fragment,
    private val bindingClass: Class<VB>,
) : ReadOnlyProperty<Fragment, VB>, DefaultLifecycleObserver {

    private var binding: VB? = null

    init {
        fragment
            .viewLifecycleOwnerLiveData
            .observe(fragment) { lifecycleOwner ->
                lifecycleOwner.lifecycle.addObserver(this)
            }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        fragment.viewLifecycleOwner.lifecycle.removeObserver(this)
        binding = null
        super.onDestroy(owner)
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
        val view = checkNotNull(thisRef.view) {
            "ViewBinding is only valid between onCreateView and onDestroyView."
        }
        val binder = bindingClass.getMethod("bind", View::class.java)
        if (binding == null)
            binding = binder.invoke(null, view) as? VB
        return this.binding ?: throw IllegalStateException("ViewBinding is null!")
    }
}