# Databinding for kotlin

## Setup
Add flag to the `(App)build.gradle` inside `android{}` section
```gradle
dataBinding {
        enabled = true
    }
```

## Usage

Inside `.xml` files:
Add the `data` tag which should be inside a `layout` tag. The tag can be empty in the beginning but should contain a `variable` tag later for the viewmodel.

```xml
<layout>
    <data></data>
</layout>
```

Data tag with viewModel:

```xml
<data>
    <variable name="user" type="com.example.User"/>
</data>
```

To use variables together with components you can use the specific `@{}` syntax:

```xml
<TextView android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          android:text="@{user.firstName}" />
```

There are alot more possibilities with custom expressions which you can look up here: <https://developer.android.com/topic/libraries/data-binding/expressions>

Now we have to set the viewModel / variable from the activity / fragment, in this example we set a click listener and inflate with the DataBindingUtil:

```kotlin
val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

setSupportActionBar(binding.toolbar)
binding.fab.setOnClickListener{view ->
    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        .setAction("Action", null).show()
}
```

For fragments:

```kotlin
val binding: FragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_layout, container, false);
```

## Databinding Extensions
You can add custom binding adapters to views etc. These can be particularly useful with a viewModel -> for example you can hide / show content without logic in the viewModel.
Example custom adapter:

```kotlin
@BindingAdapter("android:boolean_visibility")
fun booleanVisibility(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}
```

Usage for `android:boolean_visibility` (show or hide component):

```xml
<com.google.android.material.floatingactionbutton.FloatingActionButton
    android:id="@+id/fab"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="bottom|end"
    android:layout_margin="@dimen/fab_margin"
    android:boolean_visibility="@{true}"
    app:srcCompat="@android:drawable/ic_dialog_email" />
```
