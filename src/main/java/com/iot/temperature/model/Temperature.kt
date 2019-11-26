package com.iot.temperature.model

import lombok.Data
import lombok.NoArgsConstructor
import lombok.Value

import javax.persistence.*
import javax.validation.constraints.NotNull
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Data
@NoArgsConstructor
class Temperature : Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    internal var id: Long? = null

    internal var temperature: Double? = null
    internal var humidity: Float? = null
    internal var pressure: Float? = null

    @NotNull
    internal var timestamp:
    //	@Temporal(TemporalType.TIMESTAMP)
            LocalDateTime? = null

    companion object {

        private const val serialVersionUID = 4198943515224478533L
    }
}
