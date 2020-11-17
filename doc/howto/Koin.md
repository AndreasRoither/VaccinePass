# Koin for kotlin

## Setup

Add coin dependecy to `(App)build.gradle`:
```gradle
// Koin for Android
def koin_version = "2.1.6"
implementation "org.koin:koin-android:$koin_version"
// Koin for Lifecycle scoping
implementation "org.koin:koin-androidx-scope:$koin_version"
// Koin for Android Architecture ViewModel
implementation "org.koin:koin-androidx-viewmodel:$koin_version"
```

## Usage

### Declare a module

```kotlin
// Given some classes 
class Controller(val service : BusinessService) 
class BusinessService() 

// just declare it 
val myModule = module { 
  single { Controller(get()) } 
  single { BusinessService() } 
} 
```

### Start koin

```kotlin
fun main(vararg args : String) { 
  // start Koin!
  startKoin {
    // declare modules
    modules(myModule)
  }
} 
```

### Injection

```kotlin
// Just inject in a simple Activity 
class MyActivity() : AppCompatActivity() {

    // lazy inject BusinessService into property
    val service : BusinessService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // or directly get any instance
        val service : BusinessService = get()
    }
}
```

### Inject by constructor

```kotlin
// Controller & BusinessService are declared in a module
class Controller(val service : BusinessService){ 
  
  fun hello() {
     // service is ready to use
     service.sayHello()
  }
} 
```

### ViewModel

```kotlin
// Injected by constructor
class MyViewModel(val repo : MyRepository) : ViewModel()

// declared ViewModel using the viewModel keyword
val myModule : Module = module {
  viewModel { MyViewModel(get()) } 
  single { MyRepository() }
}

// Just get it
class MyActivity() : AppCompatActivity() {

  // lazy inject MyViewModel
  val myViewModel : MyViewModel by viewModel()
}
```

### Testing with Koin

```kotlin
// Just tag your class with KoinTest to unlick your testing power
class SimpleTest : KoinTest { 
  
  // lazy inject BusinessService into property
  val service : BusinessService by inject()

  @Test
  fun myTest() {
      // You can start your Koin configuration
      startKoin { modules(myModules) }

      // or directly get any instance
      val service : BusinessService = get()
  }
} 
```
