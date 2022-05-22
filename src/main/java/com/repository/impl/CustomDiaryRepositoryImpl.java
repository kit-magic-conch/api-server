package com.repository.impl;

import com.domain.PrivacyType;
import com.domain.entity.Diary;
import com.domain.entity.QDiary;
import com.domain.entity.QTag;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.repository.CustomDiaryRepository;
import com.util.QuerydslUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CustomDiaryRepositoryImpl implements CustomDiaryRepository {

    private final JPAQueryFactory queryFactory;
    private final QDiary qDiary = QDiary.diary;
    private final QTag qTag = QTag.tag;

    @Override
    public List<Diary> findDiariesPermittedByTagsContains(Long accountId, String key, Pageable pageable) {
        // TODO: 배치사이즈 사용해서 N + 1 문제 해결하기
        return queryFactory.selectFrom(qDiary)
                .where(hasAccess(accountId),
                        hasTagNameLike(key))
                .orderBy(QuerydslUtils.ordersFromPageable(pageable, qDiary))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression hasAccess(Long accountId) {
        return qDiary.account.id.eq(accountId)
                .or(qDiary.privacy.eq(PrivacyType.PUBLIC));
    }

    private BooleanExpression hasTagNameLike(String key) {
        if (key == null)
            return null;

        return JPAExpressions
                .selectDistinct(qTag)
                .from(qTag)
                .where(qTag.name.contains(key),
                        qTag.diary.eq(qDiary))
                .exists();
    }
}
