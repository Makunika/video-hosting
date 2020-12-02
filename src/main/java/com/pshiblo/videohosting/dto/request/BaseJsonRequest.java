package com.pshiblo.videohosting.dto.request;

import com.pshiblo.videohosting.models.BaseEntity;

/**
 * @author Максим Пшибло
 */
public interface BaseJsonRequest<T> {
    T toEntity(BaseEntity... entities);
}
