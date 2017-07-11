package hr.fer.zemris.java.hw16.search.commands;

/**
 * Enumeracija koja sadrži samo dvije vrijednosti:
 * <ol>
 * <li>{@link #CONTINUE}</li>
 * <li>{@link #EXIT}</li>
 * </ol>
 * 
 * Obe vrijednosti koriste se kako bi pozivatelju metode
 * {@link ICommand#execute(hr.fer.zemris.java.hw16.search.environment.IEnvironment)}
 * dale informaciju o izvođenju naredbe
 * 
 * @see ICommand
 * 
 * @author Davor Češljaš
 */
public enum CommandStatus {

	/**
	 * Vrijednost koja predstavlja uspješno izvođenje naredbe te da se savjetuje
	 * nastavak rada programa
	 */
	CONTINUE,

	/**
	 * Vrijednost koja predstavlja uspješno izvođenje naredbe te da se savjetuje
	 * prekid rada programa
	 */
	EXIT
}
