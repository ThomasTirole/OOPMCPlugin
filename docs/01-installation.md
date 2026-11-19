# 1. Installer les outils

Avant d'ouvrir le projet, il faut 4 choses.
Cochez au fur et à mesure.

- [ ] IntelliJ IDEA
- [ ] Un JDK 25
- [ ] Le plugin IntelliJ « Minecraft Development »
- [ ] Minecraft Java Edition, version 26.3

---

## IntelliJ IDEA

**Quoi :** l'éditeur (IDE) dans lequel on code.

**Comment :** <https://www.jetbrains.com/idea/download/>.
La version Community suffit.
Avec votre adresse d'école, vous pouvez demander
une licence étudiante gratuite pour la version Ultimate.

**Pourquoi IntelliJ :** il comprend Gradle tout seul.
Il télécharge les dépendances, propose l'autocomplétion
sur l'API Paper et lance le serveur en un clic.

---

## JDK 25

**Quoi :** le Java Development Kit, c'est-à-dire
le compilateur (`javac`) et la machine virtuelle (`java`).

**Pourquoi 25 et pas un autre :** le serveur Paper 26.3
exige Java 25. Le projet le dit dans `build.gradle.kts` :

```kotlin
java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}
```

Avec un JDK plus ancien, la compilation échoue.

**Comment, le plus simple (depuis IntelliJ) :**

1. Ouvrez n'importe quel projet, ou l'écran d'accueil.
2. **File > Project Structure** (Ctrl+Alt+Shift+S).
3. **SDK > Add SDK > Download JDK...**
4. Version : **25**. Vendor : **Eclipse Temurin**.
5. **Download**.

IntelliJ l'installe dans votre dossier utilisateur (`~/.jdks`).
Pas besoin de toucher aux variables d'environnement.

---

## Plugin « Minecraft Development »

**Quoi :** une extension IntelliJ spécialisée pour
les plugins Minecraft.

**Pourquoi :** pour ouvrir OOPMC, ce n'est pas obligatoire.
Mais ce plugin vous aide beaucoup :

- il reconnaît les classes `Listener` et les `@EventHandler` ;
- il autocomplète les noms d'événements ;
- il vérifie le `plugin.yml` ;
- il sait créer un nouveau projet de plugin,
  si vous voulez repartir de zéro plus tard.

**Comment :**

1. **File > Settings** (macOS : **IntelliJ IDEA > Settings**).
2. **Plugins > Marketplace**.
3. Cherchez **Minecraft Development**, puis **Install**.
4. Redémarrez IntelliJ.

---

## Minecraft Java Edition 26.3

**Quoi :** le jeu, pour vous connecter à votre serveur
de test et voir le plugin en action.

**Pourquoi la même version :** un client Minecraft ne peut
se connecter qu'à un serveur de **la même version**.
Notre serveur de test est en 26.3 (`minecraftVersion("26.3")`
dans `build.gradle.kts`).

**Comment :**

1. Ouvrez le launcher Minecraft.
2. Onglet **Installations > Nouvelle installation**.
3. Version : **release 26.3**.
4. Lancez cette installation.

Sans le jeu, vous pouvez quand même lancer le serveur
et voir ses messages dans la console d'IntelliJ.

---

Suite : [2. Ouvrir et configurer le projet dans IntelliJ](02-intellij.md)
