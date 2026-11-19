# 7. À vous de jouer : défis

Pour chaque défi :

- un **nouveau package** dans `ch.tirtho.oopmc` ;
- une classe par fichier ;
- le branchement dans `OOPMCPlugin.onEnable()` ;
- les commandes déclarées dans `plugin.yml`.

Avant de coder un défi de niveau 2 ou 3,
**dessinez vos classes sur papier** : qui écoute quoi,
qui stocke quoi, qui appelle qui.

---

## Niveau 1 : un événement, quelques lignes

- [ ] **Atterrissage en douceur** : pas de dégâts de chute
  quand on tombe sur de la laine.
  Pistes : `EntityDamageEvent`, `event.getCause() == DamageCause.FALL`,
  le bloc sous le joueur, `event.setCancelled(true)`.
- [ ] **Compteur de morts** : à chaque mort,
  afficher « Tu es mort X fois ».
  Piste : `player.getStatistic(Statistic.DEATHS)`.
- [ ] **Traînée de fleurs** : une fleur pousse là où le joueur
  marche, seulement sur l'herbe.
  Pistes : `PlayerMoveEvent` (attention, très fréquent !),
  `Material.GRASS_BLOCK`, `Material.POPPY`.

## Niveau 2 : événements + un peu d'état

- [ ] **Pomme dorée = super saut** : manger une pomme dorée
  donne un effet de saut pendant 10 secondes.
  Pistes : `PlayerItemConsumeEvent`, `PotionEffect`,
  `PotionEffectType.JUMP_BOOST`.
- [ ] **Bûcheron** : casser la base d'un tronc fait tomber
  tout le tronc au-dessus.
  Pistes : `BlockBreakEvent`, une boucle vers le haut
  avec `block.getRelative(BlockFace.UP)`, `Tag.LOGS`.
- [ ] **Quiz dans le chat** : `/quiz` pose une question,
  le premier qui répond juste gagne un diamant.
  Pistes : une classe `Question` (énoncé + réponse),
  `AsyncChatEvent`. Attention, cet événement est
  **asynchrone** : pour donner l'objet, repassez
  sur le thread principal avec le scheduler
  (`Bukkit.getScheduler().runTask(...)`).

## Niveau 3 : un vrai système

- [ ] **Système de homes**
    - `/sethome <nom> [description]`
    - `/home <nom>` : téléportation
    - `/homes` : liste
    - `/homes <nom>` : coordonnées + description
    - `/homes delete <nom>`

  Conception suggérée : une classe `Home` (nom, `Location`,
  description), une classe `HomeManager` qui garde une
  `Map<UUID, Map<String, Home>>`, des commandes qui reçoivent
  le manager **par le constructeur**.
  Bonus : sauvegarder les homes dans un fichier
  (`getConfig()` / `saveConfig()`).
- [ ] **Le sol est de la lave** : le bloc sous le joueur
  devient du magma, puis de la lave quelques secondes plus tard.
  Pistes : `BukkitRunnable`, `runTaskLater`,
  et une commande pour activer ou désactiver le mode.

## Défi conception : refactoriser le coffre de mort

`DeathChestListener` fait tout : écouter, poser le coffre,
le marquer, le vider, le détruire. Découpez-le :

- `DeathChestListener` : écoute seulement, et **délègue** ;
- `DeathChest` : un coffre de mort (ses blocs, son propriétaire),
  qui sait se remplir, se vider dans un inventaire et se détruire ;
- éventuellement `DeathChestPlacer` : trouve la place
  et pose le coffre.

Le comportement en jeu doit rester **exactement le même**.
C'est le meilleur exercice M320 de la liste.
