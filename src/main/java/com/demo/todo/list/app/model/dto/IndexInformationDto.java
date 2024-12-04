package com.demo.todo.list.app.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
@RequiredArgsConstructor
public class IndexInformationDto {

    public final String name;
    public final String bucket;
    public final String status;
    public final String lastScanTime;

    public static IndexInformationDto create(Map<?, ?> asMap) {
        return new IndexInformationDto(
                (String) asMap.get("index"),
                (String) asMap.get("bucket"),
                (String) asMap.get("status"),
                (String) asMap.get("lastScanTime")
        );
    }

    public String getQualified() {
        return this.bucket + ":" + this.name;
    }
}
