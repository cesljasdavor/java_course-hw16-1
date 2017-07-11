package hr.fer.zemris.java.hw16.search;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.hw16.search.commands.CommandStatus;
import hr.fer.zemris.java.hw16.search.commands.ExitCommand;
import hr.fer.zemris.java.hw16.search.commands.ICommand;
import hr.fer.zemris.java.hw16.search.commands.QueryCommand;
import hr.fer.zemris.java.hw16.search.commands.ResultsCommand;
import hr.fer.zemris.java.hw16.search.commands.TypeCommand;
import hr.fer.zemris.java.hw16.search.environment.IEnvironment;
import hr.fer.zemris.java.hw16.search.environment.SearchEnvironment;
import hr.fer.zemris.java.hw16.search.provider.SearchProvider;

/**
 * Razred koji predstavlja program za pretraživanje tekstualnih datoteka. Ovaj
 * program kao argument naredbenog redka prima putanju do direktorija koji
 * sadrži datoteke koje se pretražuju. Pretraživanje se vrši putem algoritma
 * <a href = "https://en.wikipedia.org/wiki/Tf%E2%80%93idf">TF-IDF(engl. term
 * frequency–inverse document frequency)</a>. Korisniku se nude 4 moguće naredbe
 * koje su navedene u nastavku:
 * <ul>
 * <li>query - opisan u razredu {@link QueryCommand}</li>
 * <li>type - opisan u razredu {@link TypeCommand}</li>
 * <li>results - opisan u razredu {@link ResultsCommand}</li>
 * <li>exit - opisan u razredu {@link ExitCommand}</li>
 * </ul>
 * 
 * @see QueryCommand
 * @see TypeCommand
 * @see ResultsCommand
 * @see ExitCommand
 * 
 * @author Davor Češljaš
 */
public class Console {

	/**
	 * Konstanta koja predstavlja {@link Map} unutar koje su imena naredbi
	 * mapirane na implementacije samih naredbi(naredbe implementiraju sučelje
	 * {@link ICommand})
	 */
	public static final Map<String, ICommand> COMMANDS;

	static {
		COMMANDS = new HashMap<>();
		COMMANDS.put(ExitCommand.NAME, new ExitCommand());
		COMMANDS.put(QueryCommand.NAME, new QueryCommand());
		COMMANDS.put(TypeCommand.NAME, new TypeCommand());
		COMMANDS.put(ResultsCommand.NAME, new ResultsCommand());
	}

	/**
	 * Metoda od koje započinje izvođenje programa
	 *
	 * @param args
	 *            argumenti naredbenog redka. Unutar ovog programa koristi se
	 *            samo jedan argument, a koji predstavlja putanju do direktorija
	 *            sa tekstualnim datotekama koje se pretražuju
	 * @throws IOException
	 *             Ukoliko nije moguće moguće otvoriti ili čitati neku od
	 *             datoteka sa iz predanog direktorija ili nije moguće otvoriti
	 *             datoteku sa zaustavnim riječima
	 */
	public static void main(String[] args) throws IOException {
		IEnvironment environment = new SearchEnvironment(System.in, System.out);

		if (args.length == 0) {
			environment.writeLine("Za rad sustava potrebna je putanja do direktorija sa dokumentima");
			System.exit(-1);
		}

		SearchProvider.loadDocuments(args[0]);
		environment.writeLine(
				String.format("Veličina riječnika je %d riječi", SearchProvider.getProvider().getVocabulary().size()));

		while (true) {
			environment.write("Unesite naredbu > ");

			String input = environment.readLine();
			if (input.isEmpty()) {
				environment.writeLine("Niste unijeli niti jednu naredbu.");
				continue;
			}

			List<String> inputSplitted = parseInput(input);
			ICommand command = COMMANDS.get(inputSplitted.get(0));
			if (command == null) {
				environment.writeLine("Nepoznata naredba.");
				continue;
			}
			// makni ime naredbe naredba
			inputSplitted.remove(0);
			environment.setArguments(inputSplitted);

			if (command.execute(environment) == CommandStatus.EXIT) {
				break;
			}
		}

	}

	/**
	 * Pomoćna statička metoda koja se koristi za parsiranje korisnikova unosa
	 * <b>input</b>. Metoda predani primjerak razreda {@link String} rastavlja
	 * po svim prazninama te rezultat vraća kao {@link List} primjeraka razreda
	 * {@link String}
	 *
	 * @param input
	 *            korisnikov unos koji se parsira
	 * @return {@link List} primjeraka razreda {@link String} koji su anstali
	 *         parsiranjem korisnikova unosa <b>input</b>
	 */
	private static List<String> parseInput(String input) {
		String[] splitted = input.split("\\s+");
		return new LinkedList<>(Arrays.asList(splitted));
	}
}
