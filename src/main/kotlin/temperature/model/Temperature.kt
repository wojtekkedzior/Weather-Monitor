package temperature.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import lombok.AccessLevel
import lombok.Data
import lombok.NoArgsConstructor
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotNull

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class Temperature : Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    internal var id: Long? = null

    @JsonProperty("temperature")
    internal var temperature: Double? = null

    @JsonProperty("humidity")
    internal var humidity: Float? = null

    @JsonProperty("pressure")
    internal var pressure: Float? = null

    @NotNull
    @JsonFormat(locale = "cz", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "Europe/Budapest")
    internal var timestamp: LocalDateTime? = null

    companion object {
        private const val serialVersionUID = 4198943515224478533L
    }
}
