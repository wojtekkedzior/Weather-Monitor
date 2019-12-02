package com.iot.temperature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.cache.event.CacheEntryCreatedListener;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryListenerException;
import javax.cache.event.CacheEntryUpdatedListener;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

@Component
public class SimpleHourlyAverageCacheEntryListener implements CacheEntryCreatedListener<Integer, ConcurrentSkipListMap<LocalDateTime, Number>>, CacheEntryUpdatedListener<Integer, ConcurrentSkipListMap<LocalDateTime, Number>>, Serializable {

	private static final long serialVersionUID = -5147817867852884511L;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void onUpdated(Iterable<CacheEntryEvent<? extends Integer, ? extends ConcurrentSkipListMap<LocalDateTime, Number>>> events) throws CacheEntryListenerException {
		Iterator<CacheEntryEvent<? extends Integer, ? extends ConcurrentSkipListMap<LocalDateTime, Number>>> iterator = events.iterator();
//			iterator.forEachRemaining(cacheEntryEvent -> () -> cacheEntryEvent.getSource());

		log.info("update cache" );
	}
	@Override
	public void onCreated(Iterable<CacheEntryEvent<? extends Integer, ? extends ConcurrentSkipListMap<LocalDateTime, Number>>> events) throws CacheEntryListenerException {
		log.info("create cache entry" );
	}
}