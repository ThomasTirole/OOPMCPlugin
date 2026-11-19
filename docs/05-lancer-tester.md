# 5. Lancer et tester le serveur

## Lancer le serveur de test

1. Onglet **Gradle** (à droite) > `OOPMC` > `Tasks` > `run paper`.
2. Double-clic sur **runServer**.

Ce qui se passe :

1. Gradle compile votre code et produit le `.jar`.
2. run-paper télécharge le serveur Paper 26.3
   dans le dossier `run/`, seulement la première fois.
3. Le `.jar` est placé dans les plugins du serveur.
4. Le serveur démarre. La console s'affiche en bas d'IntelliJ.

Le serveur est prêt quand la console affiche une ligne avec
**Done** (par exemple `Done (12.345s)! For help, type "help"`).

**Premier lancement :** la génération du monde prend du temps,
c'est normal. Si le serveur s'arrête en parlant de l'**EULA**
(le contrat d'utilisation de Minecraft) : ouvrez
`run/eula.txt`, remplacez `eula=false` par `eula=true`
et relancez.

Pour **vérifier que le plugin est chargé**, tapez `plugins`
dans la console : `oopmc` doit apparaître en vert.

## Se connecter

1. Lancez Minecraft **26.3** (voir guide 1).
2. **Multijoueur > Connexion directe**.
3. Adresse : `localhost`.

`localhost` veut dire « cet ordinateur » : le serveur
tourne sur votre propre machine.

## Devenir admin (op)

Certaines commandes, comme `/heal`, sont réservées aux opérateurs.
Dans la **console d'IntelliJ**, pas dans le jeu, tapez :

```
op VotrePseudo
```

C'est à faire une seule fois : c'est sauvegardé dans `run/`.

## La boucle de travail

1. Modifier le code.
2. Arrêter le serveur : tapez `stop` dans la console,
   ou cliquez sur le carré rouge.
3. Relancer **runServer**. Gradle recompile tout seul.
4. Se reconnecter et tester.

Préférez `stop` au carré rouge : `stop` sauvegarde
proprement le monde.

N'utilisez pas `/reload` : cette commande recharge
les plugins à chaud et provoque souvent des bugs bizarres.
Un redémarrage complet est plus sûr.

## Tester chaque fonctionnalité

| Fonctionnalité | Comment la tester |
| --- | --- |
| Bienvenue | Se connecter : message doré + titre « OOPMC » |
| `/heal` | Se faire mal (chute), puis `/heal`. Sur un autre joueur : `/heal Pseudo` |
| Coordonnées de mort | `/kill` : les coordonnées s'affichent en rouge |
| Coffre de mort | `/testinv` pour remplir l'inventaire, puis `/kill`, puis clic droit sur le coffre |
| `/testinv` | Inventaire rempli + armure en diamant |

**Astuce :** passez en créatif (`/gamemode creative`) pour vous
déplacer vite, puis en survie (`/gamemode survival`) pour tester
les dégâts et la mort.

## Déboguer

- `System.out.println(...)` ou `getLogger().info(...)` :
  le texte s'affiche dans la console d'IntelliJ.
  C'est ce que fait `DeathCoordinatesListener`.
- En cas d'erreur, le serveur affiche une **stack trace**.
  Cherchez la première ligne qui contient `ch.tirtho.oopmc` :
  c'est la ligne de **votre** code qui a planté.

---

Suite : [6. Comprendre le code](06-code.md)
