<h1 id="clean-architecture-mvvmmvi-jetpack-compose">Clean Architecture MVVM/MVI — Jetpack Compose</h1>
<p>A production-ready Android application architecture built with <strong>Kotlin</strong>, <strong>Jetpack Compose</strong>, <strong>Clean Architecture</strong>, <strong>MVVM/MVI-style unidirectional data flow</strong>, <strong>Dagger Hilt</strong>, <strong>StateFlow</strong>, <strong>Channel</strong>, and a <strong>multi-module Gradle structure</strong>.</p>
<p>The project is designed for scalability, testability, strict separation of concerns, and independent feature development.</p>
<hr />
<h2 id="architecture-overview">Architecture Overview</h2>
<p>The application follows a strict dependency direction:</p>
<pre><code class="language-text">                         ┌──────────────────────┐
                         │        :app          │
                         │ Root / DI / NavHost  │
                         └──────────┬───────────┘
                                    │
               ┌────────────────────┼────────────────────┐
               │                    │                    │
               ▼                    ▼                    ▼
         :feature:*         :core:navigation    :core:designsystem
               │
               ▼
          :core:domain
               ▲
               │
          :core:data
           /       \
          ▼         ▼
 :core:network   :core:database
</code></pre>
<p>The most important architectural rule is:</p>
<blockquote>
<p><strong>The domain layer is pure Kotlin and has ZERO Android/framework dependencies.</strong></p>
</blockquote>
<hr />
<h1 id="goals">Goals</h1>
<p>This architecture aims to provide:</p>
<ul>
<li>Strict Clean Architecture boundaries</li>
<li>Package-by-feature organization</li>
<li>Scalable multi-module structure</li>
<li>Unidirectional data flow</li>
<li>Immutable UI state</li>
<li>Predictable one-off side effects</li>
<li>Type-safe navigation</li>
<li>Constructor-based dependency injection</li>
<li>Testable business logic</li>
<li>Clear repository boundaries</li>
<li>Offline-capable data orchestration</li>
<li>Fast incremental builds</li>
<li>Minimal architectural boilerplate</li>
<li>Strong compile-time dependency validation</li>
</ul>
<hr />
<h1 id="tech-stack">Tech Stack</h1>
<table>
<thead>
<tr>
<th>Technology</th>
<th>Purpose</th>
</tr>
</thead>
<tbody>
<tr>
<td>Kotlin</td>
<td>Primary language</td>
</tr>
<tr>
<td>Jetpack Compose</td>
<td>UI</td>
</tr>
<tr>
<td>Material 3</td>
<td>Design system</td>
</tr>
<tr>
<td>Navigation Compose</td>
<td>Navigation</td>
</tr>
<tr>
<td>Kotlin Serialization</td>
<td>Type-safe navigation</td>
</tr>
<tr>
<td>StateFlow</td>
<td>Persistent UI state</td>
</tr>
<tr>
<td>Channel</td>
<td>One-off UI effects</td>
</tr>
<tr>
<td>Coroutines</td>
<td>Async/concurrent operations</td>
</tr>
<tr>
<td>Hilt</td>
<td>Dependency Injection</td>
</tr>
<tr>
<td>Retrofit</td>
<td>Networking</td>
</tr>
<tr>
<td>OkHttp</td>
<td>HTTP client</td>
</tr>
<tr>
<td>Room</td>
<td>Local persistence</td>
</tr>
<tr>
<td>Turbine</td>
<td>Flow testing</td>
</tr>
<tr>
<td>JUnit</td>
<td>Unit testing</td>
</tr>
<tr>
<td>Gradle Kotlin DSL</td>
<td>Build system</td>
</tr>
<tr>
<td>Version Catalog</td>
<td>Dependency management</td>
</tr>
</tbody>
</table>
<hr />
<h1 id="module-structure">Module Structure</h1>
<pre><code class="language-text">.
├── app/
│
├── core/
│   ├── common/
│   ├── domain/
│   ├── data/
│   ├── network/
│   ├── database/
│   ├── designsystem/
│   └── navigation/
│
├── feature/
│   ├── auth/
│   ├── shift/
│   ├── drive/
│   └── home/
│
├── build-logic/
│
├── gradle/
│   └── libs.versions.toml
│
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
</code></pre>
<hr />
<h1 id="module-responsibilities">Module Responsibilities</h1>
<h2 id="app"><code>:app</code></h2>
<p>The application entry point.</p>
<p>Responsibilities:</p>
<ul>
<li>Application initialization</li>
<li>Hilt root</li>
<li>Root <code>NavHost</code></li>
<li>Global navigation</li>
<li>Global application state</li>
<li>Authentication routing</li>
<li>Wiring feature modules</li>
<li>Android-specific application configuration</li>
</ul>
<p>Example:</p>
<pre><code class="language-kotlin">@HiltAndroidApp
class CleanArchitectureApplication : Application()
</code></pre>
<hr />
<h2 id="coredomain"><code>:core:domain</code></h2>
<p>The most isolated layer in the project.</p>
<h3 id="contains">Contains</h3>
<ul>
<li>Domain models</li>
<li>Repository contracts</li>
<li>Business rules</li>
<li>Domain errors</li>
<li>UseCases where justified</li>
<li>Domain-level abstractions</li>
</ul>
<h3 id="must-not-contain">Must NOT contain</h3>
<pre><code class="language-text">android.*
androidx.*
androidx.compose.*
androidx.lifecycle.*
dagger.*
hilt.*
retrofit.*
okhttp.*
room.*
</code></pre>
<p>The domain layer must remain usable from a pure Kotlin/JVM environment.</p>
<p>Example:</p>
<pre><code class="language-kotlin">data class HomeItem(
    val id: Long,
    val title: String,
)
</code></pre>
<p>Repository contract:</p>
<pre><code class="language-kotlin">interface HomeRepository {
    suspend fun getItems(): Result&lt;List&lt;HomeItem&gt;, DomainError&gt;
}
</code></pre>
<hr />
<h1 id="coredata"><code>:core:data</code></h1>
<p>Responsible for implementing domain repository contracts.</p>
<p>Responsibilities:</p>
<ul>
<li>Repository implementations</li>
<li>Remote/local orchestration</li>
<li>Offline-first behavior</li>
<li>Cache policies</li>
<li>Synchronization</li>
<li>Data mapping coordination</li>
</ul>
<p>Example:</p>
<pre><code class="language-kotlin">internal class HomeRepositoryImpl(
    private val remote: HomeRemoteDataSource,
    private val local: HomeLocalDataSource,
) : HomeRepository
</code></pre>
<p>The implementation remains internal whenever possible.</p>
<hr />
<h1 id="corenetwork"><code>:core:network</code></h1>
<p>Responsible for all network infrastructure.</p>
<pre><code class="language-text">network/
├── api/
├── dto/
├── mapper/
├── interceptor/
├── di/
└── error/
</code></pre>
<p>Responsibilities:</p>
<ul>
<li>Retrofit</li>
<li>OkHttp</li>
<li>API interfaces</li>
<li>DTOs</li>
<li>Serialization</li>
<li>HTTP interceptors</li>
<li>Network-specific errors</li>
</ul>
<p>Network models must never leak into the domain layer.</p>
<pre><code class="language-text">DTO
 ↓
Mapper
 ↓
Domain Entity
</code></pre>
<hr />
<h1 id="coredatabase"><code>:core:database</code></h1>
<p>Responsible for local persistence.</p>
<pre><code class="language-text">database/
├── dao/
├── entity/
├── mapper/
├── database/
└── di/
</code></pre>
<p>Responsibilities:</p>
<ul>
<li>Room</li>
<li>DAOs</li>
<li>Local entities</li>
<li>Local data sources</li>
<li>Database configuration</li>
</ul>
<p>Room entities must never be exposed directly to presentation.</p>
<pre><code class="language-text">Room Entity
     ↓
 Mapper
     ↓
Domain Entity
</code></pre>
<hr />
<h1 id="coredesignsystem"><code>:core:designsystem</code></h1>
<p>Shared Compose UI infrastructure.</p>
<pre><code class="language-text">designsystem/
├── theme/
├── component/
├── button/
├── input/
├── dialog/
├── snackbar/
├── loading/
├── icon/
├── typography/
└── preview/
</code></pre>
<p>Contains:</p>
<ul>
<li>Material 3 theme</li>
<li>Colors</li>
<li>Typography</li>
<li>Shapes</li>
<li>Reusable components</li>
<li>Common UI patterns</li>
</ul>
<p>Does <strong>not</strong> contain:</p>
<ul>
<li>Business logic</li>
<li>ViewModels</li>
<li>Repositories</li>
<li>UseCases</li>
<li>Feature navigation decisions</li>
</ul>
<hr />
<h1 id="corenavigation"><code>:core:navigation</code></h1>
<p>Defines application-level navigation contracts.</p>
<p>Example:</p>
<pre><code class="language-kotlin">@Serializable
data object AuthRoute

@Serializable
data object ShiftRoute

@Serializable
data object DriveRoute
</code></pre>
<p>Navigation follows this direction:</p>
<pre><code class="language-text">ViewModel
    ↓
UiEvent.Navigate(...)
    ↓
Compose UI
    ↓
NavHost / Navigator
</code></pre>
<p>ViewModels never receive or access <code>NavController</code>.</p>
<hr />
<h1 id="corecommon"><code>:core:common</code></h1>
<p>Contains genuinely cross-cutting utilities.</p>
<p>Examples:</p>
<ul>
<li>Dispatcher abstraction</li>
<li>Common Kotlin extensions</li>
<li>Shared utility types</li>
<li>Logging abstractions</li>
<li>Small framework-independent helpers</li>
</ul>
<p>Avoid turning <code>common</code> into a dumping ground.</p>
<p>Every class placed here must be genuinely cross-feature.</p>
<hr />
<h1 id="feature-architecture">Feature Architecture</h1>
<p>Features use <strong>package-by-feature</strong> organization.</p>
<p>Example:</p>
<pre><code class="language-text">feature/auth/
│
├── login/
│   ├── LoginScreen.kt
│   ├── LoginViewModel.kt
│   └── LoginContract.kt
│
├── register/
│   ├── RegisterScreen.kt
│   ├── RegisterViewModel.kt
│   └── RegisterContract.kt
│
└── navigation/
    └── AuthNavigation.kt
</code></pre>
<p>Other features:</p>
<pre><code class="language-text">feature/
├── auth/
├── shift/
├── drive/
└── home/
</code></pre>
<p>Feature modules must never depend directly on other feature modules.</p>
<hr />
<h1 id="ui-architecture">UI Architecture</h1>
<p>Every screen follows a unidirectional data flow:</p>
<pre><code class="language-text">┌──────────────┐
│   User Input │
└──────┬───────┘
       ▼
┌──────────────┐
│   UiEvent    │
└──────┬───────┘
       ▼
┌──────────────┐
│  ViewModel   │
└──────┬───────┘
       ▼
┌──────────────┐
│   UseCase    │
└──────┬───────┘
       ▼
┌──────────────┐
│ Repository   │
└──────┬───────┘
       ▼
┌────────────────────┐
│ Remote / Local     │
└───────┬────────────┘
        ▼
┌──────────────┐
│  UI State    │
└──────┬───────┘
       ▼
┌──────────────┐
│    Compose   │
└──────────────┘
</code></pre>
<hr />
<h1 id="ui-state">UI State</h1>
<p>Persistent screen state is represented using <code>StateFlow</code>.</p>
<p>Example:</p>
<pre><code class="language-kotlin">@Immutable
data class HomeUiState(
    val isLoading: Boolean = false,
    val items: ImmutableList&lt;HomeItemUi&gt; = persistentListOf(),
    val errorMessage: String? = null,
)
</code></pre>
<p>Expose only immutable state:</p>
<pre><code class="language-kotlin">val uiState: StateFlow&lt;HomeUiState&gt;
</code></pre>
<p>Never expose <code>MutableStateFlow</code> publicly.</p>
<hr />
<h1 id="one-off-side-effects">One-Off Side Effects</h1>
<p>Transient events use <code>Channel</code>.</p>
<p>Examples:</p>
<ul>
<li>Navigation</li>
<li>Snackbar</li>
<li>Toast</li>
<li>Opening an external screen</li>
<li>Temporary UI actions</li>
</ul>
<p>Example:</p>
<pre><code class="language-kotlin">private val _events = Channel&lt;HomeUiEvent&gt;(Channel.BUFFERED)

val events = _events.receiveAsFlow()
</code></pre>
<p>Example contract:</p>
<pre><code class="language-kotlin">sealed interface HomeUiEvent {

    data object NavigateToDetails : HomeUiEvent

    data class ShowMessage(
        val message: String,
    ) : HomeUiEvent
}
</code></pre>
<p>Persistent state belongs in <code>StateFlow</code>.</p>
<p>One-time effects belong in <code>Channel</code>.</p>
<p>Do not mix the two responsibilities.</p>
<hr />
<h1 id="domain-result">Domain Result</h1>
<p>Expected domain failures are represented explicitly.</p>
<pre><code class="language-kotlin">sealed interface Result&lt;out D, out E : DomainError&gt; {

    data class Success&lt;out D&gt;(
        val data: D,
    ) : Result&lt;D, Nothing&gt;

    data class Error&lt;out E : DomainError&gt;(
        val error: E,
    ) : Result&lt;Nothing, E&gt;
}
</code></pre>
<p>Domain errors remain framework independent.</p>
<p>Example:</p>
<pre><code class="language-kotlin">sealed interface DomainError

sealed interface NetworkError : DomainError {
    data object Timeout : NetworkError
    data class Server(val code: Int) : NetworkError
}

sealed interface AuthError : DomainError {
    data object InvalidCredentials : AuthError
}
</code></pre>
<p>Infrastructure exceptions must be mapped before crossing the domain boundary.</p>
<hr />
<h1 id="usecase-policy">UseCase Policy</h1>
<p>UseCases are intentionally <strong>pragmatic</strong>.</p>
<p>Create a UseCase when it:</p>
<ul>
<li>Encapsulates business rules</li>
<li>Orchestrates multiple repositories</li>
<li>Represents an important application operation</li>
<li>Requires isolated testing</li>
<li>Performs meaningful domain transformation</li>
</ul>
<p>Do not create pointless pass-through UseCases.</p>
<p>Avoid:</p>
<pre><code class="language-kotlin">class GetUserUseCase(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(id: Long) =
        repository.getUser(id)
}
</code></pre>
<p>when the class adds no meaningful behavior.</p>
<p>Prefer useful operations such as:</p>
<pre><code class="language-kotlin">class CalculateDriverEarningsUseCase(...)
</code></pre>
<p>or:</p>
<pre><code class="language-kotlin">class SubmitShiftUseCase(...)
</code></pre>
<hr />
<h1 id="dependency-injection">Dependency Injection</h1>
<p>The project uses <strong>Dagger Hilt</strong>.</p>
<p>Application:</p>
<pre><code class="language-kotlin">@HiltAndroidApp
class CleanArchitectureApplication : Application()
</code></pre>
<p>Activity:</p>
<pre><code class="language-kotlin">@AndroidEntryPoint
class MainActivity : ComponentActivity()
</code></pre>
<p>DI modules belong near the implementation they configure.</p>
<p>Example:</p>
<pre><code class="language-text">core/network/di/
core/database/di/
core/data/di/
app/di/
</code></pre>
<p>Use <code>@Provides</code> for third-party objects:</p>
<pre><code class="language-kotlin">@Provides
@Singleton
fun provideRetrofit(...): Retrofit
</code></pre>
<p>Use <code>@Binds</code> for interface implementations:</p>
<pre><code class="language-kotlin">@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindHomeRepository(
        implementation: HomeRepositoryImpl,
    ): HomeRepository
}
</code></pre>
<p>Never put Hilt or Dagger annotations in the pure domain layer.</p>
<hr />
<h1 id="dispatcher-injection">Dispatcher Injection</h1>
<p>Do not hardcode dispatchers in business logic.</p>
<p>Avoid:</p>
<pre><code class="language-kotlin">withContext(Dispatchers.IO)
</code></pre>
<p>inside a UseCase or ViewModel without architectural justification.</p>
<p>Prefer an injected dispatcher abstraction:</p>
<pre><code class="language-kotlin">interface AppDispatchers {
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val main: CoroutineDispatcher
}
</code></pre>
<p>Production:</p>
<pre><code class="language-text">IO      → Dispatchers.IO
Default → Dispatchers.Default
Main    → Dispatchers.Main
</code></pre>
<p>Testing:</p>
<pre><code class="language-text">→ StandardTestDispatcher
</code></pre>
<p>This makes coroutine-based code deterministic and testable.</p>
<hr />
<h1 id="navigation">Navigation</h1>
<p>Navigation uses type-safe Navigation Compose with Kotlin Serialization.</p>
<p>Example:</p>
<pre><code class="language-kotlin">@Serializable
data object AuthRoute

@Serializable
data object ShiftRoute

@Serializable
data object DriveRoute
</code></pre>
<p>Parameterized destinations should use serializable arguments.</p>
<p>Navigation remains a UI/application responsibility.</p>
<p>Never do this in a ViewModel:</p>
<pre><code class="language-kotlin">navController.navigate(...)
</code></pre>
<p>Instead:</p>
<pre><code class="language-kotlin">sendEvent(
    HomeUiEvent.NavigateToDetails
)
</code></pre>
<p>and let the UI perform navigation.</p>
<hr />
<h1 id="testing">Testing</h1>
<p>Testing is a first-class architectural concern.</p>
<h2 id="required">Required</h2>
<ul>
<li>JUnit</li>
<li>Kotlin Coroutines Test</li>
<li>Turbine</li>
<li>Hand-written Fakes</li>
</ul>
<h2 id="avoid">Avoid</h2>
<ul>
<li>Mockito</li>
<li>MockK</li>
</ul>
<p>unless a specific technical requirement justifies them.</p>
<h3 id="viewmodel-tests-must-verify">ViewModel tests must verify</h3>
<ul>
<li>Initial state</li>
<li>Loading state</li>
<li>Success state</li>
<li>Error state</li>
<li>State transitions</li>
<li>One-off events</li>
</ul>
<p>Example:</p>
<pre><code class="language-kotlin">viewModel.uiState.test {
    assertThat(awaitItem()).isEqualTo(expectedInitialState)
}
</code></pre>
<p>Channel/effect testing should verify that transient events are emitted exactly as expected.</p>
<hr />
<h1 id="sample-home-feature">Sample Home Feature</h1>
<p>The repository contains a complete Home example demonstrating the architecture.</p>
<pre><code class="language-text">UI
 ↓
HomeViewModel
 ↓
GetHomeItemsUseCase
 ↓
HomeRepository
 ↓
HomeRepositoryImpl
 ↓
Remote / Local
 ↓
DTO / Entity
 ↓
Domain Entity
</code></pre>
<p>Example domain model:</p>
<pre><code class="language-kotlin">data class HomeItem(
    val id: Long,
    val title: String,
)
</code></pre>
<p>Feature structure:</p>
<pre><code class="language-text">feature/home/
├── HomeScreen.kt
├── HomeViewModel.kt
└── HomeContract.kt
</code></pre>
<p>Domain:</p>
<pre><code class="language-text">core/domain/home/
├── model/
│   └── HomeItem.kt
├── repository/
│   └── HomeRepository.kt
└── usecase/
    └── GetHomeItemsUseCase.kt
</code></pre>
<p>Data:</p>
<pre><code class="language-text">core/data/home/
├── HomeRepositoryImpl.kt
└── mapper/
</code></pre>
<p>Network:</p>
<pre><code class="language-text">core/network/home/
├── HomeApi.kt
└── HomeDto.kt
</code></pre>
<hr />
<h1 id="architecture-rules">Architecture Rules</h1>
<p>These rules are mandatory.</p>
<h3 id="domain">Domain</h3>
<pre><code class="language-text">❌ Android
❌ Compose
❌ Hilt
❌ Retrofit
❌ Room
❌ OkHttp
❌ ViewModel
❌ Context
❌ Resources
</code></pre>
<h3 id="presentation">Presentation</h3>
<pre><code class="language-text">✅ StateFlow
✅ Channel
✅ Compose
✅ ViewModel

❌ NavController inside ViewModel
❌ Retrofit DTOs
❌ Room entities
❌ Network calls directly from UI
</code></pre>
<h3 id="data">Data</h3>
<pre><code class="language-text">✅ Repository implementations
✅ Remote/local orchestration
✅ DTO/entity mapping

❌ UI logic
❌ Compose
❌ Navigation
</code></pre>
<h3 id="features">Features</h3>
<pre><code class="language-text">✅ Depend on domain
✅ Depend on designsystem
✅ Depend on navigation
✅ Own their presentation

❌ Depend on other feature modules
</code></pre>
<hr />
<h1 id="build-verification">Build Verification</h1>
<p>Architecture changes must be verified incrementally.</p>
<p>After every architectural task:</p>
<pre><code class="language-bash">./gradlew assembleDebug
</code></pre>
<p>The next task must not begin until the build succeeds.</p>
<p>Run tests:</p>
<pre><code class="language-bash">./gradlew test
</code></pre>
<p>Run lint:</p>
<pre><code class="language-bash">./gradlew lint
</code></pre>
<hr />
<h1 id="domain-purity-check">Domain Purity Check</h1>
<p>The domain module must be checked for forbidden dependencies.</p>
<p>Look for imports such as:</p>
<pre><code class="language-text">android.*
androidx.*
androidx.compose.*
androidx.lifecycle.*
dagger.*
javax.inject.*
retrofit.*
okhttp.*
room.*
</code></pre>
<p>Any such dependency is an architectural violation.</p>
<p>Expected:</p>
<pre><code class="language-text">DOMAIN PURITY: PASS
</code></pre>
<hr />
<h1 id="development-lifecycle">Development Lifecycle</h1>
<p>Architecture changes follow:</p>
<pre><code class="language-text">SPEC
  ↓
APPROVAL
  ↓
PLAN
  ↓
BUILD
  ↓
VERIFY
  ↓
REPORT
</code></pre>
<p>No implementation should begin before the specification is approved.</p>
<hr />
<h1 id="implementation-tasks">Implementation Tasks</h1>
<p>The initial project setup follows these sequential stages.</p>
<h2 id="foundation">1. Foundation</h2>
<p>Create:</p>
<ul>
<li>Result</li>
<li>DomainError</li>
<li>UI contracts</li>
<li>Event contracts</li>
<li>Dispatcher abstraction</li>
<li>Common utilities</li>
</ul>
<p>Verify:</p>
<pre><code class="language-bash">./gradlew assembleDebug
</code></pre>
<hr />
<h2 id="domain-1">2. Domain</h2>
<p>Create:</p>
<ul>
<li>Domain models</li>
<li>Repository contracts</li>
<li>UseCases</li>
<li>Business rules</li>
</ul>
<p>Verify:</p>
<pre><code class="language-bash">./gradlew assembleDebug
</code></pre>
<p>and confirm domain purity.</p>
<hr />
<h2 id="infrastructure">3. Infrastructure</h2>
<p>Create:</p>
<ul>
<li>Retrofit</li>
<li>OkHttp</li>
<li>Room</li>
<li>DAO</li>
<li>DTO</li>
<li>Local entities</li>
<li>Repository implementations</li>
<li>Hilt modules</li>
</ul>
<p>Verify:</p>
<pre><code class="language-bash">./gradlew assembleDebug
</code></pre>
<hr />
<h2 id="home-feature">4. Home Feature</h2>
<p>Create:</p>
<ul>
<li>HomeContract</li>
<li>HomeViewModel</li>
<li>HomeScreen</li>
<li>Home navigation</li>
<li>Hilt integration</li>
</ul>
<p>Verify:</p>
<pre><code class="language-bash">./gradlew assembleDebug
</code></pre>
<hr />
<h2 id="tests">5. Tests</h2>
<p>Create:</p>
<ul>
<li>Repository Fakes</li>
<li>UseCase tests</li>
<li>ViewModel tests</li>
<li>StateFlow tests</li>
<li>Channel tests</li>
</ul>
<p>Verify:</p>
<pre><code class="language-bash">./gradlew test
</code></pre>
<hr />
<h1 id="build-until-pass">Build Until Pass</h1>
<p>For every implementation task, use the following loop:</p>
<pre><code class="language-text">Implement
    ↓
./gradlew assembleDebug
    ↓
Failure?
 ┌──┴──┐
Yes    No
 ↓      ↓
Fix   Continue
 ↓
Build again
</code></pre>
<p>Never ignore compiler errors.</p>
<p>Never suppress errors simply to get a green build.</p>
<p>Never move architectural violations into another module merely to satisfy Gradle.</p>
<hr />
<h1 id="code-style">Code Style</h1>
<p>Prefer:</p>
<ul>
<li>Constructor injection</li>
<li>Immutable models</li>
<li><code>StateFlow</code></li>
<li><code>Channel</code></li>
<li><code>sealed interface</code></li>
<li>Small ViewModels</li>
<li>Stateless Composables</li>
<li>State hoisting</li>
<li>Explicit mapping</li>
<li>Structured concurrency</li>
<li>Lifecycle-aware collection</li>
</ul>
<p>Avoid:</p>
<ul>
<li>Global mutable state</li>
<li>Service locators</li>
<li>God ViewModels</li>
<li>God Base classes</li>
<li>Deep inheritance</li>
<li>Unnecessary abstraction</li>
<li>Feature-to-feature dependencies</li>
<li>Framework leakage into domain</li>
<li>Business logic inside Composables</li>
</ul>
<hr />
<h1 id="project-principles">Project Principles</h1>
<h2 id="separation-of-concerns">Separation of Concerns</h2>
<p>Each layer has exactly one primary responsibility.</p>
<h2 id="dependency-inversion">Dependency Inversion</h2>
<p>High-level business rules depend on contracts, not implementations.</p>
<h2 id="framework-isolation">Framework Isolation</h2>
<p>Business logic does not know Android exists.</p>
<h2 id="feature-independence">Feature Independence</h2>
<p>Features should evolve independently.</p>
<h2 id="explicit-state">Explicit State</h2>
<p>The UI is rendered from observable immutable state.</p>
<h2 id="predictable-effects">Predictable Effects</h2>
<p>One-off effects are emitted explicitly.</p>
<h2 id="testability">Testability</h2>
<p>Business logic and presentation logic must be executable under deterministic tests.</p>
<h2 id="minimal-abstraction">Minimal Abstraction</h2>
<p>Architecture should reduce complexity, not create ceremony.</p>
<hr />
<h1 id="recommended-package-convention">Recommended Package Convention</h1>
<p>Use the application namespace as the root:</p>
<pre><code class="language-text">com.leo.clean_mvvm_mvi
</code></pre>
<p>Then:</p>
<pre><code class="language-text">com.leo.clean_mvvm_mvi
├── app
├── core
└── feature
</code></pre>
<p>Example:</p>
<pre><code class="language-text">com.leo.clean_mvvm_mvi.feature.auth.login
com.leo.clean_mvvm_mvi.feature.shift
com.leo.clean_mvvm_mvi.feature.drive
com.leo.clean_mvvm_mvi.core.domain
com.leo.clean_mvvm_mvi.core.network
com.leo.clean_mvvm_mvi.core.database
</code></pre>
<hr />
<h1 id="definition-of-done">Definition of Done</h1>
<p>A feature is considered complete only when:</p>
<pre><code class="language-text">[ ] Architecture boundaries are respected
[ ] Domain remains framework independent
[ ] Repository contract lives in domain
[ ] Repository implementation lives in data
[ ] DTO/entity mappings are explicit
[ ] UI state uses StateFlow
[ ] One-off effects use Channel
[ ] ViewModel does not access NavController
[ ] DI graph resolves successfully
[ ] Compose code compiles
[ ] Unit tests pass
[ ] Lint passes where applicable
[ ] assembleDebug passes
</code></pre>
<hr />
<h1 id="final-verification">Final Verification</h1>
<p>Expected final report:</p>
<pre><code class="language-text">ARCHITECTURE STATUS: PASS

assembleDebug: PASS
tests: PASS
lint: PASS
domain purity: PASS
Hilt graph: PASS
navigation wiring: PASS
Compose compilation: PASS
</code></pre>
<p>The project is not considered production-ready until all mandatory checks pass.</p>
<hr />
<h1 id="architecture-philosophy">Architecture Philosophy</h1>
<p>This project follows a simple principle:</p>
<blockquote>
<p><strong>Make business logic independent, UI predictable, dependencies explicit, and features independently scalable.</strong></p>
</blockquote>
<p>Clean Architecture is used as a tool for controlling complexity—not as a reason to create unnecessary layers.</p>
<p>When an abstraction does not provide meaningful architectural value, do not create it.</p>
<p>When a dependency crosses a boundary, stop and reconsider the design.</p>
<p>When a build fails, fix the architectural or implementation problem before continuing.</p>
<hr />
<h2 id="license">License</h2>
<p>Add your project license here.</p>
