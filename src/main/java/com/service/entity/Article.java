package com.service.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ARTICLE")
public class Article {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "SOURCE_ID", nullable = false)
	private Source source;
	
	@Column(name = "TITLE", nullable = false)
	private String title;
	
	@Column(name = "DESCRIPTION", nullable = false)
	private String summary;
	
	@Column(name = "IMAGE_URL", nullable = true)
	private String imageUrl;
	
	@Column(name = "LANGUAGE", nullable = false)
	private String language;
	
	@Column(name = "LOCATION", nullable = false)
	private String location;
	
	@Column(name = "CATEGORY_CODE", nullable = false)
	private String categoryCode;
	
	@Column(name = "ORIGINAL_URL", nullable = false)
	private String originalUrl;
	
	@Column(name = "PUBLISHED_BY", nullable = true)
	private String publishedBy;
	
	@Column(name = "PUBLISHED_AT", nullable = false)
	private LocalDateTime publishedAt;
	
	@Column(name = "CREATED_AT", nullable = false)
	private LocalDateTime createdAt;
	
	public Article() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	
	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	public String getPublishedBy() {
		return publishedBy;
	}

	public void setPublishedBy(String publishedBy) {
		this.publishedBy = publishedBy;
	}

	public LocalDateTime getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt(LocalDateTime publishedAt) {
		this.publishedAt = publishedAt;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
