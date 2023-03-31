package utils;

public class Queries {
    public static String eventBySlug = "query EventEntrants($slug: String!, $page: Int!, $perPage: Int!) {\n" +
            "  event(slug: $slug) {\n" +
            "    id\n" +
            "    numEntrants\n" +
            "    name\n" +
            "    tournament{\n" +
            "       city\n" +
            "       name\n" +
            "       startAt\n" +
            "       endAt\n" +
            "    }\n"+
            "    entrants(query: {\n" +
            "      page: $page\n" +
            "      perPage: $perPage\n" +
            "    }) {\n" +
            "      pageInfo {\n" +
            "        total\n" +
            "        totalPages\n" +
            "      }\n" +
            "      nodes {\n" +
            "        id\n" +
            "        isDisqualified\n" +
            "        standing {\n" +
            "          placement\n" +
            "        }\n" +
            "        participants {\n" +
            "          user {\n" +
            "           player{\n" +
            "               id\n" +
            "           }\n" +
            "          }\n" +
            "          gamerTag\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

    public static String eventSetsBySlug = "query EventSets($slug: String!, $page: Int!, $perPage: Int!) {\n" +
            "  event(slug: $slug) {\n" +
            "    id\n" +
            "    name\n" +
            "    sets(\n" +
            "      page: $page\n" +
            "      perPage: $perPage\n" +
            "      sortType: STANDARD\n" +
            "    ) {\n" +
            "      pageInfo {\n" +
            "        total\n" +
            "      }\n" +
            "      nodes {\n" +
            "        id\n" +
            "        winnerId\n" +
            "        slots {\n" +
            "          id\n" +
            "          entrant {\n" +
            "            id\n" +
            "            name\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";


    public static String eventBySlugVariablesMapper(String slug) {
        return "{\n" +
                "  \"slug\": \"" + slug + "\",\n" +
                "  \"page\": 1,\n" +
                "  \"perPage\": 128\n" +
                "}";
    }

    public static String eventSetsBySlugVariablesMapper(String slug) {
        return "{\n" +
                "  \"slug\": \"" + slug + "\",\n" +
                "  \"page\": 1,\n" +
                "  \"perPage\": 249\n" +
                "}";
    }
}
