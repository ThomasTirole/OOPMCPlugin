# OOPMC - Plugin Minecraft en Java

OOPMC est un plugin pour serveur Minecraft **Paper**.
Il sert de support à l'Atelier JAVA (module 320) :
on y retrouve la programmation orientée objet
(héritage, interfaces, polymorphisme, encapsulation)
appliquée à un vrai jeu.

## Ce que fait le plugin

| Fonctionnalité | Type | Package |
| --- | --- | --- |
| Message + titre de bienvenue à la connexion | Événement | `welcome` |
| `/heal [joueur]` : vie et faim au maximum | Commande | `heal` |
| Coordonnées affichées à la mort | Événement | `deathcoords` |
| Coffre de mort façon Valheim | Événements | `deathchest` |
| `/testinv` : remplit l'inventaire pour tester | Commande | `testinv` |

## Versions utilisées

| Outil | Version | Où c'est défini |
| --- | --- | --- |
| Java (JDK) | 25 | `build.gradle.kts` (toolchain) |
| Minecraft / Paper | 26.3 | `build.gradle.kts` + `plugin.yml` |
| Gradle | 9.7.0 | `gradle/wrapper/gradle-wrapper.properties` |
| run-paper | 3.1.0 | `build.gradle.kts` |

## Démarrage rapide

Si tout est déjà installé (sinon, lisez le guide 1) :

1. IntelliJ : **File > New > Project from Version Control**,
   coller l'URL de ce repo.
2. Attendre la fin de la synchronisation Gradle.
3. Onglet **Gradle** (à droite) > `OOPMC` > `Tasks` >
   `run paper` > double-clic sur **runServer**.
4. Minecraft 26.3 > Multijoueur > Connexion directe > `localhost`.

## Le guide complet

Lisez-les dans l'ordre :

1. [Installer les outils](docs/01-installation.md)
2. [Ouvrir et configurer le projet dans IntelliJ](docs/02-intellij.md)
3. [Comprendre les fichiers Gradle](docs/03-gradle.md)
4. [Comprendre le plugin.yml](docs/04-plugin-yml.md)
5. [Lancer et tester le serveur](docs/05-lancer-tester.md)
6. [Comprendre le code](docs/06-code.md)
7. [À vous de jouer : défis](docs/07-defis.md)
8. [Dépannage](docs/08-depannage.md)

## Liens utiles

- Documentation Paper : <https://docs.papermc.io>
- Javadoc de l'API : dans IntelliJ, **Ctrl+Q** (macOS : F1)
  sur une classe ou une méthode.
