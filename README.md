# 🎵 Vinilos — Qué hace cada carpeta

**Backend API:** `https://backvynils-alternos-production.up.railway.app`

---

## Estructura general

```
app/src/main/java/com/misw/vinilos/
│
├── data/
│   ├── model/
│   ├── network/
│   ├── database/
│   │   └── dao/
│   └── repository/
│
├── ui/
│   ├── album/
│   ├── artist/
│   └── collector/
│
└── MainActivity.kt
```

---

## Qué va en cada carpeta

### 📁 data/model/
Aquí van las clases que representan los datos que llegan del API.
Cada clase tiene los mismos campos que devuelve el servidor en JSON.

**Ejemplo:** Album tiene id, name, cover, releaseDate, description, genre, recordLabel.

---

### 📁 data/network/
Aquí va todo lo relacionado con la conexión al servidor.

- **VinilosApiService** → Define qué endpoints se pueden llamar (GET /albums, GET /musicians, etc.)
- **ApiClient** → Configura Retrofit con la URL base del servidor

---

### 📁 data/database/
Aquí va la base de datos local del teléfono (Room/SQLite).
Sirve para guardar datos cuando no hay internet.

- **VinilosDatabase** → Es la base de datos, como una caja que guarda todo localmente

---

### 📁 data/database/dao/
Aquí van las interfaces que definen qué operaciones se pueden hacer sobre la base de datos local.

Cada DAO es responsable de una entidad: AlbumDao, ArtistDao, CollectorDao.
Las operaciones típicas son: guardar, obtener todos, obtener por ID.

---

### 📁 data/repository/
Aquí va la lógica que decide de dónde vienen los datos.

- Si hay internet → pide los datos al servidor
- Si no hay internet → usa los datos guardados localmente

Hay un Repository por cada tipo de dato: AlbumRepository, ArtistRepository, CollectorRepository.

---

### 📁 ui/album/
Todo lo relacionado con las pantallas de álbumes.

| Archivo | Qué hace |
|---------|----------|
| AlbumFragment | Pantalla que muestra la lista de álbumes — HU01 |
| AlbumDetailFragment | Pantalla que muestra el detalle de un álbum — HU02 |
| AlbumViewModel | Tiene la lógica y expone los datos como LiveData |
| AlbumAdapter | Le dice al RecyclerView cómo pintar cada álbum |

---

### 📁 ui/artist/
Todo lo relacionado con las pantallas de artistas.

| Archivo | Qué hace |
|---------|----------|
| ArtistFragment | Pantalla que muestra la lista de artistas — HU03 |
| ArtistDetailFragment | Pantalla que muestra el detalle de un artista — HU04 |
| ArtistViewModel | Tiene la lógica y expone los datos como LiveData |
| ArtistAdapter | Le dice al RecyclerView cómo pintar cada artista |

---

### 📁 ui/collector/
Todo lo relacionado con las pantallas de coleccionistas.

| Archivo | Qué hace |
|---------|----------|
| CollectorFragment | Pantalla que muestra la lista de coleccionistas — HU05 |
| CollectorDetailFragment | Pantalla que muestra el detalle de un coleccionista — HU06 |
| CollectorViewModel | Tiene la lógica y expone los datos como LiveData |
| CollectorAdapter | Le dice al RecyclerView cómo pintar cada coleccionista |

---

### 📁 res/layout/
Aquí van los archivos XML que definen cómo se ve cada pantalla visualmente.
Un archivo por Fragment y uno por item del RecyclerView.

### 📁 res/navigation/
Aquí va el archivo que define cómo se navega entre pantallas.
Es como un mapa de la app.

### 📁 res/menu/
Aquí va el menú de navegación inferior con los tabs de Álbumes, Artistas y Coleccionistas.

---

## Quién hace qué

| Responsable | Carpeta | Historias |
|-------------|---------|-----------|
| **Rubén** | `ui/album/` | HU01 — Catálogo álbumes, HU02 — Detalle álbum |
| **Diego** | `ui/artist/` | HU03 — Listado artistas, HU04 — Detalle artista |
| **David** | `ui/collector/` | HU05 — Listado coleccionistas, HU06 — Detalle coleccionista |
| **Brian** | `data/` completo | Base de datos, API, modelos, repositorios |

---

## Cómo fluyen los datos

```
Pantalla (Fragment)
    ↓ observa cambios
ViewModel
    ↓ pide datos
Repository
    ↓ con internet       ↓ sin internet
Servidor Railway      Base de datos local
```

El Fragment nunca habla directamente con el servidor.
Siempre pasa por el ViewModel y el Repository.

---

## Endpoints disponibles

| Pantalla | Endpoint |
|----------|----------|
| HU01 — Lista álbumes | GET /albums |
| HU02 — Detalle álbum | GET /albums/{id} |
| HU03 — Lista artistas | GET /musicians |
| HU04 — Detalle artista | GET /musicians/{id} |
| HU05 — Lista coleccionistas | GET /collectors |
| HU06 — Detalle coleccionista | GET /collectors/{id} |
| HU07 — Crear álbum | POST /albums |
| HU08 — Agregar track | POST /albums/{id}/tracks |