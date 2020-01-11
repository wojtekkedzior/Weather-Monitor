package temperature

import javax.cache.expiry.Duration
import javax.cache.expiry.ExpiryPolicy

class TemperatureExpiryPolicy : ExpiryPolicy {
    override fun getExpiryForCreation(): Duration {
        return Duration.TEN_MINUTES
    }

    override fun getExpiryForAccess(): Duration {
        return Duration.TEN_MINUTES
    }

    override fun getExpiryForUpdate(): Duration {
        return Duration.TEN_MINUTES
    }
}