package hr.fer.zemris.java.hw16.search.provider;

/**
 * Razred koji predstavlja jedan vektor pretraživanja dokumenta. Pretraživanje
 * se ovdje svrši algoritmom
 * <a href = "https://en.wikipedia.org/wiki/Tf%E2%80%93idf">TF-IDF(engl. term
 * frequency–inverse document frequency)</a> te se stoga primjerci ovog razreda
 * sastoje od putanje do pripadnog dokumenta <b>filePath</b> te od polja realnih
 * brojeva dvostruke preciznosti <b>tfidf</b>, a koji upravo predstavlja
 * izgrađeni vektor pretraživanja. Primjerci ovog razreda mogu ustvrditi svoju
 * sličnost pozivom statičke metode {@link #calculateVectorAttributes(double[])}
 * 
 * @author Davor Češljaš
 */
public class DocumentVector {

	/** Članska varijabl koja predstavlja putanju do pripradne datoteke */
	private String filePath;

	/** Članska varijabla koja predstavlja izgrađeni vektor pretraživanja */
	private double[] tfidf;

	/** Članska varijabla koja predstavlja modul vektora pretraživanja */
	private double modul;

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. Unutar ovog
	 * konstruktora parametar <b>filePath</b> sprema se u pripdanu člansku
	 * varijablu, dok se pomoću vektora <b>tf</b> računa <b>tfidf</b>. Uvjet da
	 * će vektor <b>tfidf</b> biti ispravan je da je vektor dobiven pozivom
	 * metode {@link SearchProvider#getIdf()} ispravan.
	 * 
	 * @param filePath
	 *            putanja do pripradne datoteke
	 * @param tf
	 *            vektor na temelju kojeg se računa vektor <b>tfidf</b>
	 */
	public DocumentVector(String filePath, double[] tf) {
		this.filePath = filePath;
		calculateVectorAttributes(tf);
	}

	/**
	 * Pomoćna metoda koja se koristi za izračun vektora {@link #tfidf} i
	 * njegovog modula {@link #modul}, na temelju predanog vektora <b>tf</b>
	 *
	 * @param tf
	 *            vektor na temelju kojeg se računa vektor {@link #tfidf} i
	 *            njegov modul {@link #modul}
	 */
	private void calculateVectorAttributes(double[] tf) {
		double[] idf = SearchProvider.getProvider().getIdf();
		if (idf.length != tf.length) {
			throw new IllegalArgumentException("Vektori tf i idf nisu iste veličine");
		}

		tfidf = new double[idf.length];

		for (int i = 0; i < idf.length; i++) {
			tfidf[i] = tf[i] * idf[i];
			modul += tfidf[i] * tfidf[i];
		}

		modul = Math.sqrt(modul);
	}

	/**
	 * Metoda koja dohvaća putanju do pripradne datoteke vektora
	 *
	 * @return putanju do pripradne datoteke vektora
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * Metoda koja dohvaća vektor <b>tfidf</b> (izgrađeni vektor pretraživanja)
	 *
	 * @return izgrađeni vektor pretraživanja
	 */
	public double[] getTfidf() {
		return tfidf;
	}

	/**
	 * Metoda koja dohvaća modul vektora pretraživanja <b>tfidf</b>
	 *
	 * @return modul vektora pretraživanja <b>tfidf</b>
	 */
	public double getModul() {
		return modul;
	}

	/**
	 * Metoda koja se koristi za izračun sličnosti između dva primjerka razreda
	 * {@link DocumentVector}.
	 *
	 * @param first
	 *            prvi primjerak razreda {@link DocumentVector} na temelju kojeg
	 *            se računa sličnost
	 * @param second
	 *            drugi primjerak razreda {@link DocumentVector} na temelju
	 *            kojeg se računa sličnost
	 * @return sličnost predanih primjeraka razreda {@link DocumentVector}
	 *         <b>first</b> i <b>second</b> izražena realnim brojem
	 */
	public static double calculateSimilarity(DocumentVector first, DocumentVector second) {
		double numerator = scalarProduct(first.tfidf, second.tfidf);
		double denominator = first.modul * second.modul;

		return denominator == 0 ? 0 : numerator / denominator;
	}

	/**
	 * Pomoćna metoda koja računa skalarini produkt dva vektora predana kao
	 * parametri <b>firstVec</b> i <b>secondVec</b>.
	 *
	 * @param firstVec
	 *            prvi vektor na temelju kojeg se računa skalarni prodkut
	 *            vektora
	 * @param secondVec
	 *            drugi vektor na temelju kojeg se računa skalarni prodkut
	 *            vektora
	 * @return skalarni produkt vektora, kao realni broj
	 */
	private static double scalarProduct(double[] firstVec, double[] secondVec) {
		double scalarProduct = 0;
		for (int i = 0; i < firstVec.length; i++) {
			scalarProduct += firstVec[i] * secondVec[i];
		}

		return scalarProduct;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filePath == null) ? 0 : filePath.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentVector other = (DocumentVector) obj;
		if (filePath == null) {
			if (other.filePath != null)
				return false;
		} else if (!filePath.equals(other.filePath))
			return false;
		return true;
	}
}
