/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.DraftHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A Roster represntation that holds a list of {@code RosterSpots}.
 * @author blarg
 */
public class Roster {
     /**
     * Internal representation of a full Rsoter list
     */
    public List< RosterSpot > roster;
    private Integer[] divisionSizes;

    public Roster() {
        this(5);
    }

    public Roster(int teamSize) {
        this.roster = new ArrayList<>(teamSize);
        divisionSizes = new Integer[]{0,0,0,0,0};
    }

    /**
     *
     * @return
     * @deprecated
     */
    public List< RosterSpot > getRoster() {
        return roster;
    }

    /**
     *
     * @param rs
     * @throws IndexOutOfBoundsException
     * @Depreceated
     */
    public void addRikishi(RosterSpot rs) throws IndexOutOfBoundsException {
        roster.add(rs);
        divisionSizes[rs.div - 1] += 1;
    }

    public void addRikishi(int draftPos_, String shikona_, String rank_, int div_) {
        RosterSpot rs = new RosterSpot(draftPos_,  shikona_,  rank_, div_);
        roster.add(rs);
        divisionSizes[rs.div - 1] += 1;
    }

    public int getDivisionCount(int i) throws IndexOutOfBoundsException {
        return divisionSizes[i - 1];
    }

    /**
     * Return the Roster spot in the form of a list of strings.
     *
     * @return
     * Elements in a list ordered:
     * {@code [0: draftPos, 1: shikona, 2: rank, 3: div]}
     */
    public List< List<String> > asList() {
        return new ArrayList<>(){
            {
                for (var rs : roster) {
                    add(rs.asList());
                }
            }
        };
    }

    // /**
    //  * Return the Roster spot in the form of a list of strings.
    //  *
    //  * @return
    //  * Elements in a list ordered:
    //  * [{@code draftPos}, {@code shikona}, {@code rank}, {@code div}]
    //  */
    // public Map<String,String> asMap() {
    //     return Map.of(
    //         "draftPos",String.valueOf(draftPos),
    //         "shikona",shikona,
    //         "rank",rank,
    //         "div",String.valueOf(div)
    //     );
    // }

    /**
     * Internal Representation of a rikishi occupying a roster spot. Composed of
     * four components: {@code draftPos}, their name ({@code shikona}),
     * {@code rank}, and {@code div}.
     */
    private class RosterSpot {
        /**
         * Constructor
         * @param draftPos_ Draft Position, 1-indexed
         * @param shikona_ Name of the rikishi
         * @param rank_ Rank of the rikishi
         * @param div_ Division
         */
        public RosterSpot(int draftPos_, String shikona_, String rank_,
                int div_) {
            this.draftPos = draftPos_;
            this.shikona = shikona_;
            this.rank = rank_;
            this.div = div_;
        }

        /**
         * Return the Roster spot in the form of a list of strings.
         *
         * @return
         * Elements in a list ordered:
         * [{@code draftPos}, {@code shikona}, {@code rank}, {@code div}]
         */
        public List<String> asList() {
            return List.of(
                    String.valueOf(draftPos)
                    ,shikona
                    ,rank
                    ,String.valueOf(div)
            );
        }

        /**
         * Position rikishi is drafted
         */
        public final int draftPos;

        /**
         * Name of the rikishi
         */
        public final String shikona;

        /**
         * Rank of the rikishi
         */
        public final String rank;

        /**
         * Division rikishi is currently in
         */
        public final int div;
    }
}