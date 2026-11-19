# 2. Ouvrir et configurer le projet dans IntelliJ

## Récupérer le projet

**Option A, avec Git (recommandé) :**

1. Écran d'accueil d'IntelliJ > **Clone Repository**
   (ou **File > New > Project from Version Control**).
2. URL : celle de ce repo GitHub.
3. Choisissez un dossier, puis **Clone**.

**Option B, sans Git :** sur GitHub,
**Code > Download ZIP**, dézippez,
puis **File > Open** et choisissez le dossier
qui contient `build.gradle.kts`.

**Pourquoi ouvrir ce dossier-là :** IntelliJ y trouve
`build.gradle.kts` et `settings.gradle.kts`.
Il comprend alors que c'est un projet Gradle et
l'importe tout seul.

## La première synchronisation Gradle

À l'ouverture, IntelliJ affiche une barre de progression
en bas : **Gradle sync**. La première fois, ça peut prendre
plusieurs minutes, parce qu'il télécharge :

1. **Gradle 9.7.0** lui-même (indiqué dans
   `gradle/wrapper/gradle-wrapper.properties`) ;
2. **l'API Paper 26.3** (les classes `Player`, `Block`,
   `Listener`...) ;
3. **le plugin run-paper**, qui sait télécharger
   et lancer un serveur.

Tout est mis en cache : les fois suivantes, c'est rapide.

Si IntelliJ demande s'il faut faire confiance au projet
(**Trust Project**), répondez oui.

## Les 3 réglages à vérifier

Ce sont ceux qui causent 90 % des problèmes.

### 1. Le SDK du projet

**File > Project Structure > Project**

- **SDK** : 25
- **Language level** : 25

**Pourquoi :** c'est le JDK qu'IntelliJ utilise
pour l'autocomplétion et pour souligner les erreurs.

### 2. Le JDK de Gradle

**File > Settings > Build, Execution, Deployment >
Build Tools > Gradle**

- **Gradle JVM** : 25
- **Build and run using** : Gradle
- **Distribution** : Wrapper

**Pourquoi :** Gradle tourne avec son propre Java,
séparé du SDK du point 1. C'est lui qui compile
réellement le plugin. S'il pointe vers un vieux JDK,
le build échoue avec des erreurs peu claires,
même si l'éditeur n'affiche rien en rouge.

### 3. L'encodage

**File > Settings > Editor > File Encodings** : **UTF-8** partout.

**Pourquoi :** nos messages contiennent des accents
(« Vous avez été soigné ! »). En mauvais encodage,
ils s'affichent mal dans le jeu.

## Où sont les choses

```
OOPMC/
├── build.gradle.kts         <- comment compiler (guide 3)
├── settings.gradle.kts      <- nom du projet
├── gradle.properties        <- version, group, options Gradle
├── gradle/wrapper/          <- quelle version de Gradle
└── src/main/
    ├── java/ch/tirtho/oopmc/
    │   ├── OOPMCPlugin.java  <- classe principale
    │   ├── welcome/          <- une fonctionnalité = un package
    │   ├── heal/
    │   ├── deathcoords/
    │   ├── deathchest/
    │   └── testinv/
    └── resources/
        └── plugin.yml       <- carte d'identité du plugin (guide 4)
```

Le dossier `run/` apparaît au premier lancement du serveur.
Il contient le serveur de test. Il est ignoré par Git
(`.gitignore`), parce que c'est du contenu généré, lourd
et propre à chaque machine.

## Note sur le Gradle Wrapper

Le repo contient `gradle/wrapper/gradle-wrapper.properties`,
mais pas les scripts `gradlew` / `gradlew.bat`.
Ça ne gêne pas IntelliJ : il lit le fichier `.properties`
et télécharge la bonne version de Gradle.
En revanche, on ne peut pas taper `./gradlew` dans un terminal.
Passez par l'onglet **Gradle** d'IntelliJ.

---

Suite : [3. Comprendre les fichiers Gradle](03-gradle.md)
