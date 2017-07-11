package hr.fer.zemris.java.hw16.search.commands;

import hr.fer.zemris.java.hw16.search.environment.IEnvironment;

/**
 * Razred koji implementira sučelje {@link ICommand}. Primjerci ovog razreda
 * pozivom metode {@link #execute(IEnvironment)} korisniku ispisuju rezultate
 * zadnjeg pretraživanja (zadnjeg poziva naredbe {@link QueryCommand#NAME}).
 * 
 * @see ICommand
 * @see IEnvironment
 * 
 * @author Davor Češljaš
 */
public class ResultsCommand implements ICommand {

	/**
	 * Konstanta koja predstavlja naziv ove naredbe, ujedino i ključna riječ
	 * koju korisnik upisuje kako bi pokrenuo ovu naredbu.
	 */
	public static final String NAME = "results";

	@Override
	public CommandStatus execute(IEnvironment environment) {
		environment.printResults();
		return CommandStatus.CONTINUE;
	}

}
