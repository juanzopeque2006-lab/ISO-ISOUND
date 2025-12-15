# ISoundMusic

Proyecto Java (Maven) mínimo.

## Requisitos
- JDK 17 o superior instalado y en `PATH`.
- Maven 3.8+ (si no usa Maven Wrapper).

## Construir y probar
```bash
mvn -v
mvn clean test
mvn package
```

## Ejecutar (GUI)
Tras `mvn package`, se genera `target/isoundmusic-0.1.0-SNAPSHOT.jar`.

```bash
java -jar target/isoundmusic-0.1.0-SNAPSHOT.jar
```

Al iniciar:
- En consola verás: `Hola ISoundMusic!`.
- Se abrirá una ventana `ISoundMusic` con un mensaje de bienvenida.
