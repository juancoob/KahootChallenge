# KahootChallenge 🎲
A simple quiz game that uses a Kahoot API.

## How to build it? 🔧
Clone the project and run it on Android Studio.

## How is it done? 👷
I have used a **Clean Architecture** by **layers**. That means the project has some modules to separate their scope in that order:
- **app** is the Android module, and there are Android frameworks and data source implementations.
- **usecases** is a Kotlin module that stores use cases for the Android module.
- **data** is a Kotlin module that stores repositories and datasources.
- **domain** is a Kotlin module that stores data classes to save the data the app collects from the server and database.
- **testshared** is a Kotlin module that stores mocked local models for testing purposes and is only accessible from **app**, **usecases** and **data**.
- **buildSrc** is a kotlin directory that stores shared dependencies for all modules.

The presentation pattern used is **MVVM** to survive configuration changes.

## Reasoning 🤔
- I have used a clean architecture to separate layers and have a scalable project.
- I have implemented MVVM as a design pattern because it is easier to follow the UDF.
- I have used Kotlin **coroutines** because we can run many on a single thread due to the suspension that doesn't block the main thread and are the standard way in Android.
- I have used **Kotlin serialization** because it comes from the Kotlin language to serialize data from the REST API service.
- I have used **Retrofit** as an API REST client because it is recommended and easy to use.
- I have managed the **Arrow** library to handle errors and data because it is a powerful and straightforward library.
- I have applied **Hilt** as DI and **Room** as DB because they are the recommended options by google for Android projects.
- I have added Glide to load image resources due to its stability and use.
- I have coded the project as understandable as possible to be comprehensible by following `SOLID` principles. As _Robert C. Martin_ says, no code documentation is needed if the code is clear enough.
- I have created unit tests to test all the functionality.
- I have used **turbine** to test hot flows because it is the current standard library.
- I have added the Gradle plugin library from Ben Manes because it is a well-known library to update dependencies used in different modules.

## TO-DO 👨‍🔧
- Improve MainAdapter
- Create a custom progress bar view to show how much time the user has. Currently, the project uses a LinearProgressIndicator.
- Add MainState to separate the UI state with the ui rendering
