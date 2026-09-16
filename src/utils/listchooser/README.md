# Créer des menus pour choisir parmi des éléments

	Merci à Jean-Christophe Routier pour la base de code et les échanges.

On souhaite pouvoir facilement créer des menus qui permettent de choisir à partir d'une liste d'éléments. Typiquement, on imagine avoir un type énuméré : 

```java
public enum Elements {
		A,B,C,D;
}
```

et pouvoir afficher un menu à l'écran :

```
0 - none
1 - A
2 - B
3 - C
4 - D
Input your choice ?
```

ou bien n'importe quel diagramme d'héritage

```java
public abstract class Elements {
	
	public void doSomething() {
		System.out.println("Running method doSomething of " + this);
	}
}
```

et des classes filles A,B,C,D (ici seule A est montrée)

```java
public class A extends Elements {

	public String toString() {
		return "A";
	}
}
```


## Réalisation

Pour réaliser cela on va se doter :

- d'une interface présentant une méthode permettant de réaliser un choix parmi une liste d'éléments, cette interface est *générique* afin de permettre un choix de n'importe quel type, et surtout de bien **récupérer**, une fois l'élément choisi, **un objet du type**

```java
public interface ListChooser<T> {

	/**
	 * Allows one to choose an item from a list of items of type T. 
	 * 
	 * @param msg The asked question.
	 * @param list The list of items of type T from which one must choose one.
	 * @return The chosen item.
	 */
	public T choose(String msg, List<? extends T> list);
} 
```

- de deux implémentations de l'interface permettant de créer 
	- un choix aléatoire (`listchooser.RandomListChooser`) 
	- ou un choix interactif (`listchooser.InteractiveListChooser`)
- d'une classe utilitaire permettant de gérer les entrées sorties (`listchooser.util.Input`)

## Utilisation

L'avantage d'avoir une interface est bien sûr que nous pouvons écrire du code nécessitant de faire des choix sans décider d'emblée la façon dont la réponse sera fournie : de manière aléatoire ou de manière interactive.

Dans le cadre d'un jeu (c'est juste un exemple ...) on pourra repousser le choix de la stratégie de réponse jusqu'au programme principal.

On illustre ci-dessous l'utilisation.

### Premier exemple

On veut sélectionner un choix aléatoire parmi les éléments A,B,D.

```java
// on crée un listchooser aléatoire
RandomListChooser<Elements> rlc = new RandomListChooser<>();
// on crée la liste des éléments parmi lesquels choisir
List<Elements> l = new ArrayList<>();
l.add(new A());
l.add(new B());
l.add(new D());
// on invoque la méthode permettant de choisir
Elements chosenElement = rlc.choose("Which element do you choose ?", l);
// on affiche le choix
System.out.println("You've chosen : " + chosenElement);
```

Un exemple d'exécution est :

```
Which element do you want ?
[0 - none, 1 - A, 2 - B, 3 - D]
You've chosen : D
```

Attention le choix peut-être `null`. En effet dans les deux implémentations de l'interface `ListChooser` fournies, un choix par défaut est ajouté, correspondant à `0 - none`, qui fait que la méthode `choose` retourne `null`. Pour information, si jamais la liste des éléments fournie est vide, la méthode retourne également `null`. Vous pourrez à loirsir modifier ce comportement en changeant le code des classes fournies.



Une fois le choix réalisé, on peut utiliser l'instance retournée 

```java
// suite du code précédent
if (chosenElement != null) chosenElement.doSomething();
```

L'affichage complet de l'exemple est alors :

```
Which element do you want ?
[0 - none, 1 - A, 2 - B, 3 - D]
You've chosen : D
Running method doSomething of D
```

### Deuxième exemple

Dans cet exemple on illustre le fait qu'on n'a pas à choisir immédiatement le type de `ListChooser`. On souhaite exécuter le même choix que ci-dessus. Pour cela on va créer une méthode qui prend en paramètre l'instance de `ListChooser`.

```java
public void makeAChoice (ListChooser<Elements> lc) {
	List<Elements> l = new ArrayList<>();
	l.add(new A());
	l.add(new B());
	l.add(new D());
	Elements chosenElement = lc.choose("Which element do you choose ?", l);
	System.out.println("You've chosen : " + chosenElement);
	if (chosenElement != null) chosenElement.doSomething();
}
```

On pourra alors réaliser deux appels différents :

```java
makeAChoice(new RandomListChooser<Elements>);
```

Dont l'exécution donnera la même chose que ci-dessus.

```
Which element do you choose ?
[0 - none, 1 - A, 2 - B, 3 - D]
You've chosen : D
Running method doSomething of D
```

Ou bien

```java
makeAChoice(new InteractiveListChooser<Elements>);
```

Dont l'exécution donnera :

```
Which element do you choose ?
      0 - none
      1 - A
      2 - B
      3 - D
            choice ?
```

Et une fois que l'utilisateur aura saisi A :           

```
You've chosen : A
Running method doSomething of A
```

## Conclusion

Vous disposez maintenant d'un outil permettant de réaliser des choix. Une illustration très basique de comment intégrer cela dans un jeu est donné dans le package `game` de l'archive, où deux joueurs s'affrontent sur un round de Pierre, Feuille, Ciseaux. Les deux joueurs réalisent tous les deux par défaut un choix aléatoire. Si on passe `i` en argument sur la ligne de commande, alors ils réalisent tous les deux un choix en mode interactif.


## Tester les entrées sorties

C'est l'occasion aussi de voir comment on peut tester les entrées sorties. Vous disposez donc également dans `test/listchooser/util/InputTest` d'un exemple de test des méthodes `readString` et `readInt` de la classe `Input`. Afin de pouvoir réaliser ce type de tests, il faut rediriger les flux des entrées et sorties standard. Un peu comme vous le faites avec les redirections des commandes UNIX. Vous aurez l'occasion de comprendre le fonctionnement de cela lors du cours de PDS au S5.




🐠


