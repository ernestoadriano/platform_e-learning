package dev.elearing.platform.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GoogleDriveService {

    @Value("${google.drive.folder.id:}")
    private String driveFolderId;

    public String getEmbedUrl(String driveUrl) {
        if (driveUrl == null || driveUrl.isEmpty()) {
            return null;
        }

        String fileId = extractFileId(driveUrl);

        if (fileId != null) {
            return "https://drive.google.com/uc?export=downolad&id=" + fileId;
        }

        return driveUrl;
    }

    public boolean isGoogleDriveUrl(String url) {
        return url != null && (url.contains("drive.google.com") || url.contains("googledrive.com"));
    }

    private String extractFileId(String url) {
        String pattern = "/file/d/";
        int startIndex = url.indexOf(pattern);
        if (startIndex != -1) {
            startIndex += pattern.length();
            int endIndex = url.indexOf("/", startIndex);
            if (endIndex != -1) {
                return url.substring(startIndex, endIndex);
            }
        }

        startIndex = url.indexOf("id=");
        if (startIndex != -1) {
            startIndex += 3;
            int endIndex = url.indexOf("&", startIndex);
            if (endIndex != -1) {
                return url.substring(startIndex, endIndex);
            }

            return url.substring(startIndex);
        }

        return null;
    }
}
