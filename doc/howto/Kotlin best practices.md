# Kotlin best practices

Original article: <https://phauer.com/2017/idiomatic-kotlin-best-practices/>

## Kotlin’s Built-in Support for Common Java Idioms and Patterns

| Java Idiom or Pattern | Idiomatic Solution in Kotlin |
|-|-|
| Optional | Nullable Types |
| Getter, Setter, Backing Field | Properties |
| Static Utility Class | Top-Level (extension) functions |
| Immutability | `data class` with immutable properties, `copy()` |
| Value Objects | `inline class` with immutable properties |
| Fluent Setter (Wither) | Named and default arguments, `apply()` |
| Method Chaining | Default arguments |
| Singleton | `object` |
| Delegation | Delegated properties `by` |
| Lazy Initialization (thread-safe) | Delegated properties `by: lazy()` |
| Observer | Delegated properties `by: Delegates.observable()` |

## Use expressions

Rule of thumb: Every time you write an `if` consider if it can be replaced with a more concise `when` expression.

```kotlin
fun getDefaultLocale2(deliveryArea: String) = when (deliveryArea.toLowerCase()) {
    "germany", "austria" -> Locale.GERMAN
    "usa", "great britain" -> Locale.ENGLISH
    "france" -> Locale.FRENCH
    else -> Locale.ENGLISH
}
```

`try-catch` is also a useful expression.

```kotlin
val json = """{"message":"HELLO"}"""
val message = try {
    JSONObject(json).getString("message")
} catch (ex: JSONException) {
    json
}
```

## Top level extension functions for utility functions

In Java, we often create static util methods in util classes. This would mean you would use an `object`in kotlin. However, Kotlin allows removing the unnecessary wrapping util class and use top-level functions instead. Often, we can additionally leverage extension functions, which increases readability. This way, our code feels more like “telling a story”.

```kotlin
fun String.countAmountOfX(): Int {
    return length - replace("x", "").length
}
"xFunxWithxKotlinx".countAmountOfX()
```

## Named arguments instead of fluent setter

Back in Java, fluent setters (also called “Wither”, similar to builder pattern where you return an instance of the class upon function call) where used to simulate named and default arguments and to make huge parameter lists more readable and less error-prone.
In Kotlin, named and default arguments fulfil the same propose but are built directly into the language:

```kotlin
       root = "~/folder",
       term = "game of thrones",
       recursive = true,
       followSymlinks = true
)
```

## Use apply() for grouping object initialization

The extension function `apply()` helps to group and centralize initialization code for an object. Besides, we don’t have to repeat the variable name over and over again.

```kotlin
val dataSource = BasicDataSource().apply {
    driverClassName = "com.mysql.jdbc.Driver"
    url = "jdbc:mysql://domain:3309/db"
    username = "username"
    password = "password"
    maxTotal = 40
    maxIdle = 40
    minIdle = 4
}
```

`apply()` is often useful when dealing with Java libraries in Kotlin.

## Don't overload for default arguments

Don’t overload methods and constructors to realize default arguments (so called “method chaining” or “constructor chaining”).
For this propose, Kotlin has named arguments:

```kotlin
fun find(name: String, recursive: Boolean = true){}
```

In fact, default arguments remove nearly all use cases for method and constructor overloading in general, because overloading is mainly used to create default arguments.

## Deal with nullability

Avoid `if-null` Checks.

Every time you write an `if-null` check, hold on. Kotlin provides much better ways to handle nulls. Often, you can use a null-safe call `?.` or the elvis operator `?:` instead.

```kotlin
val city = order?.customer?.address?.city ?: throw IllegalArgumentException("Invalid Order")
```

## Avoid `if-type` Checks

Using `as?` and `?:` we can check the type, (smart-)cast it and throw an exception if the type is not the expected one. All in one expression!

```kotlin
service as? CustomerService ?: throw IllegalArgumentException("No CustomerService")
service.getCustomer()
```

## Avoid not-null Assertions `!!`

"You may notice that the double exclamation mark looks a bit rude: it’s almost like you’re yelling at the compiler. This is intentional. The designers of Kotlin are trying to nudge you toward a better solution that doesn’t involve making assertions that can’t be verified by the compiler." -> Kotlin in Action by Dmitry Jemerov and Svetlana Isakova

## Consider `let()`

Sometimes, using `let()` can be a concise alternative for `if`. But you have to use it with sound judgment in order to avoid unreadable “train wrecks”. Nevertheless, I really want you to consider using `let()`.
With let(), there is no need for an extra variable. So we get along with one expression.

```kotlin
findOrder()?.let { dun(it.customer) }
//or
findOrder()?.customer?.let(::dun)
```

## Leverage Value Objects

With data classes, writing immutable value objects is so easy. Even for value objects containing only a single property. So there is no excuse for not using value objects anymore!

```kotlin
// Don't
fun send(target: String){}

// Do
fun send(target: EmailAddress){}
// expressive, readable, type-safe

data class EmailAddress(val value: String)
// Even better (Kotlin 1.3):
inline class EmailAddress(val value: String)
```

Since Kotlin 1.3, we should use `inline` classes for value objects. This way, we avoid the overhead of additional object creation because the compiler removes the wrapping inline class and uses the wrapped property directly. So it’s a free abstraction.

## Concise Mapping with Single Expression Functions

With single expression functions and named arguments we can write easy, concise and readable mappings between objects.

```kotlin
// Do
fun mapToDTO(entity: SnippetEntity) = SnippetDTO(
        code = entity.code,
        date = entity.date,
        author = "${entity.author.firstName} ${entity.author.lastName}"
)
val dto = mapToDTO(entity)
```

If you prefer extension functions, you can use them here to make both the function definition and the usage even shorter and more readable. At the same time, we don’t pollute our value object with the mapping logic.

```kotlin
// Do
fun SnippetEntity.toDTO() = SnippetDTO(
        code = code,
        date = date,
        author = "${author.firstName} ${author.lastName}"
)
val dto = entity.toDTO()
```

## Refer to Constructor Parameters in Property Initializers

Think twice before you define a constructor body (`init` block) only to initialize properties.
Note that we can refer to the primary constructor parameters in property initializers (and not only in the `init` block). `apply()` can help to group initialization code and get along with a single expression.

```kotlin
// Do
class UsersClient(baseUrl: String, appName: String) {
    private val usersUrl = "$baseUrl/users"
    private val httpClient = HttpClientBuilder.create().apply {
        setUserAgent(appName)
        setConnectionTimeToLive(10, TimeUnit.SECONDS)
    }.build()
    fun getUsers(){
        //call service using httpClient and usersUrl
    }
}
```

## Sealed Classes Instead of Exceptions

Especially for remote calls (like HTTP requests) the usage of a dedicated result class hierarchy can improve the safety, readability and traceability of the code.

```kotlin
// Definition
sealed class UserProfileResult {
    data class Success(val userProfile: UserProfileDTO) : UserProfileResult()
    data class Error(val message: String, val cause: Exception? = null) : UserProfileResult()
}

// Usage
val avatarUrl = when (val result = client.requestUserProfile(userId)) {
    is UserProfileResult.Success -> result.userProfile.avatarUrl
    is UserProfileResult.Error -> "http://domain.com/defaultAvatar.png"
}
```

Contrary to exceptions (which are always unchecked in Kotlin), the compiler guides you to handle the error cases. If you use `when` as an expression the compiler even forces you to handle the error case.
