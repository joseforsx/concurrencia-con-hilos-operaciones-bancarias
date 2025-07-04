# concurrencia-con-hilos-operaciones-bancarias

## 📄 Descripción

Este proyecto es un prototipo de software educativo, desarrollado en Java. El enfoque principal es el uso de **hilos (Threads)** para simular operaciones concurrentes como:  - Registro de empleados - Depósito de efectivo - Retiro de efectivo

- Registro de empleados
- Depósito de efectivo
- Retiro de efectivo
- Guarda los registros de empleados en un archivo de texto `.txt` para almacenamiento persistente

El objetivo principal es demostrar el manejo de la concurrencia en Java y cómo múltiples hilos pueden interactuar para realizar operaciones simultáneas en un entorno bancario.

Desarrollado en la Universidad Abierta y a Distancia de México (UnADM), durante el semestre 2022-2

## 🛠️ Tecnologías y conceptos utilizados

- Lenguaje: Java
- Programación concurrente con:  
  - **Clase `Thread`**  
  - **Interfaz `Runnable`**
- Interfaz gráfica de usuario (GUI), Uso de Java Swing
- Manejo de archivos de texto para guardar registros (`empleados.txt`)
- Uso de sincronización para evitar condiciones de carrera (si aplicaste sincronización)

## 📂 Estructura del proyecto

concurrencia_hilos/

├── Source Packages/

│ ├── Interfaces/

│ │ ├── AltaRegistro.java # Clase para registrar empleados

│ │ ├── Depositar.java # Clase para realizar depósitos

│ │ ├── Principal.java # Clase principal con interfaz general

│ │ └── Retirar.java # Clase para realizar retiros

│ └── Registros/

│ └── empleados.txt # Archivo de texto con datos de empleados y saldos

├── README.md

└── .gitignore

## ⚙️ Requisitos

- Java JDK 8.2 o superior.
- IDE para desarrollo Java con soporte Swing.
- Sistema operativo Windows / Linux / macOS.

## 📝 Notas

- El archivo `empleados.txt` debe ubicarse en la carpeta `src/Registros/` para que la aplicación lo encuentre.
- El proyecto utiliza múltiples hilos para operaciones de lectura y escritura en archivos para mejorar la respuesta.

## 👨‍💻  Autor

José Ángel Trevilla León

## 📄 Licencia

Este proyecto es libre para uso personal y educativo.


