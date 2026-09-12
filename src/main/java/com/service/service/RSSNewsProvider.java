package com.service.service;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.service.entity.CodeValue;
import com.service.exception.NewsProviderException;
import com.service.repository.ArticleRepository;
import com.service.repository.CodeValueRepository;
import com.service.vo.EnrichedNews;
import com.service.vo.NewsItem;
import com.rometools.rome.feed.module.Module; 
import com.rometools.modules.mediarss.MediaEntryModule;

@Service
public class RSSNewsProvider implements NewsProvider{
	
	private final ArticleRepository articleRepository;
	
	private final NewsEnrichmentService newsEnrichmentService;
	
	private final CodeValueRepository codeValueRepository;

	public RSSNewsProvider(ArticleRepository articleRepository, 
			NewsEnrichmentService newsEnrichmentService, CodeValueRepository codeValueRepository) {
		
		this.articleRepository = articleRepository;
		this.newsEnrichmentService = newsEnrichmentService;
		this.codeValueRepository = codeValueRepository;
	}

	@Override
	public List<NewsItem> fetchNews(String sourceUrl) {
		try {
			
			HttpResponse<InputStream> response = this.getFeed(sourceUrl);
			
			try (InputStream inputStream = response.body();
				     XmlReader reader = new XmlReader(inputStream)) {

	            SyndFeed feed = new SyndFeedInput().build(reader);
	
	            return feed.getEntries().stream().limit(10)
				            		.filter(entry -> (articleRepository.findByOriginalUrl(entry.getLink()) == null))
				            		.map(this::toNewsItem).filter(Objects::nonNull)
				            		.toList();
			}

        } catch (Exception e) {
            throw new NewsProviderException("Failed to fetch RSS feed", e);
        }
	}
	
	private HttpResponse<InputStream> getFeed(String sourceUrl) {
		
		try {
			
			URI feedUri = URI.create(sourceUrl);
			
			HttpClient httpClient = HttpClient.newBuilder()
								        .followRedirects(HttpClient.Redirect.NORMAL)
								        .build();

			HttpRequest request = HttpRequest.newBuilder()
								        .uri(feedUri)
								        .header("User-Agent",
								                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
								                + "(KHTML, like Gecko) Chrome/151.0.0.0 Safari/537.36")
								        .header("Accept", "application/rss+xml, application/xml, text/xml, */*")
								        .GET()
								        .build();

			HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());

			if (response.statusCode() != 200) {
			    throw new RuntimeException("RSS request failed: HTTP " + response.statusCode());
			}
			
			return response;
			
		} catch(Exception e) {
			throw new RuntimeException("Failed to call RSS Feed", e);
		}
	}
	
	private NewsItem toNewsItem(SyndEntry entry) {
		
		NewsItem newsItem = new NewsItem();
		
		newsItem.setTitle(entry.getTitle());
		newsItem.setSummary(entry.getDescription() != null ? entry.getDescription().getValue() : null);
		newsItem.setImageUrl(extractImageUrl(entry));
		
		newsItem.setUrl(entry.getLink());
		newsItem.setPublishedAt(entry.getPublishedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		
		EnrichedNews enrichedNews = newsEnrichmentService.enrich(newsItem);
		
		List<CodeValue> categoryTypeList = codeValueRepository.findByDomain("CATEGORY_TYPE");
		
		CodeValue category = categoryTypeList.stream()
										.filter(x -> enrichedNews.getCategory().equalsIgnoreCase(x.getValue()))
										.findFirst()
								        .orElse(null);
		
		newsItem.setCategoryCode(category.getCode());
		
		if(enrichedNews.getLocation() != null) {
			List<CodeValue> locationList = codeValueRepository.findByDomain("GEO_LOCATION");
			
			CodeValue location = locationList.stream()
											.filter(x -> enrichedNews.getLocation().equalsIgnoreCase(x.getValue()))
											.findFirst()
									        .orElse(null);
			
			newsItem.setLocation(location != null ? location.getCode() : "OTH");
		} else {
			newsItem.setLocation("OTH");
		}
		
        return newsItem;
    }
	
	private String extractImageUrl(SyndEntry entry) {
		
	    // 1. Try enclosure
	    if (entry.getEnclosures() != null && !entry.getEnclosures().isEmpty()) {
	        return entry.getEnclosures().get(0).getUrl();
	    }

	    // 2. Try Media RSS
	    for (Module module : entry.getModules()) {
	    	
	        if (module instanceof MediaEntryModule mediaModule) {

	            if (mediaModule.getMediaContents() != null && mediaModule.getMediaContents().length > 0) {
	                return mediaModule.getMediaContents()[0].getReference().toString();
	            }
	        }
	    }
	    return null;
	}
}
