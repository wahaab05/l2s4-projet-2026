# Mise à jour de la classe Input
Update de la classe Input permettant de gérer plusieurs inputs à la suite. Plusieurs choix peuvent donc être définis d'un seul coup. (Entrer des coordonnées x et y par exemple)
## Nouvelles méthodes
- `readStringNoNewInstance()`
	Lit la "suite" de l'entrée standard simulée
- `readIntNoNewInstance()`
	Lit comme nombre entier la "suite" de l'entrée standard simulée
	
## Example
```java
import utils.io.Input;

// simule une entrée utilisateur
String input = "0\n5\n"; // les chiffres 0 et 5 séparés par des retours à la ligne
InputStream in = new ByteArrayInputStream(input.getBytes());
System.setIn(in);

// lecture de l'entrée simulée
int x = Input.readInt();
// int y = Input.readInt();
// /!\ lève NoSuchElementException, car une nouvelle instance de Scanner a été créée et l'entrée simulée a été "perdue" dans la première instance. Il n'y a donc plus rien à lire sur l'entrée standard.
int y = Input.readIntNoNewInstance(); // fonctionne et renvoie bien 5

// ici, x=0 et y=5
```

## Usage
Le premier appel doit **nécessairement** se faire via les méthodes d'origine (`readString()` ou `readInt()`) pour s'assurer qu'**un Scanner sera initialisé**.  
Tous les appels suivants nécessitant de lire depuis la même entrée simulée se font via `readStringNoNewInstance()` ou `readIntNoNewInstance()`.  

## Note
Ces méthodes ont été testées avec une entrée simulée et avec une entrée clavier classique et ces deux usages ont le comportement attendu.
