package client.windows;

import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.util.*;
import server.AppConfig;
import server.model.*;

/**
 * Interfaccia per la gestione delle richieste REST effettuate
 * dalla finestra {@link WEvent}.
 */

public interface WEventRest {
	
	public String url = AppConfig.getURL() +"api/event";
	
	@SuppressWarnings("null")
	public default List <Event> getEventi(RestTemplate restTemplate) {
		return Arrays.asList(restTemplate.getForObject(url, Event[].class));
	}
	
	/** Restituisce l'evento con l'ID specificato */
	public default Event getEvento(String nome, RestTemplate restTemplate) {
		return restTemplate.getForObject(url+"/"+nome, Event.class);
	}
	
	/** Aggiunge un nuovo evento */
	public default Event addEvento(RestTemplate restTemplate) {
		return  restTemplate.getForObject(url+"/add", Event.class);	
	}
	
	/** Aggiorna un evento e restituisce l'evento aggiornato */
	public default Event updateEvento(Event evento, RestTemplate restTemplate) {
		restTemplate.put(url+"/"+evento.getNome()+"/update", evento);
		return getEvento(evento.getNome(), restTemplate);
	}

	/** Ottiene l'elenco delle sale libere nel giorno specificato + riga vuota*/
	public default String[] getSaleLibere(LocalDate date, RestTemplate restTemplate) {
		String urlSaleLibere = AppConfig.getURL() + "api/hall/libere/" + date.toString();
		List<String> saleNames = new ArrayList<>(Arrays.asList(restTemplate.getForObject(urlSaleLibere, String[].class)));
		saleNames.add(0, "");
		return saleNames.toArray(new String[0]);
	}
	
	/** Libera una sala nel giorno specificato */
	public default void liberaSala(String nome, LocalDate data, RestTemplate restTemplate) {
		if (nome != null && !nome.equals("") && data != null){
			String urlHall = AppConfig.getURL() + "api/hall/" + nome + "/state";
			restTemplate.put(urlHall, new StateDate(data, State.DISPONIBILE));
		}
	}

	/** Occupa una sala nel giorno specificato */
	public default void occupaSala(String nome, LocalDate data,RestTemplate restTemplate) {
		if (nome != null && !nome.equals("") && data != null){
			String urlHall = AppConfig.getURL() + "api/hall/" + nome + "/state";
			restTemplate.put(urlHall, new StateDate(data, State.PRENOTATA));
		}
	}
	

}
