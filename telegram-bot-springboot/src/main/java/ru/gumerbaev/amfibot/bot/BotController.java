package ru.gumerbaev.amfibot.bot;

import io.reactivex.Maybe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.gumerbaev.amfibot.api.TelegramBotClient;
import ru.gumerbaev.amfibot.api.model.TelegramResponse;
import ru.gumerbaev.amfibot.api.model.Update;
import ru.gumerbaev.amfibot.bot.handler.UpdateHandler;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BotController {

	private static final Logger LOG = LoggerFactory.getLogger(BotController.class);

	@Autowired
	private UpdateHandler handler;

	@Autowired
	private TelegramBotClient botClient;

	@PostMapping(value = "/webhook")
	public void webhook(@RequestBody Update update) {
		handler.handleUpdate(update);
	}

	@GetMapping(value = "/poll")
	public String poll(@RequestParam(name = "count", required = false, defaultValue = "1") int count) {
		Maybe<TelegramResponse<Update>> updatesMaybe = botClient.getUpdates();
		updatesMaybe.subscribe(res -> {
			List<Update> updates = res.getResult();
			updates.stream().limit(count).forEach(this::webhook);
		}, err -> {
			LOG.error(err.getMessage(), err);
		});
		return "Polling updates";
	}

	@GetMapping(value = "/printUpdates")
	public void getUpdates() {
		Maybe<TelegramResponse<Update>> updatesMaybe = botClient.getUpdates();
		updatesMaybe.subscribe(res -> {
			List<Update> updates = res.getResult();
			String collect = updates.stream().map(update -> update.getMessage().getText())
					.collect(Collectors.joining(", "));
			LOG.info(collect);
		}, err -> {
			LOG.error(err.getMessage(), err);
		});
	}
}
