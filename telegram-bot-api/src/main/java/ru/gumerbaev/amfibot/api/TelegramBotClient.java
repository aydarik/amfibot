package ru.gumerbaev.amfibot.api;

import io.reactivex.Maybe;
import retrofit2.http.*;
import ru.gumerbaev.amfibot.api.model.Message;
import ru.gumerbaev.amfibot.api.model.TelegramResponse;
import ru.gumerbaev.amfibot.api.model.Update;

public interface TelegramBotClient {

	@POST("sendMessage")
	@FormUrlEncoded
	Maybe<Message> sendMessage(@Field("chat_id") long chatId, @Field("text") String text);

	@POST("sendPhoto")
	@FormUrlEncoded
	Maybe<Message> sendPhoto(@Field("chat_id") long chatId, @Field("photo") String photoUrl);

	@GET("getUpdates")
	Maybe<TelegramResponse<Update>> getUpdates();

	@GET("getUpdates")
	Maybe<TelegramResponse<Update>> getUpdates(@Query("offset") int offset);
}
