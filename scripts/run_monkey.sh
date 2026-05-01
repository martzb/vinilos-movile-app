#!/bin/bash
# run_monkey.sh
# Script para ejecutar pruebas de estrés (Monkey Testing) en la app Vinilos.
# Requisitos: Emulador encendido y aplicación instalada.

# Exportar ADB al PATH si no se encuentra (Solución automática para Mac/Linux)
if ! command -v adb &> /dev/null
then
    echo "adb no encontrado en el PATH. Intentando exportar ruta por defecto de Mac/Linux..."
    export PATH=$PATH:$HOME/Library/Android/sdk/platform-tools
fi

echo "Iniciando prueba de robustez (Monkey) con 2,000 eventos..."
adb shell monkey -p com.misw.vinilos --throttle 250 -v 2000 > monkey_report.txt
echo "Generando Logcat post-prueba..."
adb logcat -d > logcat_report.txt

echo "Prueba finalizada. Revise los reportes txt para confirmar ausencia de FATAL EXCEPTION."
