package com.example.core.extensions.viewbinding

import android.app.Activity
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@Suppress("UNCHECKED_CAST")
class ActivityViewBindingProperty<T : ViewBinding>(
    private val activity: AppCompatActivity,
    private val bindingClass: Class<T>
) : ReadOnlyProperty<Activity, T>, DefaultLifecycleObserver {
    private var binding: T? = null
        get() {
            if (field == null)
                field = bindingClass.getMethod("inflate", LayoutInflater::class.java)
                    .invoke(null, activity.layoutInflater) as T
            return field
        }

    private fun requireBinding() = binding
        ?: throw IllegalStateException("Use viewBinding between onCreate and onDestroy")

    init {
        activity.lifecycle.addObserver(this)
    }


    override fun onCreate(owner: LifecycleOwner) {
        activity.setContentView(requireBinding().root)
        super.onCreate(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        activity.lifecycle.removeObserver(this)
        binding = null
        super.onDestroy(owner)
    }

    override fun getValue(thisRef: Activity, property: KProperty<*>): T {
        return requireBinding()
    }

}