package utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Constants {

    public static String API_KEY = "";
    public static String API_URL = "https://api.smash.gg/gql/alpha";
    public static List<String> COVID_SLUGS = Arrays.asList("tournament/blast-zone-tournament-12-smashers-from-the-block/event/ultimate-singles", "tournament/blast-zone-tournament-11-emergency-meeting/event/ultimate-singles","tournament/blast-zone-tournament-10-blast-zone-s-birthday/event/ultimate-singles","tournament/blast-zone-tournament-10-blast-zone-s-birthday/event/ultimate-singles","tournament/blast-zone-tournament-9-way-to-school-1/event/ultimate-singles","tournament/blast-zone-tournament-8-a-new-beginning/event/ultimate-singles","tournament/blast-zone-tournament-7-blast-zone-s-champion/event/ultimate-singles","tournament/monthly-5-amiens-match-up-29-f-vrier-2020/event/tournoi-1v1-au-sports-bar","tournament/blast-zone-tournament-6-winds-of-smash/event/ultimate-singles");
    public static List<String> POST_COVID_SLUGS = Arrays.asList("tournament/week-end-smash-star-k-o-round-3-blast-zone/event/star-k-o-beyond-the-blast-zone-round-3", "tournament/week-end-smash-star-k-o-round-3-blast-zone/event/blast-zone-tournament-21-last-before-christmas","tournament/blast-zone-tournament-20-steppin-up/event/ultimate-singles","tournament/star-k-o-beyond-the-blast-zone-round-2/event/ultimate-singles","tournament/blast-zone-tournament-19-key-of-success/event/ultimate-singles","tournament/star-k-o-beyond-the-blast-zone-round-1/event/ultimate-singles","tournament/blast-zone-tournament-18-before-the-star-falls/event/ultimate-singles","tournament/blast-zone-tournament-16-friend-or-foe/event/ultimate-singles","tournament/blast-zone-tournament-15-north-unite/event/ultimate-singles","tournament/blast-zone-tournament-14-path-of-summer/event/ultimate-singles","tournament/blast-zone-tournament-13-the-iron-fistbump-1/event/ultimate-singles") ;
    public static float ALL_MONTHLIES_CONSISTENCY_MULTIPLICATOR = 1.75f;
    public static float ALL_WEEKLIES_CONSISTENCY_MULTIPLICATOR = 1.5f;
    public static float WIN_LOSS_IMPACT_SCORE = 2f;
    public static float CONSISTENCY_COEFFICIENT = 1f;
    public static float WIN_LOSS_IMPACT_COEFFICIENT = 1f;
    public static int TOURNAMENT_NUMBER_THRESHOLD = 6;

    public static Map<Integer, Integer> WEEKLY_CONSISTENCY_POINTS = Stream.of(new Integer[][]{
            {1, 100},
            {2, 90},
            {3, 81},
            {4, 72},
            {5, 63},
            {7, 54},
            {9, 45},
            {13, 36},
            {17, 27},
            {25, 18},
            {33, 9}
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

    public static Map<Integer, Integer> MONTHLY_CONSISTENCY_POINTS = Stream.of(new Integer[][]{
            {1, 100},
            {2, 92},
            {3, 84},
            {4, 76},
            {5, 69},
            {7, 61},
            {9, 53},
            {13, 46},
            {17, 38},
            {25, 30},
            {33, 23},
            {49, 15},
            {65, 7}
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]));


}
