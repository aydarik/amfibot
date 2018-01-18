package ru.gumerbaev.amfibot.bot;

import org.springframework.context.annotation.Bean;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import ru.gumerbaev.amfibot.api.TelegramBotClient;
import ru.gumerbaev.amfibot.bot.jenkins.JenkinsApi;

@org.springframework.context.annotation.Configuration
public class Configuration {

	@Bean
	public Builder retrofitBuilder() {
		return new Retrofit.Builder().addConverterFactory(JacksonConverterFactory.create())
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
	}

	@Bean
	public JenkinsApi jenkinsApi(Builder builder) {
		return builder.baseUrl(JenkinsApi.BASE_URL).build().create(JenkinsApi.class);
	}

	@Bean
	public TelegramBotClient telegramBot(Builder builder, BotProperties botProperties) {
		return builder.baseUrl(botProperties.getApiUrl() + botProperties.getApiKey() + "/").build()
				.create(TelegramBotClient.class);
	}

}
