package hr.fer.zemris.java.hw16.search.commands;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import hr.fer.zemris.java.hw16.search.environment.IEnvironment;
import hr.fer.zemris.java.hw16.search.environment.Result;

/**
 * Razred koji implementira sučelje {@link ICommand}. Primjerci ovog razreda
 * pozivom metode {@link #execute(IEnvironment)}, a ovisno od argumenta koji se
 * može naći unutar primjerka razreda koji implementira sučelje
 * {@link IEnvironment}, ispisuju taj redni broj rezultat zadnjeg poziva naredbe
 * {@link QueryCommand#NAME}.
 * 
 * @see ICommand
 * @see IEnvironment
 * 
 * @author Davor Češljaš.
 */
public class TypeCommand implements ICommand {

	/**
	 * Konstanta koja predstavlja naziv ove naredbe, ujedino i ključna riječ
	 * koju korisnik upisuje kako bi pokrenuo ovu naredbu
	 */
	public static final String NAME = "type";

	@Override
	public CommandStatus execute(IEnvironment environment) {
		List<String> args = environment.getArguments();

		if (args.size() != 1) {
			environment.writeLine("Potreban broj argumenata je 1, Vi ste predali " + args.size());
			return CommandStatus.CONTINUE;
		}

		int index;
		try {
			index = Integer.parseInt(args.get(0));
		} catch (NumberFormatException e) {
			environment.writeLine("Unos '" + args.get(0) + "' ne mogu protumačiti kao indeks rezultata.");
			return CommandStatus.CONTINUE;
		}

		Result result = null;
		try {
			result = environment.getResults().get(index);
		} catch (IndexOutOfBoundsException | NullPointerException e) {
			environment.writeLine("Ne postoji rezultat na poziciji  " + index);
			return CommandStatus.CONTINUE;
		}

		return outputResult(environment, result);
	}

	/**
	 * Pomoćna metoda koja se koristi za čitanje datoteke koja se može dohvatiti
	 * pozivom metode {@link Result#getFilePath()} parametra <b>result</b>.
	 * Nakon što je sadržaj datoteke pročitan, koristeći primjerak razreda koji
	 * implementira sučelje {@link IEnvironment}, metoda ispisuje sadržaj
	 * datoteke, uz nekolicinu popratnih poruka.
	 *
	 * @param environment
	 *            primjerak razreda koji implementira sučelje
	 *            {@link IEnvironment}, a koji predstavlja okruženje, koje se
	 *            koristi za ispis datoteke i popratnih poruka.
	 * @param result
	 *            primjerak razreda {@link Result} iz kojeg se dohvaća putanja
	 *            do datoteke, a čiji se sadržaj ispisuje
	 * @return {@link CommandStatus#EXIT} ukoliko nije moguće pročitati datoteku
	 *         (dakle je datoteka obrisana) ili {@link CommandStatus#CONTINUE}
	 *         ukoliko se metoda normalno izvede
	 */
	private CommandStatus outputResult(IEnvironment environment, Result result) {
		String filePath = result.getFilePath();

		String fileContent = null;
		try {
			fileContent = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
		} catch (IOException e) {
			environment.writeLine("Datoteka sa putanjom '" + filePath + "' je obrisana.");
			environment.writeLine("Zatvaram program...");
			return CommandStatus.EXIT;
		}

		environment.writeLine("Dokument: " + filePath);
		environment.writeLine("----------------------------------------------------------------");
		environment.writeLine(fileContent);
		environment.writeLine("----------------------------------------------------------------");
		return CommandStatus.CONTINUE;
	}

}
