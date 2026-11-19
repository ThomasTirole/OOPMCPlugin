# 3. Comprendre les fichiers Gradle

## C'est quoi, Gradle ?

Un **outil de build**. On lui décrit le projet dans un fichier,
et il s'occupe de :

- télécharger les bibliothèques (ici, l'API Paper) ;
- compiler les `.java` en `.class` ;
- emballer le tout dans un `.jar`, le plugin final ;
- lancer des tâches utiles, comme démarrer un serveur de test.

Sans Gradle, il faudrait télécharger l'API à la main,
la mettre dans le classpath et compiler en ligne de commande.

Nos fichiers sont en `.kts` (Kotlin Script).
La syntaxe ressemble à du Java, pas besoin de connaître Kotlin.

---

## `build.gradle.kts`, bloc par bloc

### Les plugins Gradle

```kotlin
plugins {
    id("java-library")
    id("xyz.jpenilla.run-paper") version "3.1.0"
}
```

- `java-library` : apprend à Gradle à compiler du Java
  et à produire un `.jar`.
- `run-paper` : ajoute la tâche `runServer`. Elle télécharge
  un vrai serveur Paper, y copie notre `.jar` et le démarre.

Attention : ce sont des plugins **pour Gradle**,
pas des plugins Minecraft.

### Où chercher les bibliothèques

```kotlin
repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}
```

Un **repository**, c'est un serveur qui héberge des bibliothèques.

- `mavenCentral()` : le dépôt public principal de Java.
- `repo.papermc.io` : le dépôt de l'équipe Paper.
  L'API Paper n'est pas sur Maven Central, donc il faut
  le donner explicitement.

### Les dépendances

```kotlin
dependencies {
    compileOnly("io.papermc.paper:paper-api:26.3.build.+")
}
```

On dépend de l'**API Paper 26.3**. C'est elle qui contient
`JavaPlugin`, `Player`, `Listener`, `ItemStack`...

- `26.3.build.+` : « la dernière build disponible de la 26.3 ».
  Paper publie des correctifs régulièrement, le `+` prend
  le plus récent.
- `compileOnly` : on en a besoin **pour compiler**, mais on ne
  l'embarque **pas** dans notre `.jar`.
  **Pourquoi :** le serveur Paper contient déjà ces classes
  quand il tourne. Les copier dans notre plugin serait inutile
  (le `.jar` passerait de quelques Ko à des dizaines de Mo)
  et pourrait créer des conflits de versions.

### La version de Java

```kotlin
java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}
```

On force Gradle à compiler avec Java 25.
**Pourquoi :** Paper 26.3 tourne sur Java 25.
Un plugin compilé pour une autre version pourrait
ne pas se charger. Le *toolchain* garantit que tout le monde
compile pareil, quel que soit le Java installé par défaut.

### Le serveur de test

```kotlin
tasks {
    runServer {
        minecraftVersion("26.3")
        jvmArgs("-Xms2G", "-Xmx2G")
    }
```

- `minecraftVersion("26.3")` : la version du serveur à
  télécharger. Elle doit correspondre à l'API (`paper-api:26.3`)
  et à votre client Minecraft.
- `jvmArgs("-Xms2G", "-Xmx2G")` : donne 2 Go de RAM au serveur,
  dès le départ (`Xms`) et au maximum (`Xmx`).
  Sur un PC avec peu de mémoire, vous pouvez descendre à `1G`.

### Injecter la version dans `plugin.yml`

```kotlin
    processResources {
        val props = mapOf("version" to version)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}
```

Pendant le build, Gradle copie `src/main/resources` dans le `.jar`.
Ce bloc remplace au passage `${version}` dans `plugin.yml`
par la vraie version du projet (`1.0-SNAPSHOT`).

**Pourquoi :** la version n'est écrite qu'à un seul endroit
(`gradle.properties`). On évite d'oublier de la changer
dans deux fichiers.

---

## `settings.gradle.kts`

```kotlin
rootProject.name = "OOPMC"
```

Le nom du projet. Il donne aussi son nom au `.jar` :
`OOPMC-1.0-SNAPSHOT.jar`.

---

## `gradle.properties`

```properties
group=ch.tirtho
version=1.0-SNAPSHOT
org.gradle.configuration-cache=true
org.gradle.parallel=true
org.gradle.caching=true
```

- `group` : l'identifiant de l'auteur, comme un nom de domaine
  à l'envers. Pour un projet à vous : `ch.divtec.<prenom>`.
- `version` : la version du plugin. `SNAPSHOT` = version
  en développement, pas encore publiée.
- Les 3 lignes `org.gradle...` servent à accélérer les builds :
  cache de configuration, tâches en parallèle,
  réutilisation des résultats déjà calculés.

---

## `gradle/wrapper/gradle-wrapper.properties`

```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-9.7.0-bin.zip
```

Fixe la version de **Gradle** (9.7.0).
**Pourquoi :** tout le monde, élèves et enseignant, utilise
exactement le même Gradle. Pas de « chez moi ça marche ».

---

## Les tâches Gradle utiles

Onglet **Gradle** à droite d'IntelliJ > `OOPMC` > `Tasks` :

| Tâche | Dossier | Ce qu'elle fait |
| --- | --- | --- |
| `runServer` | run paper | Compile, puis lance un serveur avec le plugin |
| `build` | build | Compile et produit le `.jar` |
| `jar` | build | Produit seulement le `.jar` |
| `clean` | build | Supprime `build/`, pour repartir propre |

Le `.jar` produit se trouve dans `build/libs/`.
C'est ce fichier qu'on mettrait dans le dossier `plugins/`
d'un vrai serveur.

Après un changement dans un fichier `.kts`, IntelliJ affiche
une petite icône d'éléphant **Load Gradle Changes**.
Cliquez dessus pour que la modification soit prise en compte.

---

Suite : [4. Comprendre le plugin.yml](04-plugin-yml.md)
