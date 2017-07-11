package hr.fer.zemris.java.hw16.search.provider;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Razred koji se koristi kao pomoćni razred prilikom pretrage tekstualnih
 * datoteka. Razred predstavlja implementaciju oblikovnog obrasca
 * <a href = "https://en.wikipedia.org/wiki/Singleton_pattern">jedinstveni
 * objekt</a> (engl. Singleton). Uz dohvat jedinog mogućeg primjerka ovog
 * razreda metodom {@link #getProvider()}, razred nudi i nekoliko statičkih
 * metodi:
 * 
 * <ul>
 * <li>{@link #loadDocuments(String)}</li>
 * <li>{@link #countWordsInDocument(String[])}</li>
 * <li>{@link #createDocumentVector(String, Map)}</li>
 * </ul>
 * 
 * Primjerci ovog razreda također nude nekoliko metoda, za dohvat standardnih
 * objekata prilikom korištenja algoritma pretraživanja
 * <a href = "https://en.wikipedia.org/wiki/Tf%E2%80%93idf">TF-IDF(engl. term
 * frequency–inverse document frequency)</a> :
 * <ul>
 * <li>{@link #getVocabulary()}</li>
 * <li>{@link #getIdf()}</li>
 * <li>{@link #getDocumentVectors()}</li>
 * </ul>
 * 
 * @author Davor Češljaš
 */
public class SearchProvider {

	/**
	 * Konstanta koja predstavlja regularni izraz na temelju kojeg se određuju
	 * riječi unutar datoteka
	 */
	private static final String SPLIT_REGEX = "[^A-Za-zčČćĆžŽšŠđĐ]+";

	/** Konstanta koja predstavlja {@link Set} zaustavnih riječi */
	private static Set<String> STOPWORDS;

	/** Statička varijabla koja predstavlja jedini primjerak ovog razreda */
	private static SearchProvider provider = null;

	/**
	 * Članska varijabla koja predstavlja {@link List} riječi koje predstavljaju
	 * vokabular pretraživanja
	 */
	private List<String> vocabulary;

	/**
	 * Članska varijabla koja predstavlja {@link List} svih vektora dokumenata
	 * ,učitanih sa statičkom metodom
	 * {@link #loadDocuments(String)},predstavljenih primjercima razreda
	 * {@link DocumentVector}
	 */
	private List<DocumentVector> documentVectors;

	/**
	 * Članska varijabla koja predstavlja vektor idf unutar algoritma
	 * pretraživanja
	 * <a href = "https://en.wikipedia.org/wiki/Tf%E2%80%93idf">TF-IDF(engl.
	 * term frequency–inverse document frequency)</a>
	 */
	private double[] idf;

	/**
	 * Privatni konstruktor koji služi tome da se primjerci ovog razreda ne mogu
	 * stvarati izvan samog razreda.
	 */
	private SearchProvider() {
	}

	/**
	 * Metoda koja dohvaća {@link List} riječi koje predstavljaju vokabular
	 * pretraživanja
	 *
	 * @return {@link List} riječi koje predstavljaju vokabular pretraživanja
	 */
	public List<String> getVocabulary() {
		return vocabulary;
	}

	/**
	 * Metoda koja dohvaća {@link List} svih vektora dokumenata predstavljenih
	 * primjercima razreda {@link DocumentVector}
	 *
	 * @return {@link List} svih vektora dokumenata predstavljenih primjercima
	 *         razreda {@link DocumentVector}
	 */
	public List<DocumentVector> getDocumentVectors() {
		return documentVectors;
	}

	/**
	 * Metoda koja dohvaća vektor idf unutar algoritma pretraživanja
	 * <a href = "https://en.wikipedia.org/wiki/Tf%E2%80%93idf">TF-IDF(engl.
	 * term frequency–inverse document frequency)</a>
	 *
	 * @return vektor idf unutar gore spomenutog algoritma
	 */
	public double[] getIdf() {
		return idf;
	}

	/**
	 * Statička metoda koja dohvaća jedini primjerak ovog razreda
	 *
	 * @return tjedini primjerak ovog razreda
	 */
	public static SearchProvider getProvider() {
		return provider;
	}

	/**
	 * Statička metoda koja se koristi za učitavanje svih dokumenata koji se
	 * mogu pretražiti, a koji se nalaze unutar putanje predstavljene parametrom
	 * <b>the document directory name</b>. Ova metoda ujedino namješta jedini
	 * primjerak ovog razreda, kako bi se pomoću njega moglo pretraživati
	 * dokumente
	 *
	 * @param documentDirectoryName
	 *            parametar koji predstavlja putanju do direktorija unutar kojeg
	 *            se nalaze svi dokumenti koji se mogu pretraživati
	 * @throws IOException
	 *             Ukoliko se iti jedan dokument iz predanog direktorija ne može
	 *             pročitati.
	 */
	public static void loadDocuments(String documentDirectoryName) throws IOException {
		loadStopWords();

		File documentDirectory = new File(documentDirectoryName);
		if (!documentDirectory.exists()) {
			throw new IllegalArgumentException(String.format("Direktorij '%s' ne postoji", documentDirectoryName));
		}

		provider = new SearchProvider();

		Set<String> vocabularySet = new HashSet<>();

		Map<String, Map<String, Integer>> documentInfos = new HashMap<>();
		for (File document : documentDirectory.listFiles()) {
			documentInfos.put(document.getAbsolutePath(), parseDocument(document.toPath(), vocabularySet));
		}

		provider.vocabulary = new ArrayList<>(vocabularySet);

		createDocumentVectors(documentInfos);
	}

	/**
	 * Pomoćna statička metoda koja se koristi za učitavanje zaustavnih riječi
	 * te njihovu pohranu unutar statičke varijable {@link #STOPWORDS}
	 *
	 * @throws IOException
	 *             Ukoliko se datoteka sa zaustavnim riječima ne može pročitati.
	 */
	private static void loadStopWords() throws IOException {
		InputStream is = SearchProvider.class.getClassLoader().getResourceAsStream("stoprijeci.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

		STOPWORDS = new HashSet<>();
		while (reader.ready()) {
			STOPWORDS.add(reader.readLine().trim().toLowerCase());
		}

		STOPWORDS = Collections.unmodifiableSet(STOPWORDS);
	}

	/**
	 * Pomoćna statička metoda koja se koristi za parsiranje dokumenta
	 * predstavljenog putanjom <b>documentPath</b>. Prilikom parsiranja ovog
	 * dokumenta predani se parametar <b>vocabularySet</b> nadopunjuje sa novim
	 * riječima koji već nisu unutar tog {@link Set}a. Metoda vraća {@link Map}
	 * svih riječi koje su se pojavile unutar dokumenta (koje nisu unutar
	 * {@link #STOPWORDS}) namapirane na broj pojavaljivanja pojedine riječi
	 *
	 * @param documentPath
	 *            putanja do dokumenta koji se parsira predstavljen primjerkom
	 *            sučelja {@link Path}
	 * @param vocabularySet
	 *            vokabular koji se nadopunjuje novim riječima
	 * @return {@link Map} svih riječi koje su se pojavile unutar dokumenta
	 *         (koje nisu unutar {@link #STOPWORDS}) namapirane na broj
	 *         pojavaljivanja pojedine riječi
	 * @throws IOException
	 *             Ukoliko se datoteka sa putanjom <b>documentPath</b> ne može
	 *             pročitati.
	 */
	private static Map<String, Integer> parseDocument(Path documentPath, Set<String> vocabularySet) throws IOException {
		String fileContent = new String(Files.readAllBytes(documentPath), StandardCharsets.UTF_8);
		String[] splitted = fileContent.split(SPLIT_REGEX);

		Map<String, Integer> wordTimes = countWordsInDocument(splitted);
		vocabularySet.addAll(wordTimes.keySet());
		return wordTimes;
	}

	/**
	 * Statička metoda koja se koriste kako bi se iz predanog polja riječi
	 * predstavljenih primjercima razreda {@link String} <b>words</b>, stvorio
	 * primjerak sučelja {@link Map} koji unutar sebe mapira pojedinu riječ na
	 * broj ponavljanja unutar predanog parametra <b>words</b>
	 *
	 * @param words
	 *            polje riječi predstavljeno primjercima razreda {@link String}
	 * @return {@link Map} koji unutar sebe mapira pojedinu riječ na broj
	 *         ponavljanja unutar predanog parametra <b>words</b>
	 */
	public static Map<String, Integer> countWordsInDocument(String[] words) {
		Map<String, Integer> wordTimes = new HashMap<>();
		for (String word : words) {
			word = word.toLowerCase();
			if (STOPWORDS.contains(word)) {
				continue;
			}

			wordTimes.merge(word, 1, (currentValue, newValue) -> currentValue + newValue);
		}

		return wordTimes;
	}

	/**
	 * Pomoćna statička metoda koja prima jedan parametar, {@link Map} unutar
	 * koje su ključevi putanje do pojedine datotke, a vrijednosti {@link Map}
	 * koji unutar sebe mapira pojedinu riječ iz dokumenta na broj ponavljanja
	 * te riječi unutar istog dokumenata. Iz predanog parametra metoda stvara
	 * primjerke razreda {@link DocumentVector} za svaki pojedni unos unutar
	 * predanog parametra te ih sprema unutar članske varijable
	 * {@link #documentVectors}. Dodatno metoda poziva
	 * {@link #calculateIdf(Map)}, kako bi vektor <b>idf</b> bio prisutan
	 * prilikom stvaranja primjeraka razreda {@link DocumentVector}
	 *
	 * @param documentInfos
	 *            {@link Map} unutar koje su ključevi putanje do pojedine
	 *            datotke, a vrijednosti {@link Map} koji unutar sebe mapira
	 *            pojedinu riječ iz dokumenta na broj ponavljanja te riječi
	 *            unutar istog dokumenata.
	 */
	private static void createDocumentVectors(Map<String, Map<String, Integer>> documentInfos) {
		calculateIdf(documentInfos);

		provider.documentVectors = new ArrayList<>();
		for (Map.Entry<String, Map<String, Integer>> documentInfo : documentInfos.entrySet()) {
			provider.documentVectors.add(createDocumentVector(documentInfo.getKey(), documentInfo.getValue()));
		}
	}

	/**
	 * Pomoćna statička metoda koja se koristi za izračun vektora <b>idf</b>.
	 *
	 * @param documentInfos
	 *            {@link Map} unutar koje su ključevi putanje do pojedine
	 *            datotke, a vrijednosti {@link Map} koji unutar sebe mapira
	 *            pojedinu riječ iz dokumenta na broj ponavljanja te riječi
	 *            unutar istog dokumenata.
	 */
	private static void calculateIdf(Map<String, Map<String, Integer>> documentInfos) {
		int numOfFiles = documentInfos.size();
		provider.idf = new double[provider.vocabulary.size()];

		for (int i = 0; i < provider.idf.length; i++) {
			String word = provider.vocabulary.get(i);

			double count = documentInfos.values().stream().filter((wordTimes) -> wordTimes.containsKey(word)).count();

			provider.idf[i] = Math.log(numOfFiles / count);
		}
	}

	/**
	 * Statička metoda koja se koristi za stvaranja novog primjerka razreda
	 * {@link DocumentVector}, koristeći pri tome predane parametre
	 * <b>filePath</b> i <b>wordTimes</b>
	 *
	 * @param filePath
	 *            putanja do dokumenta
	 * @param wordTimes
	 *            {@link Map} koji unutar sebe mapira pojedinu riječ na broj
	 *            ponavljanja te riječi unutar dokumenta
	 * @return novi primjerak razreda {@link DocumentVector} stvoren iz predanih
	 *         parametara
	 */
	public static DocumentVector createDocumentVector(String filePath, Map<String, Integer> wordTimes) {
		double[] tf = new double[provider.vocabulary.size()];
		for (Map.Entry<String, Integer> entry : wordTimes.entrySet()) {
			tf[provider.vocabulary.indexOf(entry.getKey())] = entry.getValue();
		}

		return new DocumentVector(filePath, tf);
	}
}
