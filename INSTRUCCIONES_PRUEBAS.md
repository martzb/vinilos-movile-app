# Guía de Ejecución de Pruebas (Secuencia TNT)

Este documento detalla el paso a paso para que los tutores o revisores puedan reproducir localmente la estrategia de pruebas implementada en la aplicación Vinilos, siguiendo el enfoque secuencial (TNT).

## Requisitos Previos
1. **Android Studio** instalado y configurado.
2. **Node.js y npm** instalados en la máquina (requerido para Kraken).
3. **Emulador Android (AVD)** encendido y ejecutándose en el puerto por defecto (ej. `emulator-5554`).
4. **App instalada:** Ejecute la aplicación en el emulador al menos una vez desde Android Studio antes de iniciar las pruebas de sistema.

---

## Paso 1: Pruebas E2E (Espresso)
Validación programática de la UI directamente sobre el ciclo de vida de Android.

1. Abra la terminal en la raíz del proyecto.
2. Ejecute el comando de pruebas instrumentadas de Gradle:
   ```bash
   ./gradlew connectedAndroidTest
   ```
   > **Nota de compatibilidad (Error 26):** Si presenta el error `FAILURE: Build failed with an exception. What went wrong: 26`, significa que su terminal está utilizando una versión de Java demasiado reciente (ej. Java 26) que no es compatible con el compilador. Para solucionarlo, ejecute el comando asignando el JDK integrado de Android Studio:
   > 
   > **Para usuarios Mac / Linux:**
   > ```bash
   > export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" && ./gradlew connectedAndroidTest
   > ```
   > 
   > **Para usuarios Windows (PowerShell):**
   > ```powershell
   > $env:JAVA_HOME="C:\Program Files\Android\Android Studio\jbr" ; .\gradlew connectedAndroidTest
   > ```
3. **Resultado Esperado:** Verificará el flujo del catálogo, el scroll y la extracción de datos de las tarjetas directamente en el dispositivo.

---

## Paso 2: Pruebas E2E BDD (Kraken Node)
Validación orientada a comportamiento (BDD) utilizando escenarios Gherkin.

1. Navegue a la carpeta de pruebas de Kraken:
   ```bash
   cd kraken-tests
   ```
2. Instale las dependencias del proyecto:
   ```bash
   npm install
   ```
3. Ejecute la batería de escenarios:
   ```bash
   npm test
   ```
   *(Este comando es un alias de `kraken-node run` configurado en el `package.json`)*
4. **Resultado Esperado:** Kraken se conectará a Appium/UiAutomator y ejecutará visualmente los 5 escenarios `.feature` enfocados en las Historias de Usuario HU01 y HU02.

---

## Paso 3: Pruebas de Robustez (Monkey Testing)
Pruebas de estrés enviando pseudo-eventos aleatorios para detectar fugas de memoria o excepciones fatales.

1. Asegúrese de que el emulador sigue activo.
2. Vuelva a la raíz del proyecto y ejecute el script automatizado:
   ```bash
   bash scripts/run_monkey.sh
   ```
3. **Resultado Esperado:** El dispositivo recibirá 2,000 eventos rápidos de forma ininterrumpida. Al finalizar, el script volcará los resultados en dos archivos generados automáticamente (`monkey_report.txt` y `logcat_report.txt`).

---

## 📂 Reportes y Evidencias Entregadas

Si desea inspeccionar los resultados pre-calculados de nuestra ejecución de la Granja Virtual, diríjase a:

* **Reportes Monkey:** `reports/monkey/` (Contiene los logcats limpios de Crashes para las arquitecturas Old Gen, Standard y High End).
* **Líneas Base (VRT):** Carpetas `vrt-baseline-standard`, `vrt-baseline-old-gen`, y `vrt-baseline-high-end` en la raíz del repositorio, las cuales contienen las evidencias de que la UI es responsiva a la fragmentación de pantallas.
* **Demo:** `reports/demo.mp4` (Grabación en video de la navegación sin errores).
