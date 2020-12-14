package com.mobilehealthsports.vaccinepass.util
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("android:boolean_visibility")
fun booleanVisibility(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("android:object_visibility")
fun objectVisibility(view: View, item: Any?) {
    view.visibility = if (item != null) View.VISIBLE else View.GONE
}

@BindingAdapter("android:src")
fun loadImage(view: ImageView, resource: Int) {
    view.setImageResource(resource)
}
