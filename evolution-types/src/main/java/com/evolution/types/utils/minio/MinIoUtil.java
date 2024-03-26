package com.evolution.types.utils.minio;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.evolution.types.enums.FileTypeEnum;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MinIoUtil {

    /**
     * 生成对象名称，格式为时间戳/原始文件名
     *
     * @param fileExtension 文件后缀名
     * @return 生成的对象名称
     */
    public static String generateObjectName(String fileExtension) {
        String timestamp = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now());
        String nextIdStr = IdUtil.simpleUUID();
        String sanitizedFilename = nextIdStr + "." + fileExtension;
        return timestamp + "/" + sanitizedFilename;
    }

    /**
     * 创建存储桶的访问策略，设置为只读权限
     *
     * @param bucketName 存储桶名称
     * @return 存储桶访问策略配置对象
     */
    public static BucketPolicyConfigDto createBucketPolicyConfigDto(String bucketName) {
        BucketPolicyConfigDto.Statement statement = BucketPolicyConfigDto.Statement.builder()
                .Effect("Allow")
                .Principal("*")
                .Action("s3:GetObject")
                .Resource("arn:aws:s3:::" + bucketName + "/*.**")
                .build();
        return BucketPolicyConfigDto.builder()
                .Version("2012-10-17")
                .Statement(CollUtil.toList(statement))
                .build();
    }

    /**
     * 根据文件名获取文件后缀名
     *
     * @param filename 文件名
     * @return 文件后缀名
     */
    public static String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        return (lastDotIndex != -1) ? filename.substring(lastDotIndex + 1).toLowerCase() : "";
    }

    /**
     * 根据文件后缀名获取或生成对应的存储桶名称
     *
     * @param filename 文件名
     * @return 存储桶名称
     */
    public static String getBucketNameByFileExtension(String filename) {
        String fileExtension = getFileExtension(filename);

        // 判断文件类型
        FileTypeEnum fileType = FileTypeEnum.getFileType(fileExtension);
        String sanitizedFileExtension = fileExtension.replaceAll("[^a-zA-Z0-9]", "_");
        String bucketName = fileType.getPrefix() + "-" + sanitizedFileExtension;

        // 确保桶名称的长度在限制范围内
        if (bucketName.length() > 63) {
            bucketName = bucketName.substring(0, 63);
        }

        return bucketName;
    }

}
