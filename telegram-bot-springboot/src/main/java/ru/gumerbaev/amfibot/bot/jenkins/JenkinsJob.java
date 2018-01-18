package ru.gumerbaev.amfibot.bot.jenkins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JenkinsJob {

	private String name;

	private String color;
}
