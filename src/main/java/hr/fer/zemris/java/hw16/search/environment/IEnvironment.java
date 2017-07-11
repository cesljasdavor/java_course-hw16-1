package hr.fer.zemris.java.hw16.search.environment;

import java.util.List;

/**
 * Sučelje koje predstavlja okolinu izvođenja programa. Implementator sučelja
 * mora implementirati sljedeće metode:
 * <ul>
 * <li>{@link #readLine()}</li>
 * <li>{@link #write(String)}</li>
 * <li>{@link #writeLine(String)}</li>
 * <li>{@link #getResults()}</li>
 * <li>{@link #setResults(List)}</li>
 * <li>{@link #getArguments()}</li>
 * <li>{@link #setArguments(List)}</li>
 * <li>{@link #printResults()}</li>
 * </ul>
 * 
 * @author Davor Češljaš
 */
public interface IEnvironment {

	/**
	 * Metoda koja se koristi za čitanje jednog redka korisnikova unosa iz ulaza
	 * ovog okruženja.
	 *
	 * @return redak korisnika unosa
	 */
	public String readLine();

	/**
	 * Metoda se koristi za pisanje jednog primjerka razreda {@link String} na
	 * izlaz ovog okruženja. Ispis pri tome neće prijeći u novi redak
	 *
	 * @param output
	 *            primjerak razreda {@link String} koji se ispisuje na izlaz
	 *            ovog okruženja
	 */
	public void write(String output);

	/**
	 * Metoda se koristi za pisanje jednog primjerka razreda {@link String} na
	 * izlaz ovog okruženja. Ispis pri tome prelazi u novi redak.
	 *
	 * @param output
	 *            primjerak razreda {@link String} koji se ispisuje na izlaz
	 *            ovog okruženja
	 */
	public void writeLine(String outputLine);

	/**
	 * Metoda koja dohvaća zadnje rezultate pretraživanja modelirane s
	 * {@link List} primjeraka razreda {@link Result}, a koji su spremljeni
	 * unutar ovog okruženja.
	 *
	 * @return {@link List} primjeraka razreda {@link Result} koji predstavljaju
	 *         zadnje spremljene rezultate pretraživanja
	 */
	public List<Result> getResults();

	/**
	 * Metoda koja postavlja nove zadnje rezulate pretraživanja koji se predaju
	 * kao {@link List} primjeraka razreda {@link Result} <b>results</b>.
	 *
	 * @param results
	 *            novi zadnji rezulatati pretraživanja modelirani sa
	 *            {@link List}om primjeraka razreda {@link Result}.
	 */
	public void setResults(List<Result> results);

	/**
	 * Metoda koja dohvaća argumente koji su postavljeni ovom okruženju prilikom
	 * zadnjeg poziva neke naredbe.
	 *
	 * @return {@link List} primjeraka razreda {@link String} koji predstavljaju
	 *         argumente poziva neke naredbe
	 */
	public List<String> getArguments();

	/**
	 * Metoda koja postavlja argumente koji su poslani prilikom izvođenja neke
	 * naredbe.
	 *
	 * @param arguments
	 *            argumenti naredbe modelirani sa {@link List}om primjeraka
	 *            razreda {@link String}
	 */
	public void setArguments(List<String> arguments);

	/**
	 * Metoda koja koristeći metode {@link #getResults()} i
	 * {@link #writeLine(String)} ispisuje rezultate zadnjeg pretraživanja na
	 * izlaz ovog okruženja
	 */
	public void printResults();
}
