package hr.fer.zemris.java.hw16.search.environment;

/**
 * Razred koji predstavlja jedan rezultat pretraživanja. Primjerci ovog razreda
 * sastoje se od sličnosti <b>similarity</b>, koja se može dohvatiti metodom
 * {@link #getSimilarity()} te putanje do datoteke koja se uspoređivala
 * <b>filePath</b> koja se može dohvatiti sa {@link #getFilePath()}.
 * 
 * @author Davor Češljaš
 */
public class Result implements Comparable<Result> {

	/**
	 * Članska varijabla koja predstavlja sličnost datoteke sa argumentima
	 * pretraživanja
	 */
	private double similarity;

	/**
	 * Članska varijabla koja predstavlja putanju datoteke koja se uspoređivala
	 * sa argumentima pretraživanja
	 */
	private String filePath;

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. Unutar
	 * konstruktora pripadne članske varijable postavljaju se na predane
	 * parametre <b>similarity</b> i <b>filePath</b>
	 *
	 * @param similarity
	 *            sličnost datoteke sa argumentima pretraživanja
	 * @param filePath
	 *            putanju datoteke koja se uspoređivala sa argumentima
	 *            pretraživanja
	 */
	public Result(double similarity, String filePath) {
		this.similarity = similarity;
		this.filePath = filePath;
	}

	/**
	 * Metoda koja dohvaća sličnost datoteke sa argumentima pretraživanja
	 *
	 * @return sličnost datoteke sa argumentima pretraživanja
	 */
	public double getSimilarity() {
		return similarity;
	}

	/**
	 * Metoda koja dohvaća putanju datoteke koja se uspoređivala sa argumentima
	 * pretraživanja
	 *
	 * @return putanju datoteke koja se uspoređivala sa argumentima
	 *         pretraživanja
	 */
	public String getFilePath() {
		return filePath;
	}


	@Override
	public int compareTo(Result o) {
		return -Double.compare(this.similarity, o.similarity);
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
		Result other = (Result) obj;
		if (filePath == null) {
			if (other.filePath != null)
				return false;
		} else if (!filePath.equals(other.filePath))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("(%.4f) %s", similarity, filePath);
	}
}
