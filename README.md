# Vinilos — App Android

App móvil Android para explorar álbumes, artistas y coleccionistas de música.

**Backend API:** `https://backvynils-alternos-production.up.railway.app`  
**Stack:** Kotlin · Retrofit · Room · MVVM · Coroutines

---

## Estructura del proyecto

```
app/src/main/java/com/misw/vinilos/
│
├── data/
│   ├── model/              ← Clases de datos (Album, Artist, Collector)
│   ├── network/            ← Retrofit: ApiClient + VinilosApiService
│   ├── database/           ← Room: VinilosDatabase
│   │   └── dao/            ← Interfaces de acceso a SQLite local
│   └── repository/         ← Decide entre API o caché local
│
├── ui/
│   ├── album/              ← HU01, HU02
│   ├── artist/             ← HU03, HU04
│   └── collector/          ← HU05, HU06
│
└── MainActivity.kt
```

---

## Qué va en cada carpeta

### `data/model/`
Clases de datos que reflejan el JSON del servidor.

| Clase | Campos principales |
|-------|--------------------|
| `Album` | id, name, cover, releaseDate, description, genre, recordLabel, tracks, performers, comments |
| `Artist` | id, name, image, description, birthDate |
| `Collector` | id, name, telephone, email |

---

### `data/network/`
Todo lo relacionado con la conexión HTTP al servidor.

| Archivo | Responsabilidad |
|---------|----------------|
| `VinilosApiService` | Define los endpoints (`@GET`, `@POST`) con Retrofit |
| `ApiClient` | Singleton que construye y expone el servicio Retrofit |

---

### `data/database/`
Base de datos local Room/SQLite para soporte offline.

| Archivo | Responsabilidad |
|---------|----------------|
| `VinilosDatabase` | Define la base de datos y sus entidades |
| `dao/AlbumDao` | Operaciones sobre álbumes en SQLite |
| `dao/ArtistDao` | Operaciones sobre artistas en SQLite |
| `dao/CollectorDao` | Operaciones sobre coleccionistas en SQLite |

---

### `data/repository/`
Capa que decide de dónde vienen los datos.

```
¿Hay internet?
    SÍ  →  llama al servidor  →  guarda en caché local
    NO  →  lee de SQLite local
```

| Archivo | Cubre |
|---------|-------|
| `AlbumRepository` | Albums |
| `ArtistRepository` | Musicians / Bands |
| `CollectorRepository` | Collectors |

---

### `ui/album/` · `ui/artist/` · `ui/collector/`
Cada módulo de UI sigue la misma estructura:

| Archivo | Rol |
|---------|-----|
| `*Fragment` | Pantalla de lista — observa LiveData del ViewModel |
| `*DetailFragment` | Pantalla de detalle |
| `*ViewModel` | Lógica de presentación — expone `LiveData` |
| `*Adapter` | Pinta cada ítem en el `RecyclerView` |

---

## Responsabilidades del equipo

| Integrante | Módulo | Historias |
|------------|--------|-----------|
| **Rubén** | `ui/album/` | HU01 — Catálogo de álbumes · HU02 — Detalle de álbum |
| **Diego** | `ui/artist/` | HU03 — Listado de artistas · HU04 — Detalle de artista |
| **David** | `ui/collector/` | HU05 — Listado de coleccionistas · HU06 — Detalle de coleccionista |
| **Brian** | `data/` completo | Modelos · API · Room · Repositorios |

---

## Arquitectura MVVM

```
┌──────────────────────────────────────────────────────────────┐
│                         VISTA (UI)                           │
│                                                              │
│   AlbumFragment      ArtistFragment      CollectorFragment   │
│   AlbumDetail        ArtistDetail        CollectorDetail     │
│   AlbumAdapter       ArtistAdapter       CollectorAdapter    │
│                                                              │
│   · Muestra datos en pantalla                                │
│   · Observa LiveData del ViewModel                           │
│   · Nunca llama al servidor directamente                     │
└─────────────────────────┬────────────────────────────────────┘
                          │ observa LiveData
                          ▼
┌──────────────────────────────────────────────────────────────┐
│                       VIEWMODEL                              │
│                                                              │
│      AlbumViewModel   ArtistViewModel   CollectorViewModel   │
│                                                              │
│   · Recibe eventos de la Vista (click, scroll)               │
│   · Pide datos al Repository                                 │
│   · Expone resultados como LiveData                          │
│   · No conoce la Vista ni el servidor                        │
└─────────────────────────┬────────────────────────────────────┘
                          │ llama métodos
                          ▼
┌──────────────────────────────────────────────────────────────┐
│                      REPOSITORY                              │
│                                                              │
│    AlbumRepository   ArtistRepository   CollectorRepository  │
│                                                              │
│   · Decide de dónde vienen los datos                         │
│   · Con internet → Service Adapter (Retrofit)                │
│   · Sin internet → DAO local (Room)                          │
└───────────────┬──────────────────────────┬───────────────────┘
                │ con internet             │ sin internet
                ▼                          ▼
┌───────────────────────┐    ┌─────────────────────────────┐
│    SERVICE ADAPTER    │    │         ROOM DAO             │
│                       │    │                              │
│  VinilosApiService    │    │  AlbumDao                   │
│  ApiClient (Retrofit) │    │  ArtistDao                  │
│                       │    │  CollectorDao                │
│  Llama al servidor    │    │  Lee / escribe en SQLite     │
│  vía HTTP             │    │  local del teléfono          │
└──────────┬────────────┘    └─────────────────────────────┘
           │
           ▼
┌──────────────────────────────────────────────────────────────┐
│                     SERVIDOR RAILWAY                         │
│                                                              │
│      https://backvynils-alternos-production.up.railway.app   │
│                                                              │
│                   NestJS + PostgreSQL                        │
└──────────────────────────────────────────────────────────────┘
```

---

## Flujo de datos paso a paso

```
1. Usuario abre la pantalla de álbumes
         ↓
2. AlbumFragment notifica al AlbumViewModel
         ↓
3. AlbumViewModel consulta al AlbumRepository
         ↓
         ├── SÍ hay internet ──→ GET /albums (Retrofit)
         │                              ↓
         │                       guarda en Room
         │
         └── NO hay internet ──→ AlbumDao.getAll() (SQLite)
                                        ↓
4. Datos llegan al ViewModel como LiveData
         ↓
5. AlbumFragment detecta el cambio y pinta la lista
```

> El Fragment **nunca** habla directamente con el servidor.  
> Siempre pasa por ViewModel → Repository.

---

## Endpoints disponibles

| Historia | Método | Endpoint |
|----------|--------|----------|
| HU01 — Lista de álbumes | `GET` | `/albums` |
| HU02 — Detalle de álbum | `GET` | `/albums/{id}` |
| HU03 — Lista de artistas | `GET` | `/musicians` |
| HU04 — Detalle de artista | `GET` | `/musicians/{id}` |
| HU05 — Lista de coleccionistas | `GET` | `/collectors` |
| HU06 — Detalle de coleccionista | `GET` | `/collectors/{id}` |
| HU07 — Crear álbum | `POST` | `/albums` |
| HU08 — Agregar track | `POST` | `/albums/{id}/tracks` |
| HU09 — Comentar álbum | `POST` | `/albums/{id}/comments` |
| HU11 — Álbumes de coleccionista | `GET` | `/collectors/{id}/albums` |
| HU13 — Lista de premios | `GET` | `/prizes` |
