package ru.gumerbaev.amfibot.bot.jenkins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JenkinsWrapper {

	private List<JenkinsJob> jobs;
}
