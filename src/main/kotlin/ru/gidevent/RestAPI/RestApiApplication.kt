package ru.gidevent.RestAPI

import jakarta.security.auth.message.AuthException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import ru.gidevent.RestAPI.auth.AuthenticationService
import ru.gidevent.RestAPI.auth.RegisterUserDto

@SpringBootApplication
class RestApiApplication

	fun main(args: Array<String>) {
		runApplication<RestApiApplication>(*args)
	}

	/*@Bean
	fun dbPrepopulate(authenticationService: AuthenticationService): CommandLineRunner {
		return CommandLineRunner {
			authenticationService.signup(
					RegisterUserDto(
							"admin",
							"admin",
							"admin",
							"admin",
							"",
							"ADMIN"
					)
			)
		}
	}*/

@Component
class CommandLineRunnerImpl: CommandLineRunner{
	@Autowired
	lateinit var authService: AuthenticationService
	override fun run(vararg args: String?) {
		try {
			authService.signup(
					RegisterUserDto(
							"admin",
							"admin",
							"admin",
							"admin",
							"",
							"ADMIN"
					)
			)
		}catch (e: AuthException){
			println("Администратор зарегистрирован")
		}

	}

}

