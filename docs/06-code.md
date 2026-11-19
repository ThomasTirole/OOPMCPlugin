# 6. Comprendre le code

## L'idée générale

Un plugin n'a **pas de `main`**. C'est le serveur qui :

1. lit `plugin.yml` ;
2. crée un objet de la classe `main` (`OOPMCPlugin`) ;
3. appelle `onEnable()` au démarrage, puis `onDisable()` à l'arrêt.

Ensuite, notre code ne s'exécute que **quand quelque chose
se passe** : un joueur tape une commande, se connecte, meurt...
C'est de la **programmation événementielle**.

## La classe principale : `OOPMCPlugin`

```java
public final class OOPMCPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new WelcomeListener(), this);
        getCommand("heal").setExecutor(new HealCommand());
        getServer().getPluginManager().registerEvents(new DeathCoordinatesListener(), this);
        getServer().getPluginManager().registerEvents(new DeathChestListener(this), this);
        getCommand("testinv").setExecutor(new TestInventoryCommand());
    }
}
```

- `extends JavaPlugin` : **héritage**. On récupère gratuitement
  `getServer()`, `getCommand()`, `getLogger()`...
- `@Override onEnable()` : **redéfinition**. Le serveur appelle
  la méthode, c'est notre version qui s'exécute.
- La classe ne contient **aucune logique de jeu**.
  Elle **branche** les autres classes, c'est tout.

## Organisation : une fonctionnalité = un package

```
ch.tirtho.oopmc
├── OOPMCPlugin              branche tout
├── welcome/WelcomeListener
├── heal/HealCommand
├── deathcoords/DeathCoordinatesListener
├── deathchest/DeathChestListener
└── testinv/TestInventoryCommand
```

**Pourquoi :** pour retrouver vite le code d'une fonctionnalité,
et pouvoir en supprimer une sans casser les autres.

---

## Les deux briques de base

### Un événement = une classe qui `implements Listener`

```java
public class WelcomeListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(Component.text("Bienvenue " + player.getName() + " !", NamedTextColor.GOLD));
    }
}
```

- `Listener` est une **interface vide**. Elle sert d'étiquette :
  « cette classe écoute des événements ».
- `@EventHandler` marque la méthode à appeler.
- **Le type du paramètre** (`PlayerJoinEvent`) dit quel événement
  on écoute. Le nom de la méthode est libre.
- Sans `registerEvents(...)` dans `onEnable()`, rien ne se passe.

### Une commande = une classe qui `implements CommandExecutor`

Extrait de `HealCommand` :

```java
@Override
public boolean onCommand(CommandSender sender, Command command,
                         String label, String[] args) {
    if (args.length == 1) {
        Player target = Bukkit.getPlayer(args[0]);
        // ...
    }
    if (!(sender instanceof Player player)) {
        sender.sendMessage("Vous devez spécifier un joueur ! /heal <joueur>");
        return true;
    }
    healPlayer(player);
    return true;
}
```

- `CommandSender` est un type **général** : un joueur **ou**
  la console. C'est du **polymorphisme**. On vérifie avec
  `instanceof` avant de le traiter comme un `Player`.
- `args` contient les mots tapés après la commande :
  `/heal Steve` donne `args[0] == "Steve"`.
- `return true` : commande traitée.
  `return false` : le serveur affiche le `usage` du `plugin.yml`.
- `healPlayer()` est `private` : un détail interne,
  **encapsulé**.

---

## Les messages : `Component`

```java
player.sendMessage(Component.text("Bonjour", NamedTextColor.GREEN));
```

Paper utilise la bibliothèque **Adventure** pour le texte.
Un `Component` est un **objet** texte avec couleur, style,
survol, clic... C'est plus propre que les vieux codes
couleur `§a`.

Titre plein écran (dans `WelcomeListener`) :

```java
player.showTitle(Title.title(
        Component.text("OOPMC", NamedTextColor.AQUA),
        Component.text("Bon jeu !")
));
```

---

## Le coffre de mort, en détail

Fichier : `deathchest/DeathChestListener.java`

### Le comportement

1. À la mort, un coffre (double si la place le permet) apparaît
   avec tout l'inventaire. S'il est impossible de le poser
   (vide, limite de hauteur...), les objets tombent normalement.
2. Clic droit du **propriétaire** : tout passe d'un coup
   dans son inventaire, et le coffre disparaît.
3. Inventaire plein : un message indique ce qui reste,
   puis le coffre s'ouvre normalement.
4. Dès que le coffre est vide (à la fermeture), il disparaît
   sans tomber en item.

### Les 4 événements écoutés

| Méthode | Événement | Rôle |
| --- | --- | --- |
| `onPlayerDeath` | `PlayerDeathEvent` | Pose le coffre et y range les objets |
| `onChestClick` | `PlayerInteractEvent` | Récupération en un clic |
| `onChestClose` | `InventoryCloseEvent` | Supprime le coffre s'il est vide |
| `onChestBreak` | `BlockBreakEvent` | Empêche de dupliquer les objets en cassant le coffre |

Une seule classe peut écouter plusieurs événements :
il suffit de plusieurs méthodes `@EventHandler`.

### Comment reconnaître un coffre de mort ?

Un coffre de mort ressemble à n'importe quel coffre.
Pour le reconnaître, on lui colle une **étiquette invisible**
grâce au `PersistentDataContainer` (PDC) :

```java
chest.getPersistentDataContainer().set(ownerKey,
        PersistentDataType.STRING, owner.getUniqueId().toString());
chest.update();
```

- Le PDC est un petit stockage clé / valeur attaché à un bloc,
  un objet ou une entité. Il est **sauvegardé avec le monde** :
  le coffre reste reconnu après un redémarrage.
- La clé est un `NamespacedKey` : `oopmc:death_chest_owner`.
  Le préfixe `oopmc:` évite les collisions avec d'autres plugins.
- On stocke l'**UUID** du joueur, pas son pseudo :
  un joueur peut changer de pseudo, jamais d'UUID.
- `chest.update()` est **obligatoire** : `getState()` donne
  une **copie** du bloc, et `update()` réécrit cette copie
  dans le monde.

### Pourquoi le constructeur reçoit le plugin

```java
public DeathChestListener(JavaPlugin plugin) {
    this.ownerKey = new NamespacedKey(plugin, "death_chest_owner");
    this.lootedKey = new NamespacedKey(plugin, "death_chest_looted");
}
```

Un `NamespacedKey` a besoin du plugin pour son préfixe.
Au lieu d'aller le chercher dans une variable `static` globale,
on le **passe en paramètre** au constructeur.
C'est de l'**injection de dépendance**, et ça respecte
la règle du M320 : pas de `static` sauf `main`.
Les clés sont `private final` : créées une fois, jamais modifiées.

### L'astuce de `addItem()`

```java
Map<Integer, ItemStack> leftovers = player.getInventory().addItem(contents);
```

`addItem()` ajoute ce qu'il peut, et **renvoie ce qui n'a pas
rentré**. Map vide = tout est passé. Sinon, on remet les restes
dans le coffre. Lisez toujours la Javadoc d'une méthode
(Ctrl+Q) : le type de retour cache souvent ce genre de cadeau.

### Ne pas dupliquer les objets

```java
event.getDrops().clear();
```

Sans cette ligne, les objets seraient **à la fois** dans le coffre
**et** au sol. On la place **après** avoir rempli le coffre :
si quelque chose échoue avant, les objets tombent quand même
et le joueur ne perd rien.

De même, pour supprimer le coffre :
`block.setType(Material.AIR)`. Un `breakNaturally()`
ferait tomber le coffre en item.

---

## Ce que vous reconnaissez du M320

| Concept M320 | Où dans OOPMC |
| --- | --- |
| Héritage | `OOPMCPlugin extends JavaPlugin` |
| Redéfinition | `@Override onEnable()`, `onCommand()` |
| Interface | `implements Listener`, `implements CommandExecutor` |
| Polymorphisme | `CommandSender` (joueur ou console), `block.getState() instanceof Chest` |
| Encapsulation | Méthodes `private` (`healPlayer`, `markAsDeathChest`...), attributs `private final` |
| Une classe par fichier | Chaque listener et chaque commande dans son fichier |
| Pas de `static` | Le plugin passé au constructeur de `DeathChestListener` |
| Collections | `List<Block>`, `Map<Integer, ItemStack>` |

---

Suite : [7. À vous de jouer](07-defis.md)
