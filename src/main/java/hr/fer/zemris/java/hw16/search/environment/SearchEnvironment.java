package hr.fer.zemris.java.hw16.search.environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw16.search.Console;

/**
 * Razred koji implementira sučelje {@link IEnvironment}. Ovaj razred koristi se
 * kao okruženje rada programa {@link Console}.
 * 
 * @see IEnvironment
 * @see Console
 * 
 * @author Davor Češljaš
 */
public class SearchEnvironment implements IEnvironment {

	/** Članska varijabla koja predstavlja ulaz ovog okruženja */
	private BufferedReader reader;

	/** Članska varijabla koja predstavlja izlaz ovog okruženja */
	private BufferedWriter writer;

	/**
	 * Članska varijabla koja predstavlja zadnje postavljene rezultate
	 * pretraživanja
	 */
	private List<Result> results;

	/**
	 * Članska varijabla koja predstavlja zadnje postavljene argumente
	 * pretraživanja
	 */
	private List<String> arguments;

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. Konstruktoru je
	 * potrebno predati primjerak sučelja {@link InputStream}, a koji će se
	 * koristiti kao ulaz okruženja te primjerak sučelja {@link OutputStream}
	 * koji će se koristiti kao izlaz ovog okruženja
	 *
	 * @param is
	 *            primjerak sučelja {@link InputStream} koji će se koristiti kao
	 *            ulaz okruženja
	 * @param os
	 *            primjerak sučelja {@link OutputStream} koji će se koristiti
	 *            kao izlaz ovog okruženja
	 */
	public SearchEnvironment(InputStream is, OutputStream os) {
		if (is == null || os == null) {
			throw new IllegalArgumentException("Niti ulazni niti izlazni tok podataka ne smiju biti null");
		}

		reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
		writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
	}

	@Override
	public String readLine() {
		try {
			String input = reader.readLine();
			return input == null ? null : input.trim();
		} catch (IOException e) {
		}

		return null;
	}

	@Override
	public void write(String output) {
		try {
			if (output == null) {
				return;
			}

			writer.write(output);
			writer.flush();
		} catch (IOException e) {
		}
	}

	@Override
	public void writeLine(String outputLine) {
		if (outputLine == null) {
			return;
		}

		write(outputLine + "\n");
	}

	@Override
	public List<Result> getResults() {
		return results;
	}

	@Override
	public void setResults(List<Result> results) {
		this.results = Collections.unmodifiableList(results);
	}

	@Override
	public List<String> getArguments() {
		return arguments;
	}

	@Override
	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}

	@Override
	public void printResults() {
		if (results == null) {
			writeLine("Rezultati još nisu definirani. Niste upisali niti jedan upit");
			return;
		}

		for (int i = 0, len = results.size(); i < len; i++) {
			writeLine(String.format("[%2d] %s", i, results.get(i)));
		}
	}

	@Override
	protected void finalize() throws Throwable {
		reader.close();
		writer.close();
	}
}
