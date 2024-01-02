package com.multibook.bookorder.domain.product.product.repository;

import com.multibook.bookorder.domain.member.member.entity.Member;
import com.multibook.bookorder.domain.product.product.entity.Product;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import java.util.ArrayList;
import java.util.List;

import static com.multibook.bookorder.domain.product.product.entity.QProduct.product;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Product> search(Member maker, Boolean published, List<String> kwTypes, String kw, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        if(maker != null){
            builder.and(product.maker.eq(maker));
        }

        if(published!=null){
            builder.and(product.published.eq(published));
        }

        if(!kw.isBlank()){
            //기존의 조건을 리스트에 추가한다.
            List<BooleanExpression> conditions = new ArrayList<>();

            if(kwTypes.contains("name")){
                conditions.add(product.name.containsIgnoreCase(kw));
            }

            //조건 리스트를 or 조건으로 결합
            BooleanExpression combinedCondition = conditions.stream()
                    .reduce(BooleanExpression::or)
                    .orElse(null);

            //최종적으로 생성된 조건을 쿼리에 적용한다.
            if(combinedCondition!=null){
                builder.and(combinedCondition);
            }
        }

        JPAQuery<Product> productsQuery = jpaQueryFactory
                .selectFrom(product)
                .where(builder);

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(product.getType(), product.getMetadata());
            productsQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(o.getProperty())));
        }

        productsQuery.offset(pageable.getOffset()).limit(pageable.getPageSize());

        JPAQuery<Long> totalQuery = jpaQueryFactory
                .select(product.countDistinct())
                .from(product)
                .where(builder);

        return PageableExecutionUtils.getPage(productsQuery.fetch(),pageable,totalQuery::fetchOne);
    }
}
