package hr.fer.zemris.java.hw16.search.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hr.fer.zemris.java.hw16.search.environment.IEnvironment;
import hr.fer.zemris.java.hw16.search.environment.Result;
import hr.fer.zemris.java.hw16.search.provider.DocumentVector;
import hr.fer.zemris.java.hw16.search.provider.SearchProvider;

/**
 * Razred koji implementira sučelje {@link ICommand}. Primjerci ovog razreda
 * pozivom metode {@link #execute(IEnvironment)} šalju zahtjev za pretraživanje
 * tekstualnih datoteka koje su učitane, a koristeći pri tome algoritam
 * algoritma
 * <a href = "https://en.wikipedia.org/wiki/Tf%E2%80%93idf">TF-IDF(engl. term
 * frequency–inverse document frequency)</a>. Kao rezultat ispisuje se
 * maksimalno {@value #MAX_RESULTS} rezultata sa prikazanim sličnostima u
 * padajućem redoslijedu.
 * 
 * @see ICommand
 * 
 * @author Davor Češljaš
 */
public class QueryCommand implements ICommand {

	/**
	 * Konstanta koja predstavlja naziv ove naredbe, ujedino i ključna riječ
	 * koju korisnik upisuje kako bi pokrenuo ovu naredbu
	 */
	public static final String NAME = "query";

	/**
	 * Konstanta koja predstavlja maksimalan broj rezultata pretraživanja koji
	 * će se ispisati korisniku pozivom metode {@link #execute(IEnvironment)}
	 */
	private static final int MAX_RESULTS = 10;

	@Override
	public CommandStatus execute(IEnvironment environment) {
		List<String> args = environment.getArguments();

		args = args.stream().filter(word -> SearchProvider.getProvider().getVocabulary().contains(word))
				.collect(Collectors.toList());

		if (args.size() == 0) {
			environment.writeLine("Upit je prazan, molimo Vas unesite drugi upit");
			return CommandStatus.CONTINUE;
		}

		environment.writeLine("Upit je: " + args.toString());

		environment.setResults(calculateResults(args));

		environment.writeLine("Najboljih 10 rezultata:");
		environment.printResults();

		return CommandStatus.CONTINUE;
	}

	/**
	 * Pomoćna metoda koja se koristi za računje rezultata pretraživanja. Metoda
	 * prima argumente koji se pretražuju preko parametra <b>args</b>. Za
	 * pretraživanje se koristi algoritam
	 * <a href = "https://en.wikipedia.org/wiki/Tf%E2%80%93idf">TF-IDF(engl.
	 * term frequency–inverse document frequency)</a>. Metoda vraća {@link List}
	 * primjeraka razreda {@link Result}, a koji predstavljaju rezultate
	 * pretraživanja
	 *
	 * @param args
	 *            {@link List} primjeraka razreda {@link String} koji
	 *            predstavljaju riječi koje se pretražuju
	 * @return {@link List} primjeraka razreda {@link Result}, a koji
	 *         predstavljaju rezultate pretraživanja
	 */
	private List<Result> calculateResults(List<String> args) {
		Map<String, Integer> wordTimes = SearchProvider.countWordsInDocument(args.toArray(new String[0]));

		DocumentVector queryVector = SearchProvider.createDocumentVector(NAME, wordTimes);

		List<Result> results = new ArrayList<>();
		for (DocumentVector documentVector : SearchProvider.getProvider().getDocumentVectors()) {
			results.add(new Result(DocumentVector.calculateSimilarity(queryVector, documentVector),
					documentVector.getFilePath()));
		}

		results = results.stream().filter(res -> res.getSimilarity() > 0).sorted().limit(MAX_RESULTS)
				.collect(Collectors.toList());

		return results;
	}
}
