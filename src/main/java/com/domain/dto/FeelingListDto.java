package com.domain.dto;

import lombok.Getter;

@Getter
public class FeelingListDto {
    private long[] ids = new long[32];
    private int[] feelings = new int[32];
    private int[] feelingsCount = new int[6];
}
