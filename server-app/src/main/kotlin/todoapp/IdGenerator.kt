package todoapp

import org.springframework.stereotype.Component
import java.util.UUID

@Component
class IdGenerator {

    fun gen(): String = UUID.randomUUID().toString()
}