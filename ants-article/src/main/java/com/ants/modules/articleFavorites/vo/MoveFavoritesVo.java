package com.ants.modules.articleFavorites.vo;

import lombok.Data;

import java.util.List;

/**
 * TODO
 * Author Chen
 * Date   2021/10/19 17:15
 */
@Data
public class MoveFavoritesVo {

    private String selectMainId;
    private String oldMainId;
    private List<String> moveSelectKeys;
}
