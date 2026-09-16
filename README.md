# Tower Defense

Jeu de stratégie de type **Tower Defense** développé en Java dans le cadre d’un projet universitaire de deuxième année de licence informatique.

Le joueur doit défendre son territoire contre des vagues successives de bloons. Pour cela, il peut acheter, placer et améliorer différentes tourelles disposant chacune de caractéristiques et de projectiles spécifiques.

![Aperçu du projet](./design/diagrams/UML.png)

## À propos du projet

L’objectif de ce projet était de concevoir une application complète en Java en mettant en pratique les principes de la programmation orientée objet.

Le jeu repose sur une architecture modulaire afin de faciliter l’ajout de nouveaux éléments, tels que des tourelles, des projectiles, des ennemis, des plateaux ou des actions.

Le projet comprend notamment :

- un moteur de jeu ;
- plusieurs interfaces d’affichage ;
- un système de vagues ;
- une boutique ;
- des tourelles améliorables ;
- plusieurs catégories de projectiles ;
- une génération automatique des chemins ;
- des tests unitaires ;
- une documentation technique ;
- des diagrammes UML.

## Fonctionnalités

### Plateau et chemins

- Génération automatique des routes ;
- prise en charge de plateaux avec un ou plusieurs chemins ;
- déplacement des bloons sur les routes ;
- calcul de la distance restante jusqu’à l’arrivée ;
- recherche des ennemis présents dans une zone ;
- placement contrôlé des tourelles sur le plateau.

### Ennemis

Le jeu possède plusieurs types de bloons :

- bloon rouge ;
- bloon bleu ;
- bloon vert ;
- bloon jaune.

Chaque type peut disposer de caractéristiques différentes, notamment en matière de vitesse et de résistance.

### Tourelles

Plusieurs tourelles sont disponibles :

- Dart Monkey ;
- Canon Monkey ;
- Freeze Monkey ;
- Junky Monkey ;
- Needle Monkey ;
- Slow Monkey ;
- Sniper Monkey.

Les tourelles peuvent :

- détecter les ennemis à portée ;
- sélectionner une cible ;
- tirer automatiquement ;
- être achetées et placées sur le plateau ;
- être améliorées ou rétrogradées ;
- être revendues à la boutique.

### Projectiles

Le projet contient différents types de projectiles :

- fléchette ;
- fléchette tranchante ;
- fléchette très tranchante ;
- bombe ;
- bombe améliorée ;
- aiguille ;
- projectile ralentissant ;
- projectile de glace.

Selon leur type, les projectiles peuvent :

- infliger des dégâts directs ;
- toucher plusieurs ennemis ;
- ralentir une cible ;
- immobiliser temporairement un ennemi ;
- appliquer des effets particuliers.

### Vagues

Le jeu comprend un système de vagues progressives.

Chaque vague peut faire varier :

- le nombre d’ennemis ;
- le type d’ennemis ;
- leur vitesse ;
- leur résistance ;
- la difficulté globale de la partie.

Une vague se termine lorsque tous les bloons ont été éliminés ou ont atteint la fin du parcours.

### Boutique et améliorations

La boutique permet au joueur de :

- consulter les tourelles disponibles ;
- acheter et placer une tourelle ;
- vendre une tourelle ;
- améliorer ses caractéristiques ;
- revenir à une amélioration précédente ;
- récupérer une partie de l’argent investi.

Les améliorations peuvent concerner les dégâts, la portée, la vitesse d’attaque ou les caractéristiques des projectiles.

### Interfaces

Le projet propose plusieurs modes d’affichage :

- affichage simple dans le terminal ;
- affichage coloré dans le terminal ;
- affichage stylisé ;
- interface graphique développée avec Swing ;
- affichage graphique utilisant des textures.

## Architecture

Le code source est organisé autour de plusieurs composants.

```text
src/
├── game/
│   ├── actions/       # Actions réalisables par le joueur
│   ├── boards/        # Gestion des plateaux
│   ├── cells/         # Cellules du plateau
│   ├── controller/    # Contrôleurs du jeu
│   ├── displays/      # Interfaces CLI et GUI
│   ├── objects/       # Bloons, tourelles et projectiles
│   ├── roads/         # Routes et déplacements
│   ├── shops/         # Boutique
│   ├── upgrade/       # Système d’amélioration
│   └── waves/         # Génération et gestion des vagues
└── utils/
    ├── config/        # Lecture de la configuration
    ├── datastructures/# Structures de données
    ├── io/            # Entrées et sorties
    ├── maths/         # Fonctions mathématiques
    └── plane2d/       # Coordonnées et directions
```

### Plateaux

La classe abstraite `Board` regroupe les comportements communs des plateaux.

Deux implémentations principales sont disponibles :

- `DefaultBoard`, qui représente un plateau avec une route principale ;
- `MultiPathBoard`, qui permet de gérer plusieurs chemins.

### Routes

Les routes sont représentées à l’aide d’une liste doublement chaînée.

Les principales classes concernées sont :

- `Road`, qui représente une route complète ;
- `RoadTile`, qui représente une étape de la route ;
- `DoublyLinkedList`, qui implémente la structure de données ;
- `Node`, qui représente un élément de cette structure.

Chaque étape conserve sa cellule, sa direction, sa distance jusqu’à l’arrivée ainsi que ses liens avec les étapes voisines.

### Génération des chemins

La génération des routes s’appuie notamment sur :

- `CoordsIterator` ;
- `RandomCoordsIterator` ;
- `StraightCoordsIterator` ;
- `Direction`.

Les routes peuvent être droites ou générées aléatoirement, tout en respectant les limites du plateau et en évitant les demi-tours invalides.

### Moteur de jeu

La classe `Game` centralise l’état général de la partie.

Les contrôleurs permettent de gérer différents modes de jeu :

- `ManualGameController` pour les actions manuelles ;
- `AutoGameController` pour les actions automatisées.

Le système d’actions prend notamment en charge :

- l’achat d’une tourelle ;
- la vente d’une tourelle ;
- l’amélioration ou la rétrogradation d’une tourelle ;
- le passage à la vague suivante ;
- l’affichage de l’état du jeu ;
- la sortie du jeu.

## Prérequis

Pour compiler et exécuter le projet, il est nécessaire de disposer de :

- Java ;
- un terminal ;
- `make` ;
- Git, uniquement pour cloner le dépôt.

Le projet contient également `junit-console.jar` pour l’exécution des tests unitaires.

## Installation

Clonez le dépôt :

```bash
git clone git@github.com:wahaab05/l2s4-projet-2026.git
```

Accédez au dossier :

```bash
cd l2s4-projet-2026
```

## Configuration

Les paramètres du jeu sont définis dans le fichier :

```text
config.properties
```

Ce fichier regroupe notamment :

- les caractéristiques des bloons ;
- les caractéristiques des tourelles ;
- les propriétés des projectiles ;
- les coûts des éléments ;
- les paramètres des améliorations ;
- la configuration des vagues.

Un fichier `.env` peut également être ajouté à la racine afin de définir un chemin Java personnalisé.

Exemple :

```env
JAVA_HOME=/usr/bin/
```

Si ce fichier n’est pas présent, les commandes Java disponibles par défaut sur la machine sont utilisées.

> Le fichier `.env` ne doit pas être publié s’il contient des informations personnelles ou sensibles.

## Compilation et exécution

Le projet utilise un `Makefile` pour simplifier les commandes.

### Nettoyer les fichiers générés

```bash
make clean
```

### Compiler le projet

```bash
make compile
```

### Lancer les tests

```bash
make test
```

### Générer la documentation JavaDoc

```bash
make docs
```

### Générer les fichiers exécutables

```bash
make livrables
```

### Exécuter le projet

```bash
make run
```

Certaines versions intermédiaires peuvent également être exécutées avec des commandes spécifiques définies dans le `Makefile`.

## Tests

Le projet contient des tests unitaires pour les principales fonctionnalités :

- création des plateaux ;
- génération des chemins ;
- gestion des cellules ;
- déplacement des bloons ;
- fonctionnement des tourelles ;
- comportement des projectiles ;
- gestion des vagues ;
- système d’amélioration ;
- boutique ;
- lecture de la configuration ;
- structures de données ;
- opérations mathématiques.

Les fichiers de test sont disponibles dans le dossier :

```text
tests/
```

Pour lancer les tests :

```bash
make test
```

## Diagrammes UML

Les diagrammes techniques sont disponibles dans les dossiers :

```text
design/diagrams/
uml/
```

### Diagramme de classes principal

![Diagramme de classes](./design/diagrams/class_diagram.svg)

### Architecture du jeu

![Architecture du jeu](./design/diagrams/UML.png)

### Classes utilitaires

![Classes utilitaires](./design/diagrams/UML-utils.png)

## Technologies utilisées

- **Java** pour le développement du jeu ;
- **Java Swing** pour l’interface graphique ;
- **JUnit** pour les tests unitaires ;
- **Make** pour automatiser la compilation et l’exécution ;
- **PlantUML** pour certains diagrammes ;
- **Git et GitHub** pour le versionnement et la publication du projet.

## Compétences mises en pratique

Ce projet m’a permis de renforcer mes compétences en :

- programmation orientée objet ;
- héritage, abstraction et polymorphisme ;
- conception d’une architecture modulaire ;
- manipulation de structures de données ;
- développement d’interfaces graphiques ;
- écriture de tests unitaires ;
- gestion de fichiers de configuration ;
- modélisation UML ;
- débogage et refactorisation ;
- utilisation de Git dans un projet collaboratif.

## Améliorations possibles

Plusieurs évolutions peuvent encore être envisagées :

- enrichir l’interface graphique ;
- ajouter de nouveaux ennemis ;
- ajouter de nouvelles tourelles ;
- proposer davantage de cartes ;
- améliorer l’équilibrage des vagues ;
- ajouter des effets visuels et sonores ;
- compléter la couverture des tests ;
- optimiser certains algorithmes ;
- améliorer la documentation du code.

## Contexte


L’objectif était de réaliser progressivement un jeu complet en suivant plusieurs livrables, tout en appliquant les bonnes pratiques de conception, de test et de collaboration.

