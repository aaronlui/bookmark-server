package com.lhboy.bookmark.service;

import com.lhboy.bookmark.entity.Bookmark;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class MetadataService {

    public void fillMetadata(Bookmark bookmark) {

        if (!StringUtils.hasText(bookmark.getUrl())) {
            return;
        }

        try {
            Document doc = Jsoup.connect(bookmark.getUrl())
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/150.0.0.0 Safari/537.36")
                    .timeout(5000)
                    .followRedirects(true)
                    .get();

            String coverUrl = doc.select("meta[property=og:image]").stream()
                    .map(el -> el.absUrl("content"))
                    .filter(StringUtils::hasText)
                    .findFirst()
                    .orElse(null);

            String faviconUrl = doc.select("link[rel~=(?i)^(shortcut )?icon]").stream()
                    .map(el -> el.absUrl("href"))
                    .filter(StringUtils::hasText)
                    .findFirst()
                    .orElse(null);

            if (!StringUtils.hasText(bookmark.getCoverUrl()) && StringUtils.hasText(coverUrl)) {
                bookmark.setCoverUrl(coverUrl);
            }
            if (!StringUtils.hasText(bookmark.getFaviconUrl()) && StringUtils.hasText(faviconUrl)) {
                bookmark.setFaviconUrl(faviconUrl);
            }
        } catch (Exception e) {
            // 跳过
            log.warn("抓取元数据失败: {}", bookmark.getUrl(), e);
        }
    }
}
