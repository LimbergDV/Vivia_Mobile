# Vivia Mobile - Tutorial de Inicio

## 1. Descripción del proyecto

Vivia Mobile es una aplicación Android nativa desarrollada en Kotlin orientada a la gestión de propiedades inmobiliarias. Permite a arrendadores (lessors) y arrendatarios (lessees) interactuar en la plataforma. La aplicación incluye funcionalidades avanzadas como exploración de propiedades, autenticación segura, gestión de perfiles, sistema de seguidores y carga de imágenes fotográficas. Está diseñada para ser escalable, mantenible y ofrecer una experiencia de usuario fluida.

## 2. Requerimientos e instalación

### Requerimientos previos
* Android Studio (versión reciente recomendada compatible con AGP 9.0.1)
* Java Development Kit (JDK) 21
* Android SDK (Min SDK 26, Target SDK 36)
* Dispositivo físico o emulador con Android 8.0 (Oreo) o superior

### Instalación paso a paso

1. **Clonar el repositorio:**
   Abre tu terminal y ejecuta:
   ```bash
   git clone <url-del-repositorio>
   cd Vivia_Mobile
   ```

2. **Sincronizar el proyecto:**
   Puedes abrir el proyecto en Android Studio y dejar que sincronice automáticamente, o ejecutar desde la terminal:
   ```bash
   ./gradlew sync
   ```

3. **Compilar y verificar:**
   Para asegurar que todo el código y las dependencias están correctos:
   ```bash
   ./gradlew build
   ```

4. **Ejecutar la aplicación:**
   Con un dispositivo conectado o emulador activo:
   ```bash
   ./gradlew installDebug
   ```
   *Nota: Android Studio también permite ejecutar la app gráficamente usando el botón "Run" (Shift + F10).*

## 3. Arquitectura del proyecto

El proyecto está estructurado utilizando **Clean Architecture** (Arquitectura Limpia) combinada con el patrón de diseño **MVVM** (Model-View-ViewModel) en la capa de presentación. Esto garantiza un alto nivel de desacoplamiento y testabilidad.

El código se organiza por funcionalidades (*features*, ej. `auth`, `properties`, `users`), y dentro de cada funcionalidad se divide en las siguientes capas:

* **Presentation (Presentación):** Contiene la interfaz de usuario construida de forma declarativa con Jetpack Compose y los ViewModels que exponen el estado a las vistas.
* **Domain (Dominio):** El núcleo de la aplicación. Contiene las reglas de negocio (Casos de uso / Use cases) y los modelos de dominio (Entidades). No tiene dependencias de Android.
* **Data (Datos):** Implementa las interfaces de los repositorios definidas en el dominio. Aquí residen los Data Sources locales (bases de datos Room) y remotos (APIs a través de Retrofit).

Ejemplo de la estructura interna de una feature:
```text
features/properties/
├── data/
│   ├── datasources/
│   └── repositories/
├── domain/
│   ├── entities/
│   ├── repositories/
│   └── usecases/
└── presentation/
    ├── components/
    ├── screens/
    └── viewmodels/
```

## 4. Razones del uso de las librerías

Las herramientas seleccionadas son el estándar actual de la industria para el desarrollo en Android moderno:

* **Jetpack Compose:** Framework de UI declarativo. Permite construir interfaces dinámicas y reactivas con menos código en comparación con el sistema tradicional de XML.
* **Hilt (Dagger):** Maneja la inyección de dependencias. Facilita la provisión de clases (repositorios, ViewModels, etc.) en todo el ciclo de vida de la aplicación, reduciendo el código repetitivo o *boilerplate*.
* **Retrofit & Gson:** Utilizados para el consumo de APIs REST. Retrofit convierte las llamadas HTTP en interfaces de Kotlin, haciendo el código de red seguro en tiempo de compilación.
* **Room:** Una capa de abstracción sobre SQLite. Se usa para el almacenamiento local porque verifica las consultas SQL en tiempo de compilación y se integra perfectamente con flujos asíncronos (Coroutines/Flow).
* **WorkManager:** Esencial para tareas en segundo plano que deben ejecutarse de manera confiable, como el `ImageUploadWorker`. Garantiza que procesos críticos (ej. subir fotos de propiedades) se completen incluso si el usuario sale de la aplicación.
* **Coil:** Librería ligera y moderna para la carga de imágenes asíncronas, diseñada específicamente con soporte de primera clase para Compose.

## 5. Puntos destacados del proyecto

* **Soporte Offline y Caché Local:** Gracias al uso intensivo de Room (`PropertyDao`, `PropertyDraftDao`, etc.), la app puede mostrar información almacenada en caché y guardar borradores cuando no hay conexión a internet.
* **Integración Nativa de Hardware:** Implementación directa de la cámara del dispositivo (`CameraManager`) y servicios de seguridad biométrica (`BiometricService`) para una experiencia de usuario premium.
* **Sincronización Robusta:** Sistema de carga de archivos (imágenes) en segundo plano mediante WorkManager, lo que previene la pérdida de datos y mejora la percepción de rendimiento.
* **Notificaciones Push:** Integración nativa con Firebase Cloud Messaging (`FCMService`) para mantener a los usuarios actualizados en tiempo real sobre sus propiedades y mensajes.
* **Navegación Moderna:** Uso de Hilt Navigation Compose (`AppNavigator`, `FeatureNavGraph`) para gestionar los flujos de pantallas de manera declarativa y segura en cuanto al tipado.