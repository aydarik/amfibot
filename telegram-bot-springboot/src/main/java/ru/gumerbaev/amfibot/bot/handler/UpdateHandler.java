package ru.gumerbaev.amfibot.bot.handler;

import ru.gumerbaev.amfibot.api.model.Update;

public interface UpdateHandler {

	void handleUpdate(Update update);
}
