package utils.io;


import java.io.IOException;
import java.util.Scanner;

/**
 * Une classe utilitaire pour la saisie de chaînes ou d'entiers sur l'entrée
 * standard.
 */
public class Input {
	private Scanner scanner;
	/** Last Input instance that was created */
	private static Input lastInstance;

	public Input() {
	   this.scanner = new Scanner(System.in);
	   lastInstance = this;
	}

	/**
	 * Get the last input instance
	 * @return the last Input instance
	 */
	private static Input instance()
	{ return lastInstance; }
	
	/**
	 * Permet la saisie d'une chaîne sur l'entrée standard
	 * 
	 * @return la chaîne saisie
	 */
	public static String readString() {
		return new Input().localReadString();
	}
	/**
	 * Lit une chaîne sur l'entrée standard
	 * sans créer de nouvelle instance de Input (et donc de Scanner)
	 * 
	 * @return la chaine saisie
	 */
	public static String readStringNoNewInstance() {
		return Input.instance().localReadString();
	}
	private String localReadString() {
		return this.scanner.next();
	}

	
	/**
	 * Permet la saisie d'un entier sur l'entrée standard
	 * 
	 * @return l'entier saisi
	 */
	public static int readInt() throws java.io.IOException {
	   return new Input().localReadInt();
	}
	/**
	 * Lit un entier sur l'entrée standard
	 * sans créer de nouvelle instance de Input (et donc de Scanner)
	 * 
	 * @return l'entier saisi
	 */
	public static int readIntNoNewInstance() throws IOException {
		return Input.instance().localReadInt();
	}
	private int localReadInt() throws java.io.IOException {
		try {
			return this.scanner.nextInt();
		} catch (Exception e) {
		   e.printStackTrace();
			this.scanner.skip(".*");
			throw new java.io.IOException();
		}
	}	
	
	// pour le test
	public static void main(String[] args) {
		try {
			System.out.print(" chaine : ? ");
			String chaineLue = Input.readString();
			System.out.println("lue  => " + chaineLue);
			System.out.print(" int : ? ");
			int intLu = Input.readInt();
			System.out.println("lue  => " + intLu);
		} catch (java.io.IOException e) {
		}
	}
}