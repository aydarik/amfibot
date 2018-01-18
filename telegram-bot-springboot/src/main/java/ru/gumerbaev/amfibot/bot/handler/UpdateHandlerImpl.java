package ru.gumerbaev.amfibot.bot.handler;

import io.reactivex.Maybe;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.gumerbaev.amfibot.api.TelegramBotClient;
import ru.gumerbaev.amfibot.api.model.Chat;
import ru.gumerbaev.amfibot.api.model.Message;
import ru.gumerbaev.amfibot.api.model.Update;
import ru.gumerbaev.amfibot.api.model.User;
import ru.gumerbaev.amfibot.bot.jenkins.JenkinsApi;
import ru.gumerbaev.amfibot.bot.jenkins.JenkinsWrapper;
import ru.gumerbaev.amfibot.database.entities.Query;
import ru.gumerbaev.amfibot.database.repositories.QueryRepository;
import ru.gumerbaev.amfibot.database.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class UpdateHandlerImpl implements UpdateHandler {

	private Logger LOG = LoggerFactory.getLogger(UpdateHandlerImpl.class);

	@Autowired
	private JenkinsApi jenkinsApi;

	@Autowired
	private TelegramBotClient telegramBot;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private QueryRepository queryRepository;

	@Override
	public void handleUpdate(Update update) {
		Optional<Message> messageOptional = getMessage(update);

		messageOptional.ifPresent((Message message) -> {

			saveQuery(message);

			long chatId = message.getChat().getId();
			String text = message.getText();

			if (text != null) {
				LOG.debug("Chat id: " + chatId);
				LOG.debug("Text:    " + text);

/*
				int indexOf = text.indexOf(" ");

				if (indexOf > -1) {
					String queryString = text.substring(indexOf);
					// implement commands with query
				} else
*/
				if (text.startsWith("/jenkins")) {
					sendMessages(chatId, jenkinsApi.getStatus());
				} else if (text.startsWith("/start") || text.startsWith("/help")) {
					String username = getUserName(message);
					String sb = "Hello " + username + "!\nNice to meet you. I am Amfibot.";
					telegramBot.sendMessage(chatId, sb).subscribe();
				} else {
					Chat chat = message.getChat();
					if (Chat.TYPE_PRIVATE.equals(chat.getType())) {
						Maybe<Message> sendMessage = telegramBot.sendMessage(chatId,
								"This is not a proper command." + System
										.lineSeparator() + "You can send /help to get help.");
						sendMessage.subscribe(m -> {
							LOG.debug(m.getText());
						});
					}
				}
			}
		});
	}

	private String getUserName(Message message) {
		String username = message.getFrom().getUsername();
		if (username != null && !username.isEmpty()) {
			return username;
		}
		username = message.getFrom().getFirst_name();

		if (username != null && !username.isEmpty()) {
			String lastName = message.getFrom().getLast_name();
			if (lastName != null && !lastName.isEmpty()) {
				return username + " " + lastName;
			}
			return username;
		}

		return message.getChat().getUsername();
	}

	private Optional<Message> getMessage(Update update) {
		if (update.getMessage() != null) {
			return Optional.of(update.getMessage());
		} else if (update.getEdited_message() != null) {
			return Optional.of(update.getEdited_message());
		} else if (update.getChannel_post() != null) {
			return Optional.of(update.getChannel_post());
		} else if (update.getEdited_channel_post() != null) {
			return Optional.of(update.getEdited_channel_post());
		}

		return Optional.empty();
	}

	private void sendMessages(long chatId, Single<JenkinsWrapper> jenkinsJobs) {
		final List<String> brokenJobs = new ArrayList<>();
		jenkinsJobs.subscribe(jobs -> jobs.getJobs().forEach(jenkinsJob -> {
			if ("red".equals(jenkinsJob.getColor())) {
				brokenJobs.add(jenkinsJob.getName());
			}
		}), err -> LOG.error(err.getMessage(), err));

		if (!brokenJobs.isEmpty()) {
			sendMessages(chatId, brokenJobs);
		} else {
			sendMessage(chatId, "All jobs are OK!");
		}
	}

	private void sendMessages(long chatId, List<String> messages) {
		StringBuilder builder = new StringBuilder();

		boolean first = true;
		for (String msg : messages) {
			if (first) {
				first = false;
			} else {
				builder.append("\n");
			}
			builder.append(msg);
		}

		sendMessage(chatId, builder.toString());
	}

	private void sendMessage(long chatId, String message) {
		Maybe<Message> sendMessage = telegramBot.sendMessage(chatId, message);
		sendMessage.subscribe(m -> LOG.debug(m.getText()));
	}

	private void saveQuery(Message message) {
		User from = message.getFrom();
		Optional<ru.gumerbaev.amfibot.database.entities.User> userOptional = userRepository
				.findUserByTelegramId(from.getId());

		ru.gumerbaev.amfibot.database.entities.User user = userOptional.orElseGet(() -> {
			ru.gumerbaev.amfibot.database.entities.User newUser = new ru.gumerbaev.amfibot.database.entities.User();
			newUser.setTelegramId(from.getId());
			newUser.setFirst_name(from.getFirst_name());
			newUser.setLast_name(from.getLast_name());
			newUser.setUsername(from.getUsername());
			newUser.set_bot(from.is_bot());
			newUser.setLanguageCode(from.getLanguage_code());

			userRepository.save(newUser);

			return newUser;
		});
		Query query = new Query();
		query.setUser(user);
		query.setMessage(message.getText());
		int date = message.getDate();
		query.setDate(new Date(date));

		queryRepository.save(query);
	}
}
