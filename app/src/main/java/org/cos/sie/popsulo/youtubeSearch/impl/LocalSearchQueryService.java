package org.cos.sie.popsulo.youtubeSearch.impl;

import org.apache.commons.lang3.StringUtils;
import org.cos.sie.popsulo.LocalDiskCache;
import org.cos.sie.popsulo.app.QueryResult;
import org.cos.sie.popsulo.youtubeSearch.SearchQueryService;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class LocalSearchQueryService
    implements SearchQueryService
{
    private static final int DEFAULT_NUMBER_COUNT = 20;

    @Override public List<QueryResult> queryYoutube(String queryString)
        throws IOException
    {
        List<QueryResult> localCached = LocalDiskCache.getInstance().getVidIDs().values().stream().filter(
            queryResult -> Objects.nonNull(queryResult)).collect(Collectors.toList());
        Collections.sort(localCached, (o1, o2) -> {

            int i1 = StringUtils.getLevenshteinDistance(queryString, o1.getTitle());
            int i2 = StringUtils.getLevenshteinDistance(queryString, o2.getTitle());

            if (i1 < i2) {
                return -1;
            } else if (i2 < i1) {
                return 1;
            }
            return 0;
        });

        List<QueryResult> result =
            (localCached.size() > DEFAULT_NUMBER_COUNT) ? (localCached.subList(0, DEFAULT_NUMBER_COUNT)) : localCached;

        result.forEach(queryResult -> queryResult.initCachedState());
        return result;
    }
}
