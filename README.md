# 🏋️‍♂️ FitnessCenter - Sistema de Gestión

Este proyecto es una aplicación de escritorio desarrollada en **JavaFX** para la gestión integral de un centro deportivo. Permite administrar socios, clases de entrenamiento y sus respectivas reservas, garantizando la integridad de los datos mediante validaciones y persistencia local.

## 🚀 Funcionalidades Principales

### 1. Gestión de Socios (Partners)
* **Alta y Modificación**: Registro de nuevos socios y actualización de datos existentes.
* **Validaciones**:
    * Campos obligatorios: Nombre de usuario y Email.
    * Formato de Email: Debe contener `@` y `.`.
    * Formato de Teléfono: Restricción estricta a exactamente 9 dígitos numéricos.

### 2. Gestión de Entrenamientos (Trainings)
* **Control de Clases**: Gestión de nombres de clases, entrenadores, duración y precios.
* **Validaciones**:
    * Campos obligatorios: Nombre de la clase y nombre del entrenador.
    * Lógica de Negocio: El precio y la duración no pueden ser valores negativos.
    * Control de Errores: Validación de tipos numéricos mediante bloques `try-catch`.

### 3. Persistencia de Datos
* La aplicación utiliza **Serialización de Objetos** para guardar la información en un archivo local llamado `fitness_data.dat`. Los datos se cargan automáticamente al iniciar y se guardan tras cada operación de escritura.

---

## 🛠️ Tecnologías y Herramientas
* **Lenguaje:** Java 17/25.
* **Framework UI:** JavaFX 17.0.10.
* **Arquitectura:** Modelo-Vista-Controlador (MVC) y patrón Repository.
* **Entorno:** Desarrollado utilizando IntelliJ IDEA.
* **Gestor de dependencias:** Maven.

---

## 💻 Instrucciones de Instalación

1. **Clonar el repositorio:**
   ```bash
   git clone [https://github.com/a28709-collab/FitnessCenter.git]