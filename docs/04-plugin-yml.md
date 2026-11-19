# 4. Comprendre le `plugin.yml`

Fichier : `src/main/resources/plugin.yml`

C'est la **carte d'identité** du plugin. Au démarrage,
le serveur ouvre chaque `.jar` du dossier `plugins/`,
lit ce fichier et sait alors :

- quelle classe instancier ;
- quelles commandes existent ;
- quelles permissions existent.

Sans `plugin.yml`, ou avec une erreur dedans,
le plugin ne se charge pas du tout.

```yaml
name: oopmc
version: '${version}'

main: ch.tirtho.oopmc.OOPMCPlugin
api-version: '26.3'
load: POSTWORLD

commands:
  heal:
    description: Soigne le joueur
    usage: /heal
    permission: oopmc.heal

  testinv:
    description: Remplit l'inventaire pour les tests
    usage: /testinv

permissions:
  oopmc.heal:
    description: Autorise /heal
    default: op
```

## Ligne par ligne

| Clé | Rôle |
| --- | --- |
| `name` | Nom du plugin, affiché par la commande `/plugins` |
| `version` | Remplacé au build par la version de `gradle.properties` (voir guide 3) |
| `main` | Nom **complet** (package + classe) de la classe qui hérite de `JavaPlugin` |
| `api-version` | Version de l'API visée. Le serveur refuse un plugin fait pour une API plus récente que lui |
| `load` | `POSTWORLD` : charger le plugin **après** les mondes, pour que les blocs et les joueurs existent déjà |

## Les commandes

Chaque commande tapée en jeu (`/heal`) doit être **déclarée ici**.
Ensuite, dans `onEnable()`, on lui associe l'objet qui la traite :

```java
getCommand("heal").setExecutor(new HealCommand());
```

Si vous oubliez la déclaration, `getCommand("heal")` renvoie
`null`, et le plugin plante au démarrage
(`NullPointerException`).

- `description` : texte affiché dans `/help`.
- `usage` : affiché si `onCommand()` retourne `false`.
- `permission` : seuls les joueurs qui ont cette permission
  peuvent utiliser la commande.

## Les permissions

```yaml
permissions:
  oopmc.heal:
    default: op
```

`default: op` : seuls les opérateurs (admins) ont la permission.
Autres valeurs possibles :

- `true` : tout le monde ;
- `false` : personne par défaut ;
- `not op` : tout le monde sauf les admins.

**Pourquoi une permission sur `/heal` :** sinon n'importe quel
joueur pourrait se soigner à l'infini en plein combat.

Pour devenir op sur votre serveur de test, voir le guide 5.

## Ajouter une nouvelle commande : la checklist

1. La déclarer dans `plugin.yml` (`commands:`).
2. Créer une classe qui `implements CommandExecutor`,
   dans son propre package.
3. La brancher dans `OOPMCPlugin.onEnable()`
   avec `getCommand("...").setExecutor(...)`.
4. Si besoin, déclarer une permission (`permissions:`).

---

Suite : [5. Lancer et tester le serveur](05-lancer-tester.md)
