package hr.fer.zemris.java.hw16.search.commands;

import hr.fer.zemris.java.hw16.search.Console;
import hr.fer.zemris.java.hw16.search.environment.IEnvironment;

/**
 * Sučelje koje predstavlja apstraktnu naredbu programa {@link Console}. Ovo
 * sučelje nudi samo jednu metodu, koju implementator sučelja mora
 * implementirati: {@link #execute(IEnvironment)}. Metodi se predaje samo jedan
 * argument a to je okruženje unutar kojeg se program {@link Console} izvodi, a
 * koje je implementirano sučeljem {@link IEnvironment}.
 * 
 * @see Console
 * @see IEnvironment
 * 
 * @author Davor Češljaš
 */
public interface ICommand {

	/**
	 * Metoda koja se poziva kada je potrebno izvršiti koju primjerak razreda
	 * koji implementira ovo sučelje predstavlja. Metodi se kao parametar mora
	 * predati primjerak razreda koji implementira sučelje {@link IEnvironment},
	 * a koji predstavlja okruženje izvođenja programa. Ovo sučelje koristiti će
	 * se za ispis svih poruka, dohvat prijašnjih rezultata...
	 *
	 * @param environment
	 *            primjerak razreda koji implementira sučelje
	 *            {@link IEnvironment}, a koji predstavlja okruženje izvođenja
	 *            programa
	 * @return jedan od status definiranih(i opisanih) enumeracijom
	 *         {@link CommandStatus}
	 * 
	 * @see CommandStatus
	 * @see IEnvironment
	 */
	CommandStatus execute(IEnvironment environment);
}
