package hr.fer.zemris.java.hw16.search.commands;

import hr.fer.zemris.java.hw16.search.environment.IEnvironment;

// TODO: Auto-generated Javadoc
/**
 * Razred koji implementira sučelje {@link ICommand}. Primjerci ovog razreda
 * pozivom metode {@link #execute(IEnvironment)} korisniku ispisuju pozdravnu
 * poruku te vraćaju status {@link CommandStatus#EXIT} čime bi se program trebao
 * ugasiti.
 * 
 * @see ICommand
 * @see CommandStatus#EXIT
 * 
 * @author Davor Češljaš
 */
public class ExitCommand implements ICommand {

	/**
	 * Konstanta koja predstavlja naziv ove naredbe, ujedino i ključna riječ
	 * koju korisnik upisuje kako bi pokrenuo ovu naredbu
	 */
	public static final String NAME = "exit";

	@Override
	public CommandStatus execute(IEnvironment environment) {
		environment.writeLine("Zatvaram program...");
		return CommandStatus.EXIT;
	}

}
