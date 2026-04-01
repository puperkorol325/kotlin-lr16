# Лабораторная работа 18: DI (Hilt) - Часть 2

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
