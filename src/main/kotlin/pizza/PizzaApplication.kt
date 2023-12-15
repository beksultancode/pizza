package pizza

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import jakarta.persistence.Table
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import java.util.*

@SpringBootApplication
class PizzaApplication

fun main(args: Array<String>) {
    runApplication<PizzaApplication>(*args)
}

@Configuration
class CorsConfig {
    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.addAllowedOrigin("http://localhost:5173")
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }
}

@Component
class Pizaaaaa(private val pizzaRepo: PizzaRepo) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        val pizzas = listOf(
                Pizza("https://cdn.pixabay.com/photo/2017/12/09/08/18/pizza-3007395_960_720.jpg", "Pizza Margherita", "Tomato sauce, mozzarella, and oregano", 5.99),
                Pizza("https://cdn.pixabay.com/photo/2017/12/09/08/18/pizza-3007395_960_720.jpg", "Pizza Marinara", "Tomato sauce, garlic and basil", 6.99),
                Pizza("https://cdn.pixabay.com/photo/2017/12/09/08/18/pizza-3007395_960_720.jpg", "Pizza Quattro Stagioni", "Tomato sauce, mozzarella, mushrooms, ham, artichokes, olives, and oregano", 7.99),
                Pizza("https://cdn.pixabay.com/photo/2017/12/09/08/18/pizza-3007395_960_720.jpg", "Pizza Carbonara", "Tomato sauce, mozzarella, parmesan, eggs, and bacon", 8.99),
                Pizza("https://cdn.pixabay.com/photo/2017/12/09/08/18/pizza-3007395_960_720.jpg", "Pizza Frutti di Mare", "Tomato sauce and seafood", 9.99),
                Pizza("https://cdn.pixabay.com/photo/2017/12/09/08/18/pizza-3007395_960_720.jpg", "Pizza Quattro Formaggi", "Tomato sauce, mozzarella, parmesan, gorgonzola cheese, artichokes, and oregano", 10.99),
                Pizza("https://cdn.pixabay.com/photo/2017/12/09/08/18/pizza-3007395_960_720.jpg", "Pizza Crudo", "Tomato sauce, mozzarella and Parma ham", 11.99),
                Pizza(null, "Pizza Napoli", "Tomato sauce, mozzarella, oregano, anchovies", 12.99));
        pizzaRepo.saveAll(pizzas)
        println("Pizzas added to database!")
    }
}

@RestController
@RequestMapping("/pizza")
class PizzaHttpController(private val pizzaRepo: PizzaRepo) {

    @GetMapping("/all")
    fun getAllPizzas(): List<Pizza?>? {
        return pizzaRepo.findAll()
    }

    @GetMapping("/get/{id}")
    fun getPizza(@PathVariable id: UUID): Pizza? {
        return pizzaRepo.findById(id).orElse(null)
    }

    @PostMapping("/add")
    fun addPizza(@RequestBody pizzaRequest: PizzaRequest): Pizza? {
        return pizzaRepo.save(pizzaRequest.toPizza())
    }

    @DeleteMapping("/delete/{id}")
    fun deletePizza(@PathVariable id: UUID) {
        pizzaRepo.deleteById(id)
    }

    @PutMapping("/update/{id}")
    fun updatePizza(@PathVariable id: UUID, @RequestBody pizzaRequest: PizzaRequest): Pizza? {
        val pizza = pizzaRepo.findById(id).orElseThrow();
        pizza.setName(pizzaRequest.name);
        pizza.setPrice(pizzaRequest.price);
        return pizzaRepo.save(pizza);
    }
}

data class PizzaRequest(val img: String,
                        val name: String,
                        val description: String,
                        val price: Double) {
    fun toPizza(): Pizza {
        return Pizza(img, name, description, price)
    }
}

interface PizzaRepo : JpaRepository<Pizza, UUID> {}

@Entity
@Table(name = "pizza")
class Pizza {
    @Id
    private var id: UUID? = null
    private var img: String? = null
    private var name: String? = null
    private var description: String? = null
    private var price: Double? = null

    constructor() {}
    constructor(img: String?, name: String?, description: String?, price: Double?) {
        this.img = img
        this.name = name
        this.description = description
        this.price = price
    }


    @PrePersist
    fun prePersist() {
        id = UUID.randomUUID()
    }

    fun getId(): UUID? {
        return id
    }

    fun setId(id: UUID?) {
        this.id = id
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getPrice(): Double? {
        return price
    }

    fun setPrice(price: Double?) {
        this.price = price
    }

    fun getImg(): String? {
        return img
    }

    fun setImg(img: String?) {
        this.img = img
    }

    fun getDescription(): String? {
        return description
    }

    fun setDescription(description: String?) {
        this.description = description
    }
}
