package ru.gumerbaev.amfibot.bot;

import io.reactivex.Maybe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.gumerbaev.amfibot.api.TelegramBotClient;
import ru.gumerbaev.amfibot.api.model.Message;

@Component
@Profile("admin")
public class AdminComponent {

	@Autowired
	private BotProperties botProperties;

	@Autowired
	private TelegramBotClient botClient;

	@Scheduled(cron = "${bot.admin-birthday}")
	public void sendBirthdayReminder() {
		int chatId = botProperties.getAdminChatId();
		Maybe<Message> sendMessage = botClient.sendMessage(chatId, "Happy birthday!");
		sendMessage.subscribe();
	}
}
