# Лабораторная работа 18

### Задание 1

**Реализовано:**
- `@Singleton` для:
  - AppDatabase (база данных существует всё время жизни приложения)
  - TaskApi (Retrofit клиент)
  - TaskRepositoryImpl (репозиторий)
  - LocalTaskDataSource и RemoteTaskDataSource
  - UseCase классы

**Обоснование:** 
Эти объекты должны существовать в единственном экземпляре на всё приложение для экономии ресурсов и обеспечения консистентности данных.

### Задание 2

**Реализовано:**

1. **Квалификаторы:**
```kotlin
@Qualifier
annotation class LocalDataSource

@Qualifier
annotation class RemoteDataSource
```

2. **Источники данных:**
  - LocalTaskDataSource - работа с Room базой данных
  - RemoteTaskDataSource - работа с Retrofit API
3. **Репозиторий использует оба источника:**
  - При загрузке задач сначала получает данные из сети
  - Сохраняет их локально в базу
  - Предоставляет Flow для наблюдения за локальными данными

### Задание 3

**CoroutineDispatchers:**
```kotlin
@Qualifier
annotation class IoDispatcher

@Qualifier
annotation class DefaultDispatcher
```

**BaseUrl:**
```kotlin
@Qualifier
annotation class BaseUrl

@Provides
@BaseUrl
fun provideBaseUrl(): String = "https://jsonplaceholder.typicode.com/"
```

**Использование в ViewModel:**
```kotlin
@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    // Использование ioDispatcher вместо Dispatchers.IO
}
```

### Задание 4

```kotlin
@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel()
```

**Добавлены зависимости:**
  - UseCase для получения задач
  - UseCase для добавления задач
  - CoroutineDispatcher для фоновых операций

<img width="498" height="650" alt="image" src="https://github.com/user-attachments/assets/a72d507f-ab1c-48e7-9529-362d5b0748f3" />

<img width="328" height="231" alt="image" src="https://github.com/user-attachments/assets/deb062fb-7a1f-4e9f-9253-999ba6151936" />

<img width="414" height="632" alt="image" src="https://github.com/user-attachments/assets/86cd4bc6-dfa0-4740-9665-344d5f453d42" />


