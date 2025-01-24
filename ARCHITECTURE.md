# ARCHITECTURE

## Overview

This project combines**Clean Architecture**with**MVI (Model-View-Intent)**to achieve separation of
concerns, testability, and reliability in handling one-time events. It demonstrates two MVI patterns
to address architectural challenges in real-world scenarios:

1. **Structured MVI**: Uses explicit`Contract`classes and`Channel`for effects.
2. **Simplified MVI**: Uses state-driven effects with`StateFlow`.

---

## Clean Architecture

### Layers and Responsibilities

The app is organized into three layers following Clean Architecture principles:

| **Layer**        | **Path**        | **Responsibility**                 | **Components**                                                                 |
|------------------|-----------------|------------------------------------|--------------------------------------------------------------------------------|
| **Data**         | `core/data`     | Data retrieval and persistence.    | Repositories, API services, Room DAOs, DTOs (e.g.,`WonderResponse`).           |
| **Domain**       | `core/domain`   | Business logic and rules.          | Entities (e.g.,`User`,`Wonder`), use cases, repository interfaces.             |
| **Presentation** | `features/*/ui` | UI rendering and user interaction. | ViewModels, MVI contracts (`State`/`Event`/`Effect`), Jetpack Compose screens. |

### Dependency Flow

```
Presentation → Domain ← Data
```

- **Presentation**depends on**Domain**(use cases, entities).
- **Domain**defines interfaces for**Data**to implement (e.g.,`WondersRepository`).
- **Data**depends on Android frameworks (Room, Retrofit, Firebase).

---

## MVI Implementation Details

### Base Architecture Components

```kotlin
// Core MVI Interfaces
interface ViewState
interface ViewEvent
interface ViewEffect

// Base ViewModel with Channel for Effects
abstract class BaseViewModel<State : ViewState, Event : ViewEvent, Effect : ViewEffect>(
    initialState: State
) : ViewModel() {
    private val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<Event>()
    private val _effects = Channel<Effect>(Channel.CONFLATED)

    val effect = _effects.receiveAsFlow()

    init { subscribeToEvents() }

    private fun subscribeToEvents() {
        viewModelScope.launch {
            _event.collect { handleEvents(it) }
        }
    }

    fun setEvent(event: Event) {
        viewModelScope.launch { _event.emit(event) }
    }

    protected fun setState(reducer: State.() -> State) {
        _state.update { it.reducer() }
    }

    protected fun setEffect(builder: () -> Effect) {
        viewModelScope.launch { _effects.send(builder()) }
    }

    abstract fun handleEvents(event: Event)
}
```

---

## Two MVI Flavors

### 1. Structured MVI (Channel-based Effects)

#### Characteristics

- **Explicit separation**of State/Event/Effect
- **Channel**for one-time effects
- **StateFlow**for state management
- Used in complex screens (`Home`,`SignIn`,`Detail`)

#### Implementation

```kotlin
// Contract Definition
object HomeContract {
    data class State(val loading: Boolean, val wonders: List<Wonder>) : ViewState
    sealed class Event : ViewEvent { data class OnItemClick(val wonder: Wonder) : Event() }
    sealed class Effect : ViewEffect { data class NavigateToDetail(val wonder: Wonder) : Effect() }
}

// ViewModel Implementation
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWondersUseCase: GetWondersUseCase
) : BaseViewModel<HomeContract.State, HomeContract.Event, HomeContract.Effect>(
    HomeContract.State(loading = true, wonders = emptyList())
) {
    override fun handleEvents(event: HomeContract.Event) {
        when (event) {
            is HomeContract.Event.OnItemClick -> {
                setEffect { HomeContract.Effect.NavigateToDetail(event.wonder) }
            }
        }
    }
    
    // State initialization logic...
}

// UI Layer
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeContract.Effect.NavigateToDetail -> 
                    handleNavigation(effect.wonder)
            }
        }
    }
}
```

#### Channel Characteristics

- **Capacity**:`CONFLATED`(only keeps latest effect)
- **Pros**:
    - Clear separation of concerns
    - Strict unidirectional flow
- **Cons**:
    - Risk of lost effects if no active collector
    - Requires explicit effect collection in UI

---

### 2. Simplified MVI (StateFlow-based Effects)

#### Characteristics

- **State-embedded effects**
- **Manual state reset**
- Used in simple screens (`Favorite`,`Profile`)

#### Implementation

```kotlin

// State with Embedded Effects
data class FavoriteState(
    val loading: Boolean = false,
    val wonders: List<Wonder> = emptyList(),
    val navigateDetail: Wonder? = null  // Effect as state property
) : ViewState

// ViewModel Implementation
@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(FavoriteState())
    val state = _state.asStateFlow()

    fun handleEvent(event: FavoriteEvent) {
        when (event) {
            is FavoriteEvent.OnItemClick -> 
                _state.update { it.copy(navigateDetail = event.wonder) }
            
            FavoriteEvent.ResetNavigation ->
                _state.update { it.copy(navigateDetail = null) }
        }
    }
}

// UI Layer
@Composable
fun FavoriteScreen(viewModel: FavoriteViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    LaunchedEffect(state.navigateDetail) {
        state.navigateDetail?.let { wonder ->
            navigateToDetail(wonder)
            viewModel.handleEvent(FavoriteEvent.ResetNavigation)
        }
    }
}
```

---

## Why Two Approaches?

### Structured MVI (Channel-based)

**Best For**:

- Complex screens with multiple side effects
- Features requiring strict architecture
- Teams familiar with MVI patterns
  **Trade-offs**:

```text
✅ Clear effect separation
✅ Better testability
❌ Risk of lost effects
❌ More boilerplate
```

### Simplified MVI (StateFlow-based)

**Best For**:

- Simple screens with few effects
- Rapid prototyping
- Junior developers learning MVI
  **Trade-offs**:

```text
✅ Survives configuration changes
✅ Less boilerplate
❌ Manual state cleanup
❌ Less explicit effect tracking
```

---

## One-Time Event Handling Comparison

| Aspect                | Structured MVI (Channel)     | Simplified MVI (StateFlow)     |
|-----------------------|------------------------------|--------------------------------|
| **Event Persistence** | Lost if no active collector  | Survives configuration changes |
| **Recomposition**     | Risk of duplicate processing | Safe with proper reset         |
| **Boilerplate**       | High (separate Effect class) | Low (state properties)         |
| **Testability**       | Easy (explicit effects)      | Harder (state inspection)      |
| **Learning Curve**    | Steeper                      | Gentler                        |

---

## Key Architectural Decisions

1. **Channel Configuration**  
   Using`CONFLATED`capacity ensures:
    - Only latest effect is preserved
    - Backpressure handling for rapid events
    - Minimal memory footprint
2. **StateFlow for State**  
   Guarantees:
    - Always has a current value
    - Survives configuration changes
    - Efficient UI updates
3. **Hybrid Approach**  
   Combines benefits of both patterns:
    - Strict architecture where needed
    - Pragmatic simplicity for trivial flows
    - Real-world demonstration of trade-offs

---

## Conclusion

This project demonstrates how**Clean Architecture**and**MVI**can coexist to build robust Android
apps. By addressing one-time event challenges with both`Channel`and`StateFlow`, it balances strict
architecture with real-world reliability, making it a valuable learning resource.