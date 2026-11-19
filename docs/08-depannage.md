# 8. Dépannage

| Symptôme | Cause probable | Solution |
| --- | --- | --- |
| `Unsupported class file major version` | Gradle n'utilise pas Java 25 | Settings > Build Tools > Gradle > **Gradle JVM = 25** |
| `No matching toolchains found for requested specification: {languageVersion=25}` | Aucun JDK 25 installé, ou Gradle ne le trouve pas | Installer un JDK 25 (guide 1) et le choisir comme Gradle JVM |
| Tout est en rouge dans l'éditeur, `Cannot resolve symbol JavaPlugin` | Gradle n'a pas fini ou a raté sa synchronisation | Onglet Gradle > bouton **Reload All Gradle Projects** (flèches circulaires) |
| Pas de tâche `runServer` dans l'onglet Gradle | Même cause | Recharger Gradle, puis regarder dans `Tasks > run paper` |
| Le serveur s'arrête tout de suite en parlant d'EULA | EULA pas acceptée | `run/eula.txt` : `eula=true`, puis relancer |
| Minecraft : « Version incompatible » | Client et serveur pas en même version | Installation 26.3 dans le launcher (guide 1) |
| Minecraft : « Connexion refusée » | Le serveur n'est pas encore prêt, ou il est arrêté | Attendre le **Done** dans la console |
| `oopmc` en rouge, ou absent dans `plugins` | Le plugin a planté au chargement | Remonter la console jusqu'à la première erreur |
| `NullPointerException` dans `onEnable` sur `getCommand(...)` | Commande pas déclarée dans `plugin.yml` | La déclarer (guide 4) |
| « You don't have permission » sur `/heal` | Vous n'êtes pas op | Dans la console : `op VotrePseudo` |
| L'événement ne se déclenche jamais | Listener pas enregistré, ou `@EventHandler` oublié | Vérifier `registerEvents(...)` dans `onEnable()` et l'annotation |
| La modif du code ne change rien en jeu | Serveur pas redémarré | `stop`, puis relancer `runServer` |
| Accents illisibles dans le chat | Fichiers pas en UTF-8 | Settings > Editor > File Encodings > UTF-8 |
| Le serveur rame ou plante pour manque de mémoire | Pas assez de RAM | Baisser `-Xmx2G` à `-Xmx1G` dans `build.gradle.kts` |

## Repartir de zéro

Si plus rien ne marche :

1. Arrêter le serveur.
2. Gradle > `Tasks > build > clean`.
3. Supprimer le dossier `run/` : vous perdez le monde de test,
   mais rien d'autre.
4. **File > Invalidate Caches > Invalidate and Restart**.
5. Relancer `runServer`.

## Toujours bloqué ?

Copiez la **première** erreur de la console, celle qui
contient `ch.tirtho.oopmc` ou `Caused by`, et montrez-la.
« Ça marche pas » sans le message d'erreur, on ne peut rien en faire.
