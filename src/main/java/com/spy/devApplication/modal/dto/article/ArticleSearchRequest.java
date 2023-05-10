package com.spy.devApplication.modal.dto.article;

import com.spy.devApplication.modal.dto.common.PageRequest;
import lombok.Data;

@Data
public class ArticleSearchRequest extends PageRequest {
    private String param;
}
