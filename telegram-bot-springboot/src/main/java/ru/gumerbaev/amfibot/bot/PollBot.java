package ru.gumerbaev.amfibot.bot;

import io.reactivex.Maybe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.gumerbaev.amfibot.api.TelegramBotClient;
import ru.gumerbaev.amfibot.api.model.TelegramResponse;
import ru.gumerbaev.amfibot.api.model.Update;
import ru.gumerbaev.amfibot.bot.handler.UpdateHandler;

import java.util.List;

@Component
@Profile("poll")
public class PollBot {
	private static final Logger log = LoggerFactory.getLogger(PollBot.class);

	@Autowired
	private UpdateHandler handler;

	@Autowired
	private TelegramBotClient botClient;

	private int updateIdOffset = 0;

	@Scheduled(fixedRate = 10000)
	public void reportCurrentTime() {
		Maybe<TelegramResponse<Update>> updatesMaybe = botClient.getUpdates(updateIdOffset);
		updatesMaybe.subscribe(res -> {
			List<Update> updates = res.getResult();
			updates.forEach(PollBot.this::handleUpdate);
		}, err -> {
			log.error(err.getMessage(), err);
		});
	}

	private void handleUpdate(Update update) {
		updateIdOffset = update.getUpdate_id() + 1;
		handler.handleUpdate(update);
	}
}
