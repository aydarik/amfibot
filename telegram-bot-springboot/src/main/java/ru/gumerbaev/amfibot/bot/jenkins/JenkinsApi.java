package ru.gumerbaev.amfibot.bot.jenkins;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface JenkinsApi {
	
	String BASE_URL = "http://192.168.124.214:8080/api/";
	
	@GET("json?tree=jobs[name,color]")
	Single<JenkinsWrapper> getStatus();

/*
	default String getJenkinsUrl() {
		return "http://192.168.124.214:8080/";
	}
*/
}
