package ru.gumerbaev.amfibot.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {

	private int message_id;
	private User from;
	private int date;
	private Chat chat;
	private String text;
}
