package ru.gumerbaev.amfibot.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramResponse<T> {

	private boolean ok;
	private List<T> result;
}
