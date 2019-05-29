package com.iot.temperature;

import javax.cache.expiry.Duration;
import javax.cache.expiry.ExpiryPolicy;

public class TemperatureExpiryPolicy implements ExpiryPolicy{

	@Override
	public Duration getExpiryForCreation() {
		return Duration.TEN_MINUTES;
	}

	@Override
	public Duration getExpiryForAccess() {
		return Duration.TEN_MINUTES;
	}

	@Override
	public Duration getExpiryForUpdate() {
		return Duration.TEN_MINUTES;
	}

}
