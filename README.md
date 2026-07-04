# DesvarApp

DesvarApp es una suite de escritorio portable fuera de línea que proporciona una interfaz gráfica (GUI) ligera para operaciones
criptográficas y manipulación de datos cotidianas, evitando el uso de comandos complejos o herramientas web que comprometan la privacidad.

Desarrollada con **Arquitectura Hexagonal** en el núcleo y el patrón **MVVM** en la capa de presentación con **JavaFX 21** y **Java 21**.

---

## Características Actuales (v1.0.0)

* **Gestión GPG / OpenPGP:** Generación local de pares de llaves (RSA), cifrado/descifrado simétrico y asimétrico compatible con GnuPG, y
  llavero local indexado en **SQLite**.
* **Utilidades de Datos:** Codificación y decodificación Base64 en tiempo real con bindings reactivos.
* **Seguridad en UX:** Tablas de solo lectura con celdas copiables para mitigar modificaciones accidentales de llaves o fingerprints.

---

## Tecnologías Principales

* **Core & Crypto:** Java 21, BouncyCastle (`bcprov`, `bcpg`, `bcpkix`).
* **UI & UX:** OpenJFX 21, ControlsFX (Tema oscuro optimizado).
* **Persistencia:** SQLite JDBC.

---

## Instalación y Compilación Local

### Uso Portable (Usuario)

1. Descarga el `.zip` desde **[Releases](https://github.com/berracode/desbare/releases)**.
2. Descomprime y ejecuta `DesvarApp.exe`. No requiere instalación ni variables de entorno.

### Construcción desde Código (Desarrollador)

Requiere JDK 21 y Maven.

```bash
git clone [https://github.com/berracode/desbare.git](https://github.com/berracode/desbare.git)
cd desbare
mvn clean package -DskipTests
```

El ejecutable portable se genera en: target/dist/DesvarApp/.

---

## Roadmap

- [ ] **Mapeador Cron:** Validador e intérprete visual de expresiones cron.
- [ ] **Format / Minify:** Formateador local de JSON/XML con resaltado de sintaxis.
- [ ] **Checksum Tool:** Calculador de hashes locales (MD5, SHA-256, SHA-512) para archivos y texto.
- [ ] **Mock Data:** Generador rápido de payloads estructurados de prueba.

---

## Contribuciones

Las contribuciones son bienvenidas para expandir la suite de herramientas. Para colaborar:

1. Haz un Fork del repositorio.
2. Crea una rama para tu característica (`git checkout -b feature/nueva-utilidad`).
3. Asegúrate de mantener el desacoplamiento de la Arquitectura Hexagonal (el dominio no debe conocer a JavaFX ni a BouncyCastle).
4. Abre un Pull Request detallando los cambios.

---

## Licencia

Este proyecto está bajo la **Licencia MIT**. Puedes revisar el archivo `LICENSE` en la raíz del repositorio para más detalles.