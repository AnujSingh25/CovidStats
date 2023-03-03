package com.covidStats.services;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.covidStats.models.LocationStats;

import jakarta.annotation.PostConstruct;

@Service
public class CoronaVirusDataService {

	private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/01-01-2021.csv";

	private List<LocationStats> allStats = new ArrayList<LocationStats>();

	public List<LocationStats> getAllStats() {
		return allStats;
	}

	@PostConstruct
	@Scheduled(cron = "* * 1 * * *")
	public void fetchVirusData() throws IOException, InterruptedException {

		List<LocationStats> newStats = new ArrayList<>();

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(VIRUS_DATA_URL)).build();
		HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
//		System.out.println(httpResponse.body());

		StringReader csvBodyReader = new StringReader(httpResponse.body());

//		Reader in = new FileReader("path/to/file.csv");
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);

		for (CSVRecord record : records) {
			LocationStats locationStat = new LocationStats();

			locationStat.setState(record.get("Province_State"));
			locationStat.setCountry(record.get("Country_Region"));
			locationStat.setLatestTotalCases(Integer.parseInt(record.get("Confirmed")));
//			locationStat.setLatestTotalCases(record.get(record.size() - 1));

			System.out.println(locationStat);
//			String state = record.get("Province_State");
//			System.out.println(state);
//			String customerNo = record.get("CustomerNo");
//			String name = record.get("Name");
		}

		this.allStats = newStats;
	}

}