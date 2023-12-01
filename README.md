# PastillApp💊📱
PastillApp es una aplicación Android desarrollada en Kotlin que utiliza la arquitectura MVVM (Model-View-ViewModel) para facilitar la gestión y seguimiento de la medicación para usuarios con tratamientos médicos continuos. La aplicación proporciona una interfaz intuitiva que permite a los usuarios establecer recordatorios de toma de medicamentos, llevar un registro diario de su estado de salud y gestionar su información de manera eficiente.

## Tecnologías Utilizadas
- Kotlin
- Firebase Authentication
- Android SDK

## Estructura del Proyecto
El proyecto PastillApp sigue una arquitectura MVVM junto a una organización modular para una gestión eficiente del código y una separación clara de responsabilidades. A continuación, se describen las carpetas principales:

- **Activities:** Contiene las actividades principales de la aplicación, cada una dedicada a una funcionalidad específica.
- **Adapters:** Almacena los adaptadores utilizados para la presentación de datos en listas y vistas.
- **Fragments:** Contiene los fragmentos, componentes modulares de interfaz de usuario, utilizados para construir las vistas de la aplicación.
- **Helpers:** Incluye clases de apoyo que proporcionan funciones auxiliares y utilidades en todo el proyecto.
- **Listeners:** Almacena implementaciones de listeners utilizados para gestionar eventos y acciones del usuario.
- **Models:** Contiene las clases de modelo que representan la estructura de datos utilizada en la aplicación.
- **Services:** Incluye clases que encapsulan la lógica de servicios o procesos en segundo plano utilizados por la aplicación.
- **Viewmodels:** Contiene las clases ViewModel, responsables de gestionar y preparar los datos para su presentación en la interfaz de usuario.
- **res:** Directorio estándar que contiene recursos como archivos de diseño, imágenes y valores.
## Uso
`Registro y Configuración Inicial:`
- Abra la aplicación después de la instalación.
- Complete el proceso de registro proporcionando una dirección de correo electrónico válida.
- Verifique su cuenta a través del enlace enviado por correo electrónico.
  
`Establecer Recordatorios:`
- Vaya a la sección "Registrar" desde la barra de navegación inferior.
- Configure los recordatorios para cada medicamento, especificando la frecuencia y horarios deseados.
- Guarde la configuración.
  
`Seguimiento Diario:`
- Acceda a la sección "Síntomas" para registrar su estado de salud diario.
- Seleccione los síntomas experimentados y guarde la información.
  
`Visualización de Datos:`
- Explore la sección "Calendario" para acceder a sus recordatorios y registro de salud diario.
- Revise el historial de recordatorios en la sección "Ver Recordatorios".

## Equipo
Estudiantes del Instituto ORT Argentina, en la carrera de Analistas en Sistemas: Agustina Boto, Joaquin Herreros Schoklender, Tomas Guerchicoff Adamo, Camila Szesko, Camila Ingberg, Francisco Veron, Paola Quiñonez, Federico Marty, Patricia Berkovics, Pia Potasznik, Jose Lara
