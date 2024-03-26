package com.evolution.types.enums;

import lombok.Getter;

/**
 * 枚举类型，表示文件类型
 */
@Getter
public enum FileTypeEnum {
    IMAGE("image", "jpg", "jpeg", "png", "gif", "bmp", "tiff", "webp","jfif"),
    VIDEO("video", "mp4", "avi", "mkv"),
    AUDIO("audio", "mp3", "wav", "flac"),
    FILE("file");

    private final String prefix;
    private final String[] extensions;

    FileTypeEnum(String prefix, String... extensions) {
        this.prefix = prefix;
        this.extensions = extensions;
    }

    public static FileTypeEnum getFileType(String extension) {
        for (FileTypeEnum fileType : values()) {
            for (String ext : fileType.extensions) {
                if (ext.equalsIgnoreCase(extension)) {
                    return fileType;
                }
            }
        }
        return FILE;
    }
}

