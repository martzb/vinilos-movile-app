#!/bin/bash
# Script para capturar evidencia de memoria y screenshot para un perfil específico.

if [ -z "$1" ]; then
    echo "Error: Debes proporcionar el perfil (ej. standard, old-gen, high-end)"
    echo "Uso: bash scripts/capture_memory_profile.sh <perfil>"
    exit 1
fi

# Exportar ADB al PATH si no se encuentra (Solución automática para Mac/Linux)
if ! command -v adb &> /dev/null
then
    export PATH=$PATH:$HOME/Library/Android/sdk/platform-tools
fi

PROFILE=$1
PACKAGE="com.misw.vinilos"

echo "=== Perfilando dispositivo: $PROFILE ==="

# 1. Tomar Screenshot
echo "Tomando screenshot de la pantalla actual"
adb shell screencap -p /sdcard/memory_$PROFILE.png
adb pull /sdcard/memory_$PROFILE.png reports/monkey/memory_$PROFILE.png

# 2. Tomar volcado de memoria (dumpsys)
echo "Capturando consumo de memoria (meminfo)"
mkdir -p reports/monkey
adb shell dumpsys meminfo $PACKAGE > reports/monkey/meminfo_$PROFILE.txt

echo "Evidencias guardadas en:"
echo " - reports/monkey/memory_$PROFILE.png"
echo " - reports/monkey/meminfo_$PROFILE.txt"
