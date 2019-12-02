package com.iot.temperature;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.cache.event.CacheEntryCreatedListener;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryListenerException;
import javax.cache.event.CacheEntryUpdatedListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.iot.temperature.model.Temperature;

@Component
public class SimpleCacheEntryListener implements CacheEntryCreatedListener<Integer, List<Temperature>>, CacheEntryUpdatedListener<Integer, List<Temperature>>, Serializable {

	private static final long serialVersionUID = -5147817867852884511L;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void onUpdated(Iterable<CacheEntryEvent<? extends Integer, ? extends List<Temperature>>> events) throws CacheEntryListenerException {
		Iterator<CacheEntryEvent<? extends Integer, ? extends List<Temperature>>> iterator = events.iterator();
//			iterator.forEachRemaining(cacheEntryEvent -> () -> cacheEntryEvent.getSource());

		log.info("update cache" );
	}
	@Override
	public void onCreated(Iterable<CacheEntryEvent<? extends Integer, ? extends List<Temperature>>> events) throws CacheEntryListenerException {
		log.info("create cache entry" );
	}
}